/*
 * Copyright (c) bdew, 2015 - 2017
 * https://github.com/bdew/compacter
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.compacter.jei

import mezz.jei.api.recipe.IRecipeCategoryRegistration
import mezz.jei.api.{IModPlugin, IModRegistry}
import net.bdew.compacter.misc._

class CompacterJEIPlugin extends IModPlugin {
  override def registerCategories(registry: IRecipeCategoryRegistration) = {
    registry.addRecipeCategories(new CompacterRecipeCategory(registry.getJeiHelpers.getGuiHelper))
  }

  def convertRecipes(mode: CompacterCache) = {
    mode.custom.map {
      case (in, out) => new CompacterRecipe(in.stack(mode.inputAmount), out)
    }
  }

  override def register(registry: IModRegistry) = {
    import scala.collection.JavaConversions._

    val recipes = convertRecipes(CompacterCache1x1) ++
      convertRecipes(CompacterCache2x2) ++
      convertRecipes(CompacterCache3x3) ++
      convertRecipes(CompacterCacheHollow)

    registry.addRecipes(recipes, "bdew.compacter")
  }
}
