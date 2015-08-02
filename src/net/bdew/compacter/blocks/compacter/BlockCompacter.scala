/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/compacter
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.compacter.blocks.compacter

import net.bdew.compacter.CompacterMod
import net.bdew.lib.block.{HasTE, SimpleBlock}
import net.bdew.lib.tile.inventory.BreakableInventoryBlock
import net.minecraft.block.Block
import net.minecraft.block.material.{MapColor, Material}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World



object BlockCompacter extends SimpleBlock("Compacter", new Material(MapColor.ironColor)) with HasTE[TileCompacter] with BreakableInventoryBlock {

  setHardness(0.5F)

  override val TEClass = classOf[TileCompacter]
  override def onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, xOffset: Float, yOffset: Float, zOffset: Float): Boolean = {
    if (!world.isRemote) {
      player.openGui(CompacterMod, MachineCompacter.guiId, world, x, y, z)
    }
    true
  }

  override def onNeighborBlockChange(world: World, x: Int, y: Int, z: Int, block: Block): Unit = {
    val te = getTE(world, x, y, z)
    if (!te.haveWork && !world.isRemote && te.canWorkRS) {
      te.haveWork = true
    }
  }
}
