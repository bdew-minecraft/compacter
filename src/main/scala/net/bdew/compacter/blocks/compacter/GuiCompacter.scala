package net.bdew.compacter.blocks.compacter

import net.bdew.compacter.Textures
import net.bdew.compacter.misc.WidgetMode
import net.bdew.compacter.network.{MsgSetCraftMode, MsgSetRecurseMode, MsgSetRsMode}
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.WidgetLabel
import net.bdew.lib.power.WidgetPowerGauge
import net.minecraft.world.entity.player.Inventory

class GuiCompacter(container: ContainerCompacter, playerInv: Inventory) extends BaseScreen(container, playerInv, container.te.getDisplayName) {

  override val background: Texture = Textures.compacter

  override def init(): Unit = {
    initGui(176, 222)

    widgets.add(new WidgetLabel(title, 8, 6, Color.darkGray))
    widgets.add(new WidgetLabel(playerInv.getName, 8, rect.h - 94, Color.darkGray))

    widgets.add(new WidgetPowerGauge(Rect(9, 73, 13, 51), Textures.powerFill, container.te.power))

    widgets.add(WidgetMode(Point(116, 72), container.te.recurseMode, MsgSetRecurseMode, "compacter.recurse"))
    widgets.add(WidgetMode(Point(134, 72), container.te.craftMode, MsgSetCraftMode, "compacter.craftmode"))
    widgets.add(WidgetMode(Point(152, 72), container.te.rsMode, MsgSetRsMode, "bdlib.rsmode"))
  }
}
