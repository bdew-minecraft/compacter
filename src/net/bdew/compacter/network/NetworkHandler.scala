package net.bdew.compacter.network

import net.bdew.compacter.CompacterMod
import net.bdew.compacter.blocks.{ContainerCompacter, CraftMode, RecurseMode}
import net.bdew.lib.multiblock.data.RSMode
import net.bdew.lib.network.NetChannel

object NetworkHandler extends NetChannel(CompacterMod.channel) {
  regServerHandler {
    case (MsgSetRsMode(rsMode), player) =>
      if (player.openContainer.isInstanceOf[ContainerCompacter])
        player.openContainer.asInstanceOf[ContainerCompacter].te.rsMode := rsMode
    case (MsgSetCraftMode(craftMode), player) =>
      if (player.openContainer.isInstanceOf[ContainerCompacter]) {
        val te = player.openContainer.asInstanceOf[ContainerCompacter].te
        te.craftMode := craftMode
        te.checkRecipes = true
      }
    case (MsgSetRecurseMode(recurseMode), player) =>
      if (player.openContainer.isInstanceOf[ContainerCompacter])
        player.openContainer.asInstanceOf[ContainerCompacter].te.recurseMode := recurseMode
  }
}

case class MsgSetRsMode(rsMode: RSMode.Value) extends NetworkHandler.Message

case class MsgSetCraftMode(craftMode: CraftMode.Value) extends NetworkHandler.Message

case class MsgSetRecurseMode(recurseMode: RecurseMode.Value) extends NetworkHandler.Message