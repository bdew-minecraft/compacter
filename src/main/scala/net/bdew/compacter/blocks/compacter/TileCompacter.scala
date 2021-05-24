package net.bdew.compacter.blocks.compacter

import net.bdew.compacter.Config
import net.bdew.compacter.misc._
import net.bdew.compacter.registries.Blocks
import net.bdew.lib.Text
import net.bdew.lib.capabilities.Capabilities
import net.bdew.lib.capabilities.handlers.{PowerEnergyHandler, SidedInventoryItemHandler}
import net.bdew.lib.data.base.{DataSlot, TileDataSlotsTicking}
import net.bdew.lib.data.{DataSlotEnum, DataSlotInventory}
import net.bdew.lib.inventory.RestrictedInventory
import net.bdew.lib.items.ItemUtils
import net.bdew.lib.misc.RSMode
import net.bdew.lib.power.DataSlotPower
import net.bdew.lib.tile.{BreakListeningTile, TileExtended}
import net.minecraft.entity.player.{PlayerEntity, PlayerInventory}
import net.minecraft.inventory.container.{Container, INamedContainerProvider}
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntityType
import net.minecraft.util.Direction
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.energy.IEnergyStorage
import net.minecraftforge.items.IItemHandler

import scala.collection.mutable

class TileCompacter(teType: TileEntityType[_]) extends TileExtended(teType)
  with TileDataSlotsTicking with INamedContainerProvider with BreakListeningTile {

  object Slots {
    val input: Range.Inclusive = 0 to 26
    val output: Range.Inclusive = 27 to 42
  }

  var haveWork = true
  var checkRecipes = true
  var needPower = true

  val rsMode: DataSlotEnum[RSMode.type] = DataSlotEnum("rsMode", this, RSMode)
  val craftMode: DataSlotEnum[CraftMode.type] = DataSlotEnum("craftMode", this, CraftMode)
  val recurseMode: DataSlotEnum[RecurseMode.type] = DataSlotEnum("recurseMode", this, RecurseMode)
  val outputQueue: DataSlotItemQueue = DataSlotItemQueue("outputQueue", this)
  val inputQueue: DataSlotItemQueue = DataSlotItemQueue("inputQueue", this)
  val power: DataSlotPower = DataSlotPower("power", this)
  val inventory: DataSlotInventory = DataSlotInventory("inv", this, 43)

  val externalInventory = new RestrictedInventory(inventory,
    canExtract = (slot, _) => Slots.output.contains(slot),
    canInsert = (slot, stack, _) => Slots.input.contains(slot) && inputQueue.isEmpty && canCraftItem(stack)
  )

  val inventoryHandler: LazyOptional[IItemHandler] = SidedInventoryItemHandler.create(externalInventory)
  val powerHandler: LazyOptional[IEnergyStorage] = PowerEnergyHandler.create(power, true, false)

  power.configure(Config.Compacter)

  serverTick.listen(doTick)

  //noinspection ComparingUnrelatedTypes
  override def getCapability[T](cap: Capability[T], side: Direction): LazyOptional[T] = {
    if (cap == Capabilities.CAP_ITEM_HANDLER)
      inventoryHandler.cast()
    else if (cap == Capabilities.CAP_ENERGY_HANDLER)
      powerHandler.cast()
    else
      super.getCapability(cap, side)
  }

  override def getDisplayName: ITextComponent = Text.translate(Blocks.compacter.block.get().getDescriptionId)
  override def createMenu(id: Int, playerInventory: PlayerInventory, player: PlayerEntity): Container =
    new ContainerCompacter(this, playerInventory, id)

  override def dataSlotChanged(slot: DataSlot): Unit = {
    if (needPower && slot == power) {
      haveWork = true
      needPower = false
    } else if (!haveWork && slot == inventory) {
      haveWork = true
    }
    super.dataSlotChanged(slot)
  }

  def getNonEmptyStack(slot: Int): Option[ItemStack] = {
    inventory.getItem(slot) match {
      case x if x.isEmpty => None
      case x => Some(x)
    }
  }

  def doTick(): Unit = {
    if (checkRecipes) {
      checkRecipes = false
      for (slot <- Slots.input; stack <- getNonEmptyStack(slot) if !canCraftItem(stack)) {
        outputQueue.push(stack)
        inventory.setItem(slot, ItemStack.EMPTY)
      }
    }

    processInputQueue()

    if (outputQueue.nonEmpty) {
      processOutputQueue()
      if (haveWork && outputQueue.nonEmpty) haveWork = false
    }
    if (haveWork && !canWorkRS) haveWork = false
    if (!haveWork) return

    val inputs = mutable.Map.empty[ItemDef, Int].withDefaultValue(0)

    for (slot <- Slots.input; stack <- getNonEmptyStack(slot)) {
      inputs(ItemDef(stack)) += stack.getCount
      inventory.setItem(slot, ItemStack.EMPTY)
    }

    craftMode.value match {
      case CraftMode.THREE_ONLY =>
        tryCraftAllStacks(inputs, CompacterCaches.cache3x3)
      case CraftMode.TWO_ONLY =>
        tryCraftAllStacks(inputs, CompacterCaches.cache2x2)
      case CraftMode.TWO_THREE =>
        tryCraftAllStacks(inputs, CompacterCaches.cache2x2)
        tryCraftAllStacks(inputs, CompacterCaches.cache3x3)
      case CraftMode.THREE_TWO =>
        tryCraftAllStacks(inputs, CompacterCaches.cache3x3)
        tryCraftAllStacks(inputs, CompacterCaches.cache2x2)
      case CraftMode.ONE_ONLY =>
        tryCraftAllStacks(inputs, CompacterCaches.cache1x1)
      case CraftMode.HOLLOW =>
        tryCraftAllStacks(inputs, CompacterCaches.cacheHollow)
    }

    val putBack = inputs filter (_._2 > 0) flatMap { case (item, amount) =>
      splitToStacks(item.stack(), amount)
    }

    for ((stack, slot) <- putBack.zip(Slots.input)) {
      inventory.setItem(slot, stack)
    }

    if (outputQueue.nonEmpty) processOutputQueue()
    haveWork = false
    processInputQueue()
  }

  def processOutputQueue(): Unit = {
    while (outputQueue.nonEmpty) {
      var stack = outputQueue.pop()
      stack = ItemUtils.addStackToSlots(stack, inventory, Slots.output, false)
      if (!stack.isEmpty) {
        outputQueue.push(stack)
        return
      }
    }
  }

  def processInputQueue(): Unit = {
    while (inputQueue.nonEmpty) {
      var stack = inputQueue.pop()
      stack = ItemUtils.addStackToSlots(stack, inventory, Slots.input, false)
      if (!stack.isEmpty) {
        inputQueue.push(stack)
        return
      }
    }
  }

  def splitToStacks(stack: ItemStack, amount: Int): List[ItemStack] = {
    var left = amount
    val res = mutable.Buffer.empty[ItemStack]
    val maxStack = stack.getMaxStackSize
    stack.setCount(maxStack)
    while (left >= maxStack) {
      res += stack.copy()
      left -= maxStack
    }
    if (left > 0) {
      stack.setCount(left)
      res += stack
    }
    res.toList
  }

  def tryCraftAllStacks(stacks: mutable.Map[ItemDef, Int], mode: CompacterCache): Unit = {
    for ((item, amount) <- stacks.toList if amount >= mode.inputAmount) {
      var toOutput = amount / mode.inputAmount
      if (power.stored < Config.Compacter.craftEnergyUsed() * toOutput) {
        needPower = true
        return
      }
      val result = mode.getRecipe(item, level)
      if (!result.isEmpty) {
        power.extract(Config.Compacter.craftEnergyUsed() * toOutput, false)
        stacks(item) -= toOutput * mode.inputAmount
        toOutput *= result.getCount
        if (recurseMode.value == RecurseMode.ENABLED && canCraftItem(result))
          inputQueue.pushAll(splitToStacks(result, toOutput))
        else
          outputQueue.pushAll(splitToStacks(result, toOutput))
      }
    }
  }

  def canWorkRS: Boolean = rsMode.value match {
    case RSMode.ALWAYS => true
    case RSMode.NEVER => false
    case RSMode.RS_ON => level.hasNeighborSignal(worldPosition)
    case RSMode.RS_OFF => !level.hasNeighborSignal(worldPosition)
  }

  def canCraftItem(stack: ItemStack): Boolean = craftMode.value match {
    case CraftMode.THREE_ONLY =>
      CompacterCaches.cache3x3.hasRecipe(stack, level)
    case CraftMode.TWO_ONLY =>
      CompacterCaches.cache2x2.hasRecipe(stack, level)
    case CraftMode.ONE_ONLY =>
      CompacterCaches.cache1x1.hasRecipe(stack, level)
    case CraftMode.HOLLOW =>
      CompacterCaches.cacheHollow.hasRecipe(stack, level)
    case _ =>
      CompacterCaches.cache2x2.hasRecipe(stack, level) || CompacterCaches.cache3x3.hasRecipe(stack, level)
  }

  override def invalidateCaps(): Unit = {
    super.invalidateCaps()
    powerHandler.invalidate()
    inventoryHandler.invalidate()
  }

  override def onBlockBroken(): Unit = {
    inventory.dropContent(level, getBlockPos)
  }
}
