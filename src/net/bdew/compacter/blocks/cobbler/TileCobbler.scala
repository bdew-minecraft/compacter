/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/compacter
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.compacter.blocks.cobbler

import net.bdew.lib.block.BlockRef
import net.bdew.lib.tile.TileExtended
import net.minecraft.init.Blocks
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack

class TileCobbler extends TileExtended {
  lazy val cobbleStack = new ItemStack(Blocks.cobblestone, 64)
  serverTick.listen(() => {
    for {
      (side, pos) <- BlockRef.fromTile(this).neighbours
      inv <- pos.getTile[IInventory](worldObj)
      slot <- 0 until inv.getSizeInventory
    } {
      if (inv.getStackInSlot(slot) == null && inv.isItemValidForSlot(slot, cobbleStack))
        inv.setInventorySlotContents(slot, cobbleStack.copy())
    }
  })
}
