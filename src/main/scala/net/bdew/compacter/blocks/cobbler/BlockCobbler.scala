package net.bdew.compacter.blocks.cobbler

import net.bdew.compacter.registries.Blocks
import net.bdew.lib.block.HasTETickingServer
import net.minecraft.world.level.block.Block

class BlockCobbler extends Block(Blocks.machineProps) with HasTETickingServer[TileCobbler]