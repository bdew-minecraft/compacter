package net.bdew.compacter.registries

import net.bdew.compacter.blocks.cobbler.{BlockCobbler, TileCobbler}
import net.bdew.compacter.blocks.compacter.{BlockCompacter, TileCompacter}
import net.bdew.lib.managers.BlockManager
import net.minecraft.block.AbstractBlock.Properties
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.item.BlockItem

object Blocks extends BlockManager(Items) {
  def machineProps: Properties = props(Material.STONE)
    .sound(SoundType.STONE)
    .strength(2, 8)

  val cobbler: Blocks.Def[BlockCobbler, TileCobbler, BlockItem] =
    define("cobbler", () => new BlockCobbler)
      .withTE(new TileCobbler(_))
      .withDefaultItem
      .register

  val compacter: Blocks.Def[BlockCompacter, TileCompacter, BlockItem] =
    define("compacter", () => new BlockCompacter)
      .withTE(new TileCompacter(_))
      .withDefaultItem
      .register

}
