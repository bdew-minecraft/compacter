package net.bdew.compacter.registries

import net.bdew.lib.managers.ItemManager
import net.minecraft.item.{ItemGroup, ItemStack}

object CreativeTab extends ItemGroup("compacter") {
  override def makeIcon(): ItemStack = new ItemStack(Blocks.compacter.item.get())
}

object Items extends ItemManager(CreativeTab) {
}
