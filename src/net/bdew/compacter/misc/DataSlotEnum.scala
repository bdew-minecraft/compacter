package net.bdew.compacter.misc

import net.bdew.lib.data.base.{DataSlotContainer, DataSlotVal, UpdateKind}
import net.minecraft.nbt.NBTTagCompound

case class DataSlotEnum[T <: Enumeration](name: String, parent: DataSlotContainer, enum: T) extends DataSlotVal[T#Value] {
  override var value: T#Value = enum(0)

  setUpdate(UpdateKind.GUI, UpdateKind.SAVE)

  override def load(t: NBTTagCompound, kind: UpdateKind.Value): Unit =
    value = enum(t.getByte(name))

  override def save(t: NBTTagCompound, kind: UpdateKind.Value): Unit =
    t.setByte(name, value.id.toByte)
}
