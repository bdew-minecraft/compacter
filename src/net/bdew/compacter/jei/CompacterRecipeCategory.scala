/*
 * Copyright (c) bdew, 2015 - 2017
 * https://github.com/bdew/compacter
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.compacter.jei

import mezz.jei.api.IGuiHelper
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.IRecipeCategory
import net.bdew.lib.Misc
import net.minecraft.util.ResourceLocation

class CompacterRecipeCategory(guiHelper: IGuiHelper) extends IRecipeCategory[CompacterRecipe] {
  val background = guiHelper.createDrawable(new ResourceLocation("compacter", "textures/gui/compacter.png"), 0, 0, 176, 222)

  override def getUid = "bdew.compacter"
  override def getModName = "Compacter"
  override def getTitle = Misc.toLocal("tile.compacter.compacter.name")
  override def getBackground = background

  override def setRecipe(recipeLayout: IRecipeLayout, recipeWrapper: CompacterRecipe, ingredients: IIngredients) = {
    val stacks = recipeLayout.getItemStacks
    stacks.init(0, true, 8, 18)
    stacks.init(1, false, 26, 90)
    stacks.set(0, recipeWrapper.in)
    stacks.set(1, recipeWrapper.out)
  }
}
