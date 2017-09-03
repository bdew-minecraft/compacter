/*
 * Copyright (c) bdew, 2015 - 2017
 * https://github.com/bdew/compacter
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.compacter.power

import net.bdew.lib.capabilities.CapabilityProvider
import net.bdew.lib.power.TilePoweredBase

trait TilePoweredTesla extends TilePoweredBase with CapabilityProvider {
  if (PowerProxy.haveTesla && PowerProxy.TeslaEnabled) {
    Tesla.injectTesla(this)
  }
}
