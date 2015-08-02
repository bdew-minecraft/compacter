/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/compacter
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.compacter.config

import net.bdew.compacter.blocks.cobbler.BlockCobbler
import net.bdew.lib.config.BlockManager
import net.minecraft.creativetab.CreativeTabs

object Blocks extends BlockManager(CreativeTabs.tabMisc) {
  regBlock(BlockCobbler)
}
