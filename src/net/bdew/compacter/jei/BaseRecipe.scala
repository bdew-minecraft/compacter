/*
 * Copyright (c) bdew, 2015 - 2017
 * https://github.com/bdew/compacter
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.compacter.jei

import java.util

import mezz.jei.api.recipe.IRecipeWrapper
import net.minecraft.client.Minecraft

import scala.collection.JavaConversions._

abstract class BaseRecipe extends IRecipeWrapper {
  private var widgets = List.empty[RecipeWidget]

  def addWidget(widget: RecipeWidget) = widgets :+= widget
  def addWidgets(toAdd: Iterable[RecipeWidget]) = widgets ++= toAdd

  private def inRect(widget: RecipeWidget, mouseX: Int, mouseY: Int) =
    mouseX >= widget.x && mouseX <= widget.x + widget.w && mouseY >= widget.y && mouseY <= widget.y + widget.h

  override def handleClick(minecraft: Minecraft, mouseX: Int, mouseY: Int, mouseButton: Int): Boolean = {
    widgets.exists(w => inRect(w, mouseX, mouseY) && w.clicked(mouseX - w.x, mouseY - w.y))
  }

  override def getTooltipStrings(mouseX: Int, mouseY: Int): util.List[String] = {
    widgets.filter(inRect(_, mouseX, mouseY)).flatMap(w => w.getTooltip(mouseX - w.x, mouseY - w.y))
  }

  override def drawInfo(minecraft: Minecraft, recipeWidth: Int, recipeHeight: Int, mouseX: Int, mouseY: Int): Unit = {
    widgets.foreach(w => w.draw(mouseX - w.x, mouseY - w.y))
  }
}
