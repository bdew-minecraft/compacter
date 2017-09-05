/*
 * Copyright (c) bdew, 2015 - 2017
 * https://github.com/bdew/compacter
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.compacter.jei

import net.bdew.lib.gui.{Rect, Texture}

class TextureWidget(texture: Texture, x: Int, y: Int, w: Int, h: Int) extends RecipeWidget(x, y, w, h) {
  override def draw(mx: Int, my: Int): Unit = {
    JeiDrawTarget.drawTexture(Rect(x, y, w, h), texture)
  }
  override def clicked(mx: Int, my: Int): Boolean = false
  override def getTooltip(mx: Int, my: Int): List[String] = List.empty
}
