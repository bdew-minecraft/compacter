package net.bdew.compacter.blocks

import net.bdew.lib.data.base.ContainerDataSlots
import net.bdew.lib.gui.{BaseContainer, SlotValidating}
import net.minecraft.entity.player.EntityPlayer

class ContainerCompacter(val te: TileCompacter, player: EntityPlayer) extends BaseContainer(te) with ContainerDataSlots {
  override lazy val dataSource = te

  for (y <- 0 until 3; x <- 0 until 9)
    this.addSlotToContainer(new SlotValidating(te, te.Slots.input(x + y * 9), 8 + x * 18, 18 + y * 18))

  for (y <- 0 until 2; x <- 0 until 8)
    this.addSlotToContainer(new SlotValidating(te, te.Slots.output(x + y * 8), 26 + x * 18, 90 + y * 18))

  bindPlayerInventory(player.inventory, 8, 140, 198)
}
