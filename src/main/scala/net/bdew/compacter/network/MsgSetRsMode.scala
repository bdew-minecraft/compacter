package net.bdew.compacter.network

import net.bdew.lib.misc.RSMode
import net.minecraft.network.PacketBuffer

case class MsgSetRsMode(rsMode: RSMode.Value) extends NetworkHandler.Message

object CodecSetRsMode extends NetworkHandler.Codec[MsgSetRsMode] {
  override def encodeMsg(m: MsgSetRsMode, p: PacketBuffer): Unit =
    p.writeByte(m.rsMode.id)

  override def decodeMsg(p: PacketBuffer): MsgSetRsMode =
    MsgSetRsMode(RSMode(p.readByte()))
}