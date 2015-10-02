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
import net.bdew.compacter.misc.{CompacterCache2x2, CompacterCache3x3}
import net.bdew.lib.recipes._
import net.bdew.lib.recipes.gencfg.{ConfigSection, GenericConfigLoader, GenericConfigParser}
import net.minecraftforge.oredict.OreDictionary

object Tuning extends ConfigSection

object TuningLoader {
  val loader = new RecipeLoader with GenericConfigLoader {
    override def newParser(): RecipeParser = new Parser
    override val cfgStore: ConfigSection = Tuning

    override def processRecipeStatement(st: RecipeStatement) = st match {
      case recipe: RsCompacter =>
        val outStack = getConcreteStack(recipe.result)
        if (outStack.getItemDamage == OreDictionary.WILDCARD_VALUE) {
          CompacterMod.logDebug("Output meta is unset, defaulting to 0")
          outStack.setItemDamage(0)
        }
        for (inStack <- getAllConcreteStacks(recipe.input)) {
          if (inStack.getItemDamage == OreDictionary.WILDCARD_VALUE) {
            CompacterMod.logDebug("Input meta is unset, defaulting to 0")
            inStack.setItemDamage(0)
          }
          recipe match {
            case x: RsCompacter4 =>
              CompacterMod.logInfo("Adding custom recipe: 4*%s => %s", inStack, outStack)
              CompacterCache2x2.addCustom(inStack, outStack)
            case x: RsCompacter9 =>
              CompacterMod.logInfo("Adding custom recipe: 9*%s => %s", inStack, outStack)
              CompacterCache3x3.addCustom(inStack, outStack)
          }
        }
      case _ => super.processRecipeStatement(st)
    }
  }

  trait RsCompacter extends CraftingStatement {
    def input: StackRef
    def result: StackRef
  }

  case class RsCompacter4(input: StackRef, result: StackRef) extends RsCompacter
  case class RsCompacter9(input: StackRef, result: StackRef) extends RsCompacter

  class Parser extends RecipeParser with GenericConfigParser {
    def compacterRecipe4 = "compacter" ~> ":" ~> "4" ~> "*" ~> spec ~ "=>" ~ spec ^^ { case in ~ arw ~ out => RsCompacter4(in, out) }
    def compacterRecipe9 = "compacter" ~> ":" ~> "9" ~> "*" ~> spec ~ "=>" ~ spec ^^ { case in ~ arw ~ out => RsCompacter9(in, out) }
    override def recipeStatement = super.recipeStatement | compacterRecipe4 | compacterRecipe9
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



