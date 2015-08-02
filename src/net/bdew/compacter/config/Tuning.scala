/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/compacter
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.compacter.config

import java.io.{File, FileWriter}

import net.bdew.compacter.CompacterMod
import net.bdew.lib.recipes.gencfg.{ConfigSection, GenericConfigLoader, GenericConfigParser}
import net.bdew.lib.recipes.{RecipeLoader, RecipeParser, RecipesHelper}

object Tuning extends ConfigSection

object TuningLoader {
  val loader = new RecipeLoader with GenericConfigLoader {
    override def newParser(): RecipeParser = new RecipeParser with GenericConfigParser
    override val cfgStore: ConfigSection = Tuning
  }

  def loadDelayed() = loader.processRecipeStatements()

  def loadConfigFiles() {
    if (!CompacterMod.configDir.exists()) {
      CompacterMod.configDir.mkdir()
      val nl = System.getProperty("line.separator")
      val f = new FileWriter(new File(CompacterMod.configDir, "readme.txt"))
      f.write("Any .cfg files in this directory will be loaded after the internal configuration, in alphabetic order" + nl)
      f.write("Files in 'overrides' directory with matching names cab be used to override internal configuration" + nl)
      f.close()
    }

    RecipesHelper.loadConfigs(
      modName = "Compacter",
      listResource = "/assets/compacter/config/files.lst",
      configDir = CompacterMod.configDir,
      resBaseName = "/assets/compacter/config/",
      loader = loader)
  }
}



