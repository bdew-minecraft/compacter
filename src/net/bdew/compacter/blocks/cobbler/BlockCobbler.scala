/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/compacter
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.compacter.blocks.cobbler

import net.bdew.lib.block.{HasTE, SimpleBlock}
import net.minecraft.block.material.Material

object BlockCobbler extends SimpleBlock("Cobbler", Material.iron) with HasTE[TileCobbler] {
  override val TEClass = classOf[TileCobbler]
}
