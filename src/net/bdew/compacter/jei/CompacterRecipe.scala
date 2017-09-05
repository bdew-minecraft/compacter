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
import net.bdew.compacter.Textures
import net.bdew.compacter.blocks.compacter.{CraftMode, RecurseMode}
import net.bdew.lib.multiblock.data.RSMode
import net.minecraft.item.ItemStack

class CompacterRecipe(val in: ItemStack, val out: ItemStack) extends BaseRecipe {
  CompacterRecipe.modes.get(in.getCount).foreach { texture =>
    addWidget(new TextureWidget(texture, 134 - 5, 72 - 15, 14, 14))
  }

  addWidget(new TextureWidget(Textures.modeTextures(RecurseMode.ENABLED), 116 - 5, 72 - 15, 14, 14))
  addWidget(new TextureWidget(Textures.modeTextures(RSMode.ALWAYS), 152 - 5, 72 - 15, 14, 14))

  addWidget(new PowerWidget(Textures.powerFill, 9 - 6, 73 - 16, 13, 51))

  override def getIngredients(ingredients: IIngredients) = {
    ingredients.setInput(classOf[ItemStack], in)
    ingredients.setOutput(classOf[ItemStack], out)
  }
}

object CompacterRecipe {
  val modes = Map(
    1 -> Textures.modeTextures(CraftMode.ONE_ONLY),
    4 -> Textures.modeTextures(CraftMode.TWO_ONLY),
    8 -> Textures.modeTextures(CraftMode.HOLLOW),
    9 -> Textures.modeTextures(CraftMode.THREE_ONLY)
  )
}

