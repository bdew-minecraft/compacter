package net.bdew.compacter.misc

import com.mojang.blaze3d.vertex.PoseStack
import net.bdew.compacter.Textures
import net.bdew.compacter.network.NetworkHandler
import net.bdew.lib.data.DataSlotEnum
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.Widget
import net.bdew.lib.{Client, Misc, Text}
import net.minecraft.client.resources.sounds.SimpleSoundInstance
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvents

import java.util.Locale
import scala.collection.mutable

case class WidgetMode[T <: Enumeration](p: Point, ds: DataSlotEnum[T], pktConstructor: T#Value => NetworkHandler.Message, locPfx: String) extends Widget {
  val rect = new Rect(p, 16, 16)
  val iconRect = new Rect(p + (1, 1), 14, 14)

  val values: Seq[T#Value] = ds.baseEnum.values.toList.sortBy(_.id)

  override def drawBackground(m: PoseStack, mouse: Point): Unit = {
    if (rect.contains(mouse))
      parent.drawTexture(m, rect, Textures.buttonHover)
    else
      parent.drawTexture(m, rect, Textures.buttonBase)
  }

  override def draw(m: PoseStack, mouse: Point, partial: Float): Unit = {
    parent.drawTexture(m, iconRect, Textures.modeTextures(ds.value))
  }

  override def handleTooltip(p: Point, tip: mutable.ArrayBuffer[Component]): Unit = {
    tip += Text.translate(locPfx + "." + ds.value.toString.toLowerCase(Locale.US))
  }

  override def mouseClicked(p: Point, button: Int): Boolean = {
    Client.soundManager.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F))
    if (button == 0)
      ds := Misc.nextInSeq(values, ds.value)
    else if (button == 1)
      ds := Misc.prevInSeq(values, ds.value)
    NetworkHandler.sendToServer(pktConstructor(ds.value))
    true
  }
}
