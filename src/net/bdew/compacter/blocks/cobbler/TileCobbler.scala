/*
 * Copyright (c) bdew, 2015 - 2017
 * https://github.com/bdew/compacter
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.compacter.blocks.cobbler

import net.bdew.lib.capabilities.helpers.ItemHelper
import net.bdew.lib.tile.TileTicking
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing

class TileCobbler extends TileTicking {
  lazy val cobbleStack = new ItemStack(Blocks.COBBLESTONE, 64)
  serverTick.listen(() => {
    for {
      side <- EnumFacing.VALUES
      handler <- ItemHelper.getItemHandler(world, pos.offset(side), side.getOpposite)
      slot <- 0 until handler.getSlots
    } {
      if (handler.getStackInSlot(slot).isEmpty) handler.insertItem(slot, cobbleStack.copy(), false)
    }
  })
}
