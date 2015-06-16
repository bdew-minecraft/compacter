package net.bdew.compacter.config

import net.bdew.compacter.blocks.cobbler.BlockCobbler
import net.bdew.lib.config.BlockManager
import net.minecraft.creativetab.CreativeTabs

object Blocks extends BlockManager(CreativeTabs.tabMisc) {
  regBlock(BlockCobbler)
}
