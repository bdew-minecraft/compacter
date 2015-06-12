package net.bdew.compacter.blocks

import net.bdew.compacter.CompacterMod
import net.bdew.lib.block.{HasTE, SimpleBlock}
import net.bdew.lib.tile.inventory.BreakableInventoryBlock
import net.minecraft.block.material.Material
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World

object BlockCompacter extends SimpleBlock("Compacter", Material.iron) with HasTE[TileCompacter] with BreakableInventoryBlock {
  override val TEClass = classOf[TileCompacter]
  override def onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, xOffset: Float, yOffset: Float, zOffset: Float): Boolean = {
    if (!world.isRemote) {
      player.openGui(CompacterMod, MachineCompacter.guiId, world, x, y, z)
    }
    true
  }
}
