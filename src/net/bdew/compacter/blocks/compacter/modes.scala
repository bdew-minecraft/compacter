/*
 * Copyright (c) bdew, 2015 - 2017
 * https://github.com/bdew/compacter
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.compacter.blocks.compacter

object RecurseMode extends Enumeration {
  val ENABLED = Value(0)
  val DISABLED = Value(1)
}

object CraftMode extends Enumeration {
  val TWO_THREE = Value(0)
  val THREE_TWO = Value(1)
  val THREE_ONLY = Value(2)
  val TWO_ONLY = Value(3)
  val ONE_ONLY = Value(4)
  val HOLLOW = Value(5)
}
