/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/compacter
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.compacter.nei

import java.awt.Rectangle
import java.util

import codechicken.nei.recipe.TemplateRecipeHandler
import codechicken.nei.recipe.TemplateRecipeHandler.RecipeTransferRect
import codechicken.nei.{NEIServerUtils, PositionedStack}
import net.bdew.compacter.misc._
import net.minecraft.item.ItemStack

import scala.collection.mutable

class CompacterRecipeHandler extends TemplateRecipeHandler {

  class CompacterRecipe(in: ItemStack, out: ItemStack, cache: CompacterCache) extends CachedRecipe {
    val result = new PositionedStack(out, 119, 24)
    val inputs =
      for (x <- 0 until cache.size; y <- 0 until cache.size if cache.recipeHasItemAt(x, y)) yield
      new PositionedStack(in, 25 + x * 18, 6 + y * 18)

    import scala.collection.JavaConversions._

    override def getIngredients: util.List[PositionedStack] = inputs
    override def getResult: PositionedStack = result
  }

  val allCaches = List(CompacterCache1x1, CompacterCache2x2, CompacterCache3x3, CompacterCacheHollow)

  override def loadTransferRects(): Unit = {
    transferRects.add(new RecipeTransferRect(new Rectangle(84, 23, 24, 18), "Compacter"))
  }

  def addAllRecipes(): Unit = {
    for (cache <- allCaches; (input, output) <- cache.custom)
      arecipes.add(new CompacterRecipe(input.stack(), output, cache))
  }

  def findStack(hay: mutable.Map[ItemDef, ItemStack], needle: ItemStack) =
    hay.find(x => NEIServerUtils.areStacksSameTypeCrafting(needle, x._2))

  override def loadCraftingRecipes(outputId: String, results: AnyRef*): Unit = (outputId, results) match {
    case ("item", Seq(x: ItemStack)) => loadCraftingRecipes(x)
    case ("Compacter", _) => addAllRecipes()
    case _ =>
  }

  override def loadCraftingRecipes(result: ItemStack): Unit = {
    for (cache <- allCaches; (input, output) <- findStack(cache.custom, result))
      arecipes.add(new CompacterRecipe(input.stack(), output, cache))
  }

  override def loadUsageRecipes(inputId: String, ingredients: AnyRef*): Unit = (inputId, ingredients) match {
    case ("item", Seq(x: ItemStack)) => loadUsageRecipes(x)
    case ("Compacter", _) => addAllRecipes()
    case _ =>
  }

  override def loadUsageRecipes(ingredient: ItemStack): Unit = {
    val idef = ItemDef(ingredient)
    for (cache <- allCaches; result <- cache.custom.get(idef))
      arecipes.add(new CompacterRecipe(idef.stack(1), result, cache))
  }

  override def getGuiTexture = "textures/gui/container/crafting_table.png"
  override def getRecipeName = "Compacter Recipe"
}
