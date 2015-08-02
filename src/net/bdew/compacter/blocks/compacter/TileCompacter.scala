/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/compacter
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.compacter.blocks.compacter

import net.bdew.compacter.misc._
import net.bdew.compacter.power.TilePowered
import net.bdew.lib.data.base.{DataSlot, TileDataSlots}
import net.bdew.lib.items.ItemUtils
import net.bdew.lib.multiblock.data.RSMode
import net.bdew.lib.power.DataSlotPower
import net.bdew.lib.tile.inventory.{BreakableInventoryTile, PersistentInventoryTile, SidedInventory}
import net.minecraft.item.ItemStack

import scala.collection.mutable

class TileCompacter extends TileDataSlots with PersistentInventoryTile with BreakableInventoryTile with SidedInventory with TilePowered {
  override def getSizeInventory = 43

  object Slots {
    val input = 0 to 26
    val output = 27 to 42
  }

  var haveWork = true
  var checkRecipes = true
  var needPower = true

  val rsMode = DataSlotEnum("rsMode", this, RSMode)
  val craftMode = DataSlotEnum("craftMode", this, CraftMode)
  val recurseMode = DataSlotEnum("recurseMode", this, RecurseMode)
  val outputQueue = DataSlotItemQueue("outputQueue", this)
  val inputQueue = DataSlotItemQueue("inputQueue", this)
  override val power = DataSlotPower("power", this)


  power.configure(MachineCompacter)
  serverTick.listen(doTick)

  override def dataSlotChanged(slot: DataSlot): Unit = {
    if (needPower && slot == power) {
      haveWork = true
      needPower = false
    }
    super.dataSlotChanged(slot)
  }

  def doTick(): Unit = {
    if (checkRecipes) {
      checkRecipes = false
      for (slot <- Slots.input; stack <- Option(getStackInSlot(slot)) if !canCraftItem(stack)) {
        outputQueue.push(stack)
        setInventorySlotContents(slot, null)
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
    processInputQueue()
  }

  def processOutputQueue(): Unit = {
    while (outputQueue.nonEmpty) {
      var stack = outputQueue.pop()
      stack = ItemUtils.addStackToSlots(stack, this, Slots.output, false)
      if (stack != null) {
        outputQueue.push(stack)
        return
      }
    }
  }

  def processInputQueue(): Unit = {
    while (inputQueue.nonEmpty) {
      var stack = inputQueue.pop()
      stack = ItemUtils.addStackToSlots(stack, this, Slots.input, false)
      if (stack != null) {
        inputQueue.push(stack)
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
      if (power.stored < MachineCompacter.activationEnergy * toOutput) {
        needPower = true
        return
      }
      val result = mode.getRecipe(item, worldObj)
      if (result != null) {
        power.extract(MachineCompacter.activationEnergy * toOutput, false)
        stacks(item) -= toOutput * mode.inputAmount
        toOutput *= result.stackSize
        if (recurseMode.value == RecurseMode.ENABLED && canCraftItem(result))
          inputQueue.pushAll(splitToStacks(result, toOutput))
        else
          outputQueue.pushAll(splitToStacks(result, toOutput))
      }
    }
  }

  def canWorkRS = rsMode.value match {
    case RSMode.ALWAYS => true
    case RSMode.NEVER => false
    case RSMode.RS_ON => worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord)
    case RSMode.RS_OFF => !worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord)
  }

  override def isItemValidForSlot(slot: Int, stack: ItemStack) =
    Slots.input.contains(slot) && inputQueue.isEmpty && canCraftItem(stack)

  override def canInsertItem(slot: Int, stack: ItemStack, side: Int) =
    Slots.input.contains(slot) && inputQueue.isEmpty && canCraftItem(stack)

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
