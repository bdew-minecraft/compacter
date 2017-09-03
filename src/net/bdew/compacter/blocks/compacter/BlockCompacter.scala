/*
 * Copyright (c) bdew, 2015 - 2017
 * https://github.com/bdew/compacter
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.compacter.blocks.compacter

import net.bdew.compacter.CompacterMod
import net.bdew.compacter.blocks.MachineMaterial
import net.bdew.lib.block.{BaseBlock, HasTE}
import net.bdew.lib.tile.inventory.BreakableInventoryBlock
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.{EnumFacing, EnumHand}
import net.minecraft.util.math.BlockPos
import net.minecraft.world.{IBlockAccess, World}

object BlockCompacter extends BaseBlock("compacter", MachineMaterial) with HasTE[TileCompacter] with BreakableInventoryBlock {

  setHardness(0.5F)

  override val TEClass = classOf[TileCompacter]


  override def onBlockActivated(world: World, pos: BlockPos, state: IBlockState, player: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float) = {
    if (!world.isRemote) {
      player.openGui(CompacterMod, MachineCompacter.guiId, world, pos.getX, pos.getY, pos.getZ)
    }
    true
  }

  override def neighborChanged(state: IBlockState, world: World, pos: BlockPos, block: Block, fromPos: BlockPos): Unit = {
    val te = getTE(world,pos)
    if (!te.haveWork && !world.isRemote && te.canWorkRS) {
      te.haveWork = true
    }
  }
}
