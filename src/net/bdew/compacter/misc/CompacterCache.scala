/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/compacter
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.compacter.misc

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.{Container, InventoryCrafting}
import net.minecraft.item.crafting.CraftingManager
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.world.World

case class ItemDef(item: Item, damage: Int)

object ItemDef {
  def apply(stack: ItemStack): ItemDef = ItemDef(stack.getItem, stack.getItemDamage)
}

object FakeContainer extends Container {
  override def canInteractWith(player: EntityPlayer) = true
}

class FakeInventory(size: Int, stack: ItemStack) extends InventoryCrafting(FakeContainer, size, size) {
  override def getSizeInventory = size * size
  override def markDirty(): Unit = {}
  override def getStackInRowAndColumn(x: Int, y: Int): ItemStack =
    if (x < size && y < size) stack else null
  override def getStackInSlot(slot: Int): ItemStack =
    if (slot < size * size) stack else null
}

class FakeInventoryHollow(stack: ItemStack) extends FakeInventory(3, stack) {
  override def getStackInRowAndColumn(x: Int, y: Int): ItemStack =
    if (x < 3 && y < 3 && !(x == 1 && y == 1)) stack else null
}

class CompacterCache(size: Int) {
  val negative = collection.mutable.Set.empty[ItemDef]
  val cache = collection.mutable.Map.empty[ItemDef, ItemStack]

  val inputAmount = size * size

  def hasRecipe(stack: ItemStack, world: World) =
    (!stack.hasTagCompound) && (getRecipe(stack, world) != null)

  def getRecipe(stack: ItemStack, world: World): ItemStack =
    getRecipe(ItemDef(stack), world)

  def getInventory(stack: ItemStack) = new FakeInventory(size, stack)

  def getRecipe(itemDef: ItemDef, world: World): ItemStack = {
    if (negative.contains(itemDef)) return null
    if (cache.contains(itemDef)) return cache(itemDef).copy()

    val fakeInventory = getInventory(new ItemStack(itemDef.item, 1, itemDef.damage))
    val result = CraftingManager.getInstance.findMatchingRecipe(fakeInventory, world)
    if (result == null) {
      negative += itemDef
      null
    } else {
      cache += itemDef -> result.copy()
      result.copy()
    }
  }
}

object CompacterCache1x1 extends CompacterCache(1)
object CompacterCache2x2 extends CompacterCache(2)
object CompacterCache3x3 extends CompacterCache(3)

object CompacterCacheHollow extends CompacterCache(3) {
    override def getInventory(stack: ItemStack) = new FakeInventoryHollow(stack)
    override val inputAmount = 8
}
