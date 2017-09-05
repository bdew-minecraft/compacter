/*
 * Copyright (c) bdew, 2015 - 2017
 * https://github.com/bdew/compacter
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.compacter.jei

abstract class RecipeWidget(val x: Int, val y: Int, val w: Int, val h: Int) {
  def draw(mx: Int, my: Int): Unit
  def clicked(mx: Int, my: Int): Boolean
  def getTooltip(mx: Int, my: Int): List[String]
}
