package net.bdew.compacter.network

import net.bdew.compacter.blocks.compacter.{ContainerCompacter, CraftMode, RecurseMode}
import net.bdew.lib.misc.RSMode
import net.bdew.lib.network.NetChannel

object NetworkHandler extends NetChannel("main", "1") {
  regServerContainerHandler(1, classOf[MsgSetRsMode], classOf[ContainerCompacter]) { (msg, cont, _) =>
    cont.te.rsMode := msg.rsMode
    cont.te.haveWork = true
  }

  regServerContainerHandler(2, classOf[MsgSetCraftMode], classOf[ContainerCompacter]) { (msg, cont, _) =>
    cont.te.craftMode := msg.craftMode
    cont.te.checkRecipes = true
    cont.te.haveWork = true
  }

  regServerContainerHandler(3, classOf[MsgSetRecurseMode], classOf[ContainerCompacter]) { (msg, cont, _) =>
    cont.te.recurseMode := msg.recurseMode
  }
}

case class MsgSetRsMode(rsMode: RSMode.Value) extends NetworkHandler.Message

case class MsgSetCraftMode(craftMode: CraftMode.Value) extends NetworkHandler.Message

case class MsgSetRecurseMode(recurseMode: RecurseMode.Value) extends NetworkHandler.Message