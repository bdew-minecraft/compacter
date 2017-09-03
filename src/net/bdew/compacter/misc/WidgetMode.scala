/*
 * Copyright (c) bdew, 2015 - 2017
 * https://github.com/bdew/compacter
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.compacter.misc

import java.util.Locale

import net.bdew.compacter.Textures
import net.bdew.compacter.network.NetworkHandler
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.Widget
import net.bdew.lib.{Client, Misc}
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.init.SoundEvents

import scala.collection.mutable

case class WidgetMode[T <: Enumeration](p: Point, ds: DataSlotEnum[T], pktConstructor: T#Value => NetworkHandler.Message, locPfx: String) extends Widget {
  val rect = new Rect(p, 16, 16)
  val iconRect = new Rect(p + (1, 1), 14, 14)

  val values = ds.enum.values.toList.sortBy(_.id)

  override def draw(mouse: Point, partial: Float) {
    if (rect.contains(mouse))
      parent.drawTexture(rect, Textures.buttonHover)
    else
      parent.drawTexture(rect, Textures.buttonBase)

    parent.drawTexture(iconRect, Textures.modeTextures(ds.value))
  }

  override def handleTooltip(p: Point, tip: mutable.MutableList[String]) {
    tip += Misc.toLocal(locPfx + "." + ds.value.toString.toLowerCase(Locale.US))
  }

  override def mouseClicked(p: Point, button: Int) {
    Client.minecraft.getSoundHandler.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F))
    if (button == 0)
      ds := Misc.nextInSeq(values, ds.value)
    else if (button == 1)
      ds := Misc.prevInSeq(values, ds.value)
    NetworkHandler.sendToServer(pktConstructor(ds.value))
  }
}
