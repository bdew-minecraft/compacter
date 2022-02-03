package net.bdew.compacter.blocks.cobbler

import net.bdew.lib.capabilities.helpers.ItemHelper
import net.bdew.lib.tile.{TileExtended, TileTickingServer}
import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class TileCobbler(teType: BlockEntityType[_], pos: BlockPos, state: BlockState) extends TileExtended(teType, pos, state) with TileTickingServer {
  lazy val cobbleStack = new ItemStack(Blocks.COBBLESTONE, 64)
  serverTick.listen(() => {
    for {
      side <- Direction.values()
      handler <- ItemHelper.getItemHandler(level, worldPosition.offset(side.getNormal), side.getOpposite)
      slot <- 0 until handler.getSlots
    } {
      if (handler.getStackInSlot(slot).isEmpty) handler.insertItem(slot, cobbleStack.copy(), false)
    }
  })
}
