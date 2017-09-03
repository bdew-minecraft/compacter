/*
 * Copyright (c) bdew, 2015 - 2017
 * https://github.com/bdew/compacter
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.compacter.power

import cofh.redstoneflux.api.IEnergyReceiver
import net.bdew.compacter.config.Tuning
import net.bdew.lib.power.TilePoweredBase
import net.minecraft.util.EnumFacing
import net.minecraftforge.fml.common.Optional

@Optional.Interface(modid = PowerProxy.TE_MOD_ID, iface = "cofh.redstoneflux.api.IEnergyReceiver")
trait TilePoweredRF extends TilePoweredBase with IEnergyReceiver {
  override def receiveEnergy(from: EnumFacing, maxReceive: Int, simulate: Boolean) =
    if (PowerProxy.RFEnabled)
      power.inject(maxReceive, simulate).floor.toInt
    else 0

  override def canConnectEnergy(from: EnumFacing) = PowerProxy.RFEnabled
  override def getEnergyStored(from: EnumFacing) = power.stored.floor.toInt
  override def getMaxEnergyStored(from: EnumFacing) = power.capacity.floor.toInt
}
