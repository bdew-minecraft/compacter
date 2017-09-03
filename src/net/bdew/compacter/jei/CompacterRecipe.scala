/*
 * Copyright (c) bdew, 2015 - 2017
 * https://github.com/bdew/compacter
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.compacter.jei

import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.IRecipeWrapper
import net.minecraft.item.ItemStack

class CompacterRecipe(val in: ItemStack, val out: ItemStack) extends IRecipeWrapper {
  override def getIngredients(ingredients: IIngredients) = {
    ingredients.setInput(classOf[ItemStack], in)
    ingredients.setOutput(classOf[ItemStack], out)
  }
}
