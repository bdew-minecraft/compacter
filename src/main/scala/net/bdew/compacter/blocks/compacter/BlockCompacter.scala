package net.bdew.compacter.blocks.compacter

import net.bdew.compacter.registries.Blocks
import net.bdew.lib.block.HasTETickingServer
import net.bdew.lib.tile.BreakBroadcastingBlock
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.{InteractionHand, InteractionResult}
import net.minecraftforge.network.NetworkHooks

class BlockCompacter extends Block(Blocks.machineProps) with HasTETickingServer[TileCompacter] with BreakBroadcastingBlock {
  override def use(state: BlockState, world: Level, pos: BlockPos, player: Player, hand: InteractionHand, hit: BlockHitResult): InteractionResult = {
    if (world.isClientSide) {
      InteractionResult.SUCCESS
    } else player match {
      case serverPlayer: ServerPlayer =>
        NetworkHooks.openScreen(serverPlayer, getTE(world, pos), pos)
        InteractionResult.CONSUME
      case _ => InteractionResult.FAIL
    }
  }

  override def neighborChanged(state: BlockState, world: Level, pos: BlockPos, block: Block, fromPos: BlockPos, moving: Boolean): Unit = {
    val te = getTE(world, pos)
    if (!te.haveWork && !world.isClientSide && te.canWorkRS) {
      te.haveWork = true
    }
  }
}
