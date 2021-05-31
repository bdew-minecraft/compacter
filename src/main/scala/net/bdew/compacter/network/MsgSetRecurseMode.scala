package net.bdew.compacter.network

import net.bdew.compacter.blocks.compacter.RecurseMode
import net.minecraft.network.PacketBuffer

case class MsgSetRecurseMode(recurseMode: RecurseMode.Value) extends NetworkHandler.Message

object CodecSetRecurseMode extends NetworkHandler.Codec[MsgSetRecurseMode] {
  override def encodeMsg(m: MsgSetRecurseMode, p: PacketBuffer): Unit =
    p.writeByte(m.recurseMode.id)

  override def decodeMsg(p: PacketBuffer): MsgSetRecurseMode =
    MsgSetRecurseMode(RecurseMode(p.readByte()))
}