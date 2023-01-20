package net.bdew.compacter.registries

import net.bdew.compacter.Compacter
import net.bdew.lib.managers.ItemManager
import net.minecraft.network.chat.Component

object Items extends ItemManager {
  creativeTabs.registerTab(
    "main",
    Component.translatable("itemGroup." + Compacter.ModId),
    Blocks.compacter.item,
    all
  )
}
