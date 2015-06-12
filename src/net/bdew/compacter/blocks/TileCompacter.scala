package net.bdew.compacter.blocks

import net.bdew.compacter.misc._
import net.bdew.lib.data.base.TileDataSlots
import net.bdew.lib.items.ItemUtils
import net.bdew.lib.multiblock.data.RSMode
import net.bdew.lib.tile.inventory.{BreakableInventoryTile, PersistentInventoryTile, SidedInventory}
import net.minecraft.item.ItemStack

import scala.collection.mutable

class TileCompacter extends TileDataSlots with PersistentInventoryTile with BreakableInventoryTile with SidedInventory {
  override def getSizeInventory = 43

  object Slots {
    val input = 0 to 26
    val output = 27 to 42
  }

  var haveWork = true
  var checkRecipes = true

  val rsMode = DataSlotEnum("rsMode", this, RSMode)
  val craftMode = DataSlotEnum("craftMode", this, CraftMode)
  val recurseMode = DataSlotEnum("recurseMode", this, RecurseMode)
  val outputQueue = DataSlotOutputQueue("outputQueue", this)

  serverTick.listen(doTick)

  def doTick(): Unit = {
    if (checkRecipes) {
      println("CHECK RECIPES")
      checkRecipes = false
      for (slot <- Slots.input; stack <- Option(getStackInSlot(slot)) if !canCraftItem(stack)) {
        outputQueue.push(stack)
        setInventorySlotContents(slot, null)
      }
    }

    if (outputQueue.nonEmpty) processOutputQueue()
    if (outputQueue.nonEmpty) haveWork = false
    if (!haveWork) return

    println("LOOP")

    val inputs = mutable.Map.empty[ItemDef, Int].withDefaultValue(0)

    for (slot <- Slots.input; stack <- Option(getStackInSlot(slot))) {
      inputs(ItemDef(stack)) += stack.stackSize
      setInventorySlotContents(slot, null)
    }

    craftMode.value match {
      case CraftMode.THREE_ONLY =>
        tryCraftAllStacks(inputs, CompacterCache3x3)
      case CraftMode.TWO_ONLY =>
        tryCraftAllStacks(inputs, CompacterCache2x2)
      case CraftMode.TWO_THREE =>
        tryCraftAllStacks(inputs, CompacterCache2x2)
        tryCraftAllStacks(inputs, CompacterCache3x3)
      case CraftMode.THREE_TWO =>
        tryCraftAllStacks(inputs, CompacterCache3x3)
        tryCraftAllStacks(inputs, CompacterCache2x2)
    }

    val putBack = inputs filter (_._2 > 0) flatMap { case (item, amount) =>
      splitToStacks(new ItemStack(item.item, 1, item.damage), amount)
    }

    for ((stack, slot) <- putBack.zip(Slots.input)) {
      setInventorySlotContents(slot, stack)
    }

    if (outputQueue.nonEmpty) processOutputQueue()
    haveWork = false
  }

  def processOutputQueue(): Unit = {
    while (outputQueue.nonEmpty) {
      var stack = outputQueue.pop()
      if ((recurseMode :== RecurseMode.ENABLED) && canCraftItem(stack)) {
        stack = ItemUtils.addStackToSlots(stack, this, Slots.input, false)
      } else {
        stack = ItemUtils.addStackToSlots(stack, this, Slots.output, false)
      }
      if (stack != null) {
        outputQueue.push(stack)
        return
      }
    }
  }


  def splitToStacks(stack: ItemStack, amount: Int): List[ItemStack] = {
    var left = amount
    val res = mutable.Buffer.empty[ItemStack]
    val maxStack = stack.getMaxStackSize
    stack.stackSize = maxStack
    while (left >= maxStack) {
      res += stack.copy()
      left -= maxStack
    }
    if (left > 0) {
      stack.stackSize = left
      res += stack
    }
    res.toList
  }

  def tryCraftAllStacks(stacks: mutable.Map[ItemDef, Int], mode: CompacterCache): Unit = {
    for ((item, amount) <- stacks.toList if amount >= mode.inputAmount) {
      var toOutput = amount / mode.inputAmount
      val result = mode.getRecipe(item, worldObj)
      if (result != null) {
        stacks(item) -= toOutput * mode.inputAmount
        toOutput *= result.stackSize
        outputQueue.pushAll(splitToStacks(result, toOutput))
      }
    }
  }

  override def isItemValidForSlot(slot: Int, stack: ItemStack) =
    Slots.input.contains(slot) && canCraftItem(stack)

  override def canInsertItem(slot: Int, stack: ItemStack, side: Int) =
    Slots.input.contains(slot) && canCraftItem(stack)

  override def canExtractItem(slot: Int, stack: ItemStack, side: Int) =
    Slots.output.contains(slot)

  def canCraftItem(stack: ItemStack): Boolean = craftMode.value match {
    case CraftMode.THREE_ONLY =>
      CompacterCache3x3.hasRecipe(stack, worldObj)
    case CraftMode.TWO_ONLY =>
      CompacterCache2x2.hasRecipe(stack, worldObj)
    case _ =>
      CompacterCache2x2.hasRecipe(stack, worldObj) || CompacterCache3x3.hasRecipe(stack, worldObj)
  }

  override def markDirty(): Unit = {
    super.markDirty()
    haveWork = true
  }
}
