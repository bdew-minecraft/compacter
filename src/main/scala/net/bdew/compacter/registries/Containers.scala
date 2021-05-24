package net.bdew.compacter.registries

import net.bdew.compacter.blocks.compacter.{ContainerCompacter, GuiCompacter}
import net.bdew.lib.managers.ContainerManager
import net.minecraft.inventory.container.ContainerType
import net.minecraftforge.api.distmarker.{Dist, OnlyIn}
import net.minecraftforge.fml.RegistryObject
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent

object Containers extends ContainerManager {
  val compacter: RegistryObject[ContainerType[ContainerCompacter]] =
    registerPositional("compacter", Blocks.compacter.teType) {
      (id, inv, te) => new ContainerCompacter(te, inv, id)
    }

  @OnlyIn(Dist.CLIENT)
  override def onClientSetup(ev: FMLClientSetupEvent): Unit = {
    registerScreen(compacter) { (c, i, _) => new GuiCompacter(c, i) }
  }
}
