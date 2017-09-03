/*
 * Copyright (c) bdew, 2015 - 2017
 * https://github.com/bdew/compacter
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.compacter.misc

import net.bdew.lib.data.base.{DataSlotContainer, DataSlotVal, UpdateKind}
import net.minecraft.nbt.NBTTagCompound

case class DataSlotEnum[T <: Enumeration](name: String, parent: DataSlotContainer, enum: T) extends DataSlotVal[T#Value] {
  setUpdate(UpdateKind.GUI, UpdateKind.SAVE)

  override def default = enum(0)

  override def loadValue(t: NBTTagCompound, kind: UpdateKind.Value): T#Value =
    enum(t.getByte(name))

  override def save(t: NBTTagCompound, kind: UpdateKind.Value): Unit =
    t.setByte(name, value.id.toByte)
}
