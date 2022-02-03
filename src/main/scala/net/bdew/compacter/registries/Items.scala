package net.bdew.compacter.registries

import net.bdew.lib.managers.ItemManager
import net.minecraft.world.item.{CreativeModeTab, ItemStack}

object CreativeTab extends CreativeModeTab("compacter") {
  override def makeIcon(): ItemStack = new ItemStack(Blocks.compacter.item.get())
}

object Items extends ItemManager(CreativeTab) {
}
