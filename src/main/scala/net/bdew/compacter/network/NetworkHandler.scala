package net.bdew.compacter.network

import net.bdew.compacter.blocks.compacter.ContainerCompacter
import net.bdew.lib.network.NetChannel

object NetworkHandler extends NetChannel("main", "2") {
  regServerContainerHandler(1, CodecSetRsMode, classOf[ContainerCompacter]) { (msg, cont, _) =>
    cont.te.rsMode := msg.rsMode
    cont.te.haveWork = true
  }

  regServerContainerHandler(2, CodecSetCraftMode, classOf[ContainerCompacter]) { (msg, cont, _) =>
    cont.te.craftMode := msg.craftMode
    cont.te.checkRecipes = true
    cont.te.haveWork = true
  }

  regServerContainerHandler(3, CodecSetRecurseMode, classOf[ContainerCompacter]) { (msg, cont, _) =>
    cont.te.recurseMode := msg.recurseMode
  }
}





