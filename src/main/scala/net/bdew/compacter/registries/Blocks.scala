package net.bdew.compacter.registries

import net.bdew.compacter.blocks.cobbler.{BlockCobbler, TileCobbler}
import net.bdew.compacter.blocks.compacter.{BlockCompacter, TileCompacter}
import net.bdew.lib.managers.BlockManager
import net.minecraft.world.item.BlockItem
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour.Properties
import net.minecraft.world.level.material.MapColor

object Blocks extends BlockManager(Items) {
  def machineProps: Properties = props
    .mapColor(MapColor.STONE)
    .sound(SoundType.STONE)
    .strength(2, 8)

  val cobbler: Blocks.Def[BlockCobbler, TileCobbler, BlockItem] =
    define("cobbler", () => new BlockCobbler)
      .withTE(new TileCobbler(_, _, _))
      .withDefaultItem
      .register

  val compacter: Blocks.Def[BlockCompacter, TileCompacter, BlockItem] =
    define("compacter", () => new BlockCompacter)
      .withTE(new TileCompacter(_, _, _))
      .withDefaultItem
      .register

}
