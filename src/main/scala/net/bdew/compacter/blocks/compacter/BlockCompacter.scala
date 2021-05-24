package net.bdew.compacter.blocks.compacter

import net.bdew.compacter.registries.Blocks
import net.bdew.lib.block.HasTE
import net.bdew.lib.tile.BreakBroadcastingBlock
import net.minecraft.block.{Block, BlockState}
import net.minecraft.entity.player.{PlayerEntity, ServerPlayerEntity}
import net.minecraft.util.math.{BlockPos, BlockRayTraceResult}
import net.minecraft.util.{ActionResultType, Hand}
import net.minecraft.world.World
import net.minecraftforge.fml.network.NetworkHooks

class BlockCompacter extends Block(Blocks.machineProps) with HasTE[TileCompacter] with BreakBroadcastingBlock {
  override def use(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hit: BlockRayTraceResult): ActionResultType = {
    if (world.isClientSide) {
      ActionResultType.SUCCESS
    } else player match {
      case serverPlayer: ServerPlayerEntity =>
        NetworkHooks.openGui(serverPlayer, getTE(world, pos), pos)
        ActionResultType.CONSUME
      case _ => ActionResultType.FAIL
    }
  }

  override def neighborChanged(state: BlockState, world: World, pos: BlockPos, block: Block, fromPos: BlockPos, moving: Boolean): Unit = {
    val te = getTE(world, pos)
    if (!te.haveWork && !world.isClientSide && te.canWorkRS) {
      te.haveWork = true
    }
  }
}
