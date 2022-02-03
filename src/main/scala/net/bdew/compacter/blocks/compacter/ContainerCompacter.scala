package net.bdew.compacter.blocks.compacter

import net.bdew.compacter.registries.Containers
import net.bdew.lib.container.{BaseContainer, SlotValidating}
import net.bdew.lib.data.base.ContainerDataSlots
import net.minecraft.world.entity.player.Inventory

class ContainerCompacter(val te: TileCompacter, playerInventory: Inventory, id: Int)
  extends BaseContainer(te.externalInventory, Containers.compacter.get(), id) with ContainerDataSlots {
  override lazy val dataSource: TileCompacter = te

  for (y <- 0 until 3; x <- 0 until 9) {
    this.addSlot(new SlotValidating(
      te.externalInventory,
      te.Slots.input(x + y * 9),
      8 + x * 18, 18 + y * 18
    ))
  }

  for (y <- 0 until 2; x <- 0 until 8) {
    this.addSlot(new SlotValidating(
      te.externalInventory,
      te.Slots.output(x + y * 8),
      26 + x * 18, 90 + y * 18
    ))
  }

  bindPlayerInventory(playerInventory, 8, 140, 198)
}
