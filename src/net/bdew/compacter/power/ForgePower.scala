/*
 * Copyright (c) bdew, 2015 - 2017
 * https://github.com/bdew/compacter
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.compacter.power

import net.bdew.compacter.config.Tuning
import net.bdew.lib.Misc
import net.bdew.lib.capabilities.{CapabilityProvider, CapabilityProviderItem}
import net.bdew.lib.power.{ItemPoweredBase, TilePoweredBase}
import net.minecraft.item.ItemStack
import net.minecraftforge.common.capabilities.{Capability, CapabilityInject}
import net.minecraftforge.energy.{CapabilityEnergy, IEnergyStorage}

import scala.annotation.meta.setter

object ForgePower {
  @(CapabilityInject@setter)(classOf[IEnergyStorage])
  var CAP: Capability[IEnergyStorage] = null

  lazy val ratio = Tuning.getSection("Power").getFloat("FE_RF_Ratio")
}

class EnergyHandlerTile(tile: TilePoweredBase) extends IEnergyStorage {
  override def getEnergyStored: Int = (tile.power.stored * ForgePower.ratio).toInt
  override def getMaxEnergyStored: Int = (tile.power.capacity * ForgePower.ratio).toInt

  override def canReceive: Boolean = true
  override def receiveEnergy(maxReceive: Int, simulate: Boolean): Int =
    (tile.power.inject(maxReceive / ForgePower.ratio, simulate) * ForgePower.ratio).floor.toInt

  override def canExtract: Boolean = false
  override def extractEnergy(maxExtract: Int, simulate: Boolean): Int = 0
}

class EnergyHandlerItem(item: ItemPoweredBase, stack: ItemStack) extends IEnergyStorage {
  override def getEnergyStored: Int = (item.getCharge(stack) * ForgePower.ratio).toInt
  override def getMaxEnergyStored: Int = (item.maxCharge * ForgePower.ratio).toInt

  override def canReceive: Boolean = true
  override def receiveEnergy(maxReceive: Int, simulate: Boolean): Int = {
    val charge = item.getCharge(stack)
    val canCharge = Misc.clamp(item.maxCharge.toFloat - charge, 0F, maxReceive.toFloat / ForgePower.ratio).floor.toInt
    if (!simulate) item.setCharge(stack, charge + canCharge)
    (canCharge * ForgePower.ratio).floor.toInt
  }

  override def canExtract: Boolean = false
  override def extractEnergy(maxExtract: Int, simulate: Boolean): Int = 0
}

trait TilePoweredForge extends TilePoweredBase with CapabilityProvider {
  if (PowerProxy.ForgeEnabled)
    addCapability(CapabilityEnergy.ENERGY, new EnergyHandlerTile(this))
}

trait ItemPoweredForge extends ItemPoweredBase with CapabilityProviderItem {
  if (PowerProxy.ForgeEnabled)
    addCapability(CapabilityEnergy.ENERGY, new EnergyHandlerItem(this, _))
}