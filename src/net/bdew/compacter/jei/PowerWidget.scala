/*
 * Copyright (c) bdew, 2015 - 2017
 * https://github.com/bdew/compacter
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.compacter.jei

import net.bdew.compacter.blocks.compacter.MachineCompacter
import net.bdew.compacter.config.Config
import net.bdew.lib.DecFormat
import net.bdew.lib.gui.{Rect, Texture}

class PowerWidget(texture: Texture, x: Int, y: Int, w: Int, h: Int) extends RecipeWidget(x, y, w, h) {
  override def draw(mx: Int, my: Int): Unit = {
    JeiDrawTarget.drawTextureInterpolate(Rect(x, y, w, h), texture, 0, 0.8f, 1, 1)
  }

  override def getTooltip(mx: Int, my: Int): List[String] =
    List("%s/%s %s".format(DecFormat.round(MachineCompacter.activationEnergy * Config.powerShowMultiplier), DecFormat.round(MachineCompacter.maxStoredEnergy * Config.powerShowMultiplier), Config.powerShowUnits))

  override def clicked(mx: Int, my: Int): Boolean = false
}
