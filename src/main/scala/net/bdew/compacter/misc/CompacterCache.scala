package net.bdew.compacter.misc

import net.bdew.compacter.Compacter
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.{AbstractContainerMenu, CraftingContainer}
import net.minecraft.world.item.{Item, ItemStack}
import net.minecraft.world.item.crafting.{CraftingRecipe, RecipeType}
import net.minecraft.world.level.Level

case class ItemDef(item: Item) {
  def stack(n: Int = 1): ItemStack = {
    val s = new ItemStack(item)
    if (n > 1)
      s.setCount(n)
    s
  }
}

object ItemDef {
  def apply(stack: ItemStack): ItemDef = ItemDef(stack.getItem)
}

object FakeContainer extends AbstractContainerMenu(null, 0) {
  override def stillValid(player: Player): Boolean = true
}

class FakeInventory(cache: CompacterCache, stack: ItemStack) extends CraftingContainer(FakeContainer, cache.size, cache.size) {
  override def getContainerSize: Int = cache.size * cache.size

  override def setChanged(): Unit = {
  }

  override def getItem(slot: Int): ItemStack = {
    val x = slot % cache.size
    val y = slot / cache.size
    if (cache.recipeHasItemAt(x, y)) stack else ItemStack.EMPTY
  }
}

class CompacterCache(val size: Int) {
  val negative = collection.mutable.Set.empty[ItemDef]
  val cache = collection.mutable.Map.empty[ItemDef, ItemStack]

  val inputAmount: Int = size * size

  def hasRecipe(stack: ItemStack, world: Level): Boolean =
    (!stack.hasTag) && !getRecipe(stack, world).isEmpty

  def getRecipe(stack: ItemStack, world: Level): ItemStack =
    getRecipe(ItemDef(stack), world)

  def recipeHasItemAt(x: Int, y: Int): Boolean = x < size && y < size

  def getRecipe(itemDef: ItemDef, world: Level): ItemStack = {
    if (negative.contains(itemDef)) return ItemStack.EMPTY
    if (cache.contains(itemDef)) return cache(itemDef).copy()

    val fakeInventory = new FakeInventory(this, itemDef.stack())

    val result = world.getRecipeManager
      .getRecipeFor[CraftingContainer, CraftingRecipe](RecipeType.CRAFTING, fakeInventory, world)
      .map(recipe => recipe.assemble(fakeInventory))
      .filter(!_.isEmpty)

    if (!result.isPresent) {
      negative += itemDef
      ItemStack.EMPTY
    } else {
      cache += itemDef -> result.get().copy()
      result.get().copy()
    }
  }

  def invalidate(): Unit = {
    negative.clear()
    cache.clear()
  }
}

object CompacterCaches {
  val cache1x1: CompacterCache = new CompacterCache(1)

  val cache2x2: CompacterCache = new CompacterCache(2)

  val cache3x3: CompacterCache = new CompacterCache(3)

  val cacheHollow: CompacterCache = new CompacterCache(3) {
    override val inputAmount = 8
    override def recipeHasItemAt(x: Int, y: Int): Boolean = x < 3 && y < 3 && !(x == 1 && y == 1)
  }

  def invalidate(): Unit = {
    Compacter.logInfo("Invalidating compacter recipe caches")
    cache1x1.invalidate()
    cache2x2.invalidate()
    cache3x3.invalidate()
    cacheHollow.invalidate()
  }
}