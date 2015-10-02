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

case class ItemDef(item: Item, damage: Int) {
  def stack(n: Int = 1) = new ItemStack(item, n, damage)
}

object ItemDef {
  def apply(stack: ItemStack): ItemDef = ItemDef(stack.getItem, stack.getItemDamage)
}

object FakeContainer extends Container {
  override def canInteractWith(player: EntityPlayer) = true
}

class FakeInventory(cache: CompacterCache, stack: ItemStack) extends InventoryCrafting(FakeContainer, cache.size, cache.size) {
  override def getSizeInventory = cache.size * cache.size
  override def markDirty(): Unit = {}
  override def getStackInRowAndColumn(x: Int, y: Int): ItemStack =
    if (cache.recipeHasItemAt(x, y)) stack else null
  override def getStackInSlot(slot: Int): ItemStack = {
    val x = slot % cache.size
    val y = slot / cache.size
    getStackInRowAndColumn(x, y)
  }
}

class CompacterCache(val size: Int) {
  val custom = collection.mutable.Map.empty[ItemDef, ItemStack]
  val negative = collection.mutable.Set.empty[ItemDef]
  val cache = collection.mutable.Map.empty[ItemDef, ItemStack]

  val inputAmount = size * size

  def hasRecipe(stack: ItemStack, world: World) =
    (!stack.hasTagCompound) && (getRecipe(stack, world) != null)

  def getRecipe(stack: ItemStack, world: World): ItemStack =
    getRecipe(ItemDef(stack), world)

  def recipeHasItemAt(x: Int, y: Int) = x < size && y < size

  def getRecipe(itemDef: ItemDef, world: World): ItemStack = {
    if (negative.contains(itemDef)) return null
    if (custom.contains(itemDef)) return custom(itemDef).copy()
    if (cache.contains(itemDef)) return cache(itemDef).copy()

    val fakeInventory = new FakeInventory(this, new ItemStack(itemDef.item, 1, itemDef.damage))
    val result = CraftingManager.getInstance.findMatchingRecipe(fakeInventory, world)
    if (result == null) {
      negative += itemDef
      null
    } else {
      cache += itemDef -> result.copy()
      result.copy()
    }
  }

  def addCustom(in: ItemStack, out: ItemStack): Unit = {
    custom += ItemDef(in) -> out
  }
}

object CompacterCache1x1 extends CompacterCache(1)

object CompacterCache2x2 extends CompacterCache(2)

object CompacterCache3x3 extends CompacterCache(3)

object CompacterCacheHollow extends CompacterCache(3) {
  override val inputAmount = 8
  override def recipeHasItemAt(x: Int, y: Int): Boolean = x < 3 && y < 3 && !(x == 1 && y == 1)
}
