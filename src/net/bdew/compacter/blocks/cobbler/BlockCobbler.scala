package net.bdew.compacter.blocks.cobbler

import net.bdew.lib.block.{HasTE, SimpleBlock}
import net.minecraft.block.material.Material

object BlockCobbler extends SimpleBlock("Cobbler", Material.iron) with HasTE[TileCobbler] {
  override val TEClass = classOf[TileCobbler]
}
