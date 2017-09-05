/*
 * Copyright (c) bdew, 2015 - 2017
 * https://github.com/bdew/compacter
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.compacter.jei

import net.bdew.lib.Client
import net.bdew.lib.gui.SimpleDrawTarget

object JeiDrawTarget extends SimpleDrawTarget {
  override def getZLevel = 100
  override def getFontRenderer = Client.fontRenderer
}