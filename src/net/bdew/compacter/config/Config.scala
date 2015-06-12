/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.compacter.config

import java.io.File

import net.bdew.compacter.CompacterMod
import net.minecraftforge.common.config.Configuration

object Config {
  var powerShowUnits = "RF"
  var powerShowMultiplier = 1F

  def load() {
    val c = new Configuration(new File(CompacterMod.configDir, "client.config"))
    c.load()

    try {
      powerShowUnits = c.get("Display", "PowerShowUnits", "RF", "Units to use when displaying power. Valid values: EU, RF").getString
      if (powerShowUnits != "RF") powerShowMultiplier = Tuning.getSection("Power").getFloat(powerShowUnits + "_RF_Ratio")
    } finally {
      c.save()
    }
  }
}