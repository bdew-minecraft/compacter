package net.bdew.compacter.registries

import net.bdew.compacter.blocks.compacter.{ContainerCompacter, GuiCompacter}
import net.bdew.lib.managers.ContainerManager
import net.minecraft.world.inventory.MenuType
import net.minecraftforge.api.distmarker.{Dist, OnlyIn}
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.registries.RegistryObject

object Containers extends ContainerManager {
  val compacter: RegistryObject[MenuType[ContainerCompacter]] =
    registerPositional("compacter", Blocks.compacter.teType) {
      (id, inv, te) => new ContainerCompacter(te, inv, id)
    }

  @OnlyIn(Dist.CLIENT)
  override def onClientSetup(ev: FMLClientSetupEvent): Unit = {
    registerScreen(compacter) { (c, i, _) => new GuiCompacter(c, i) }
  }
}
