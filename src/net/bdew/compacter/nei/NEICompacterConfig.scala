/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/compacter
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.compacter.nei

import codechicken.nei.api.{API, IConfigureNEI}

class NEICompacterConfig extends IConfigureNEI {
  override val getName = "Compacter"
  override val getVersion = "COMPACTER_VER"
  override def loadConfig(): Unit = {
    val compacterRecipeHandler = new CompacterRecipeHandler
    API.registerRecipeHandler(compacterRecipeHandler)
    API.registerUsageHandler(compacterRecipeHandler)
  }
}
