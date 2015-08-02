/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/compacter
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.compacter.blocks

import net.bdew.compacter.Textures
import net.bdew.compacter.misc.{WidgetMode, WidgetPowerGaugeCustom}
import net.bdew.compacter.network.{MsgSetCraftMode, MsgSetRecurseMode, MsgSetRsMode}
import net.bdew.lib.Misc
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.WidgetLabel
import net.minecraft.entity.player.EntityPlayer

class GuiCompacter(val te: TileCompacter, player: EntityPlayer) extends BaseScreen(new ContainerCompacter(te, player), 176, 222) {
  val background = Texture("compacter", "textures/gui/compacter.png", rect)

  override def initGui(): Unit = {
    super.initGui()

    widgets.add(new WidgetLabel(BlockCompacter.getLocalizedName, 8, 6, Color.darkGray))
    widgets.add(new WidgetLabel(Misc.toLocal("container.inventory"), 8, this.ySize - 96 + 3, Color.darkGray))

    widgets.add(new WidgetPowerGaugeCustom(Rect(9, 73, 13, 51), Textures.powerFill, te.power))

    widgets.add(WidgetMode(Point(116, 72), te.recurseMode, MsgSetRecurseMode, "compacter.recurse"))
    widgets.add(WidgetMode(Point(134, 72), te.craftMode, MsgSetCraftMode, "compacter.craftmode"))
    widgets.add(WidgetMode(Point(152, 72), te.rsMode, MsgSetRsMode, "bdlib.rsmode"))
  }
}
