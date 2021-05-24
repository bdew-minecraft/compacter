package net.bdew.compacter.blocks.cobbler

import net.bdew.lib.capabilities.helpers.ItemHelper
import net.bdew.lib.tile.{TileExtended, TileTicking}
import net.minecraft.block
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntityType
import net.minecraft.util.Direction

class TileCobbler(teType: TileEntityType[_]) extends TileExtended(teType) with TileTicking {
  lazy val cobbleStack = new ItemStack(block.Blocks.COBBLESTONE, 64)
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
