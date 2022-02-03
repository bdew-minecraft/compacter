package net.bdew.compacter.network

import net.bdew.lib.misc.RSMode
import net.minecraft.network.FriendlyByteBuf

case class MsgSetRsMode(rsMode: RSMode.Value) extends NetworkHandler.Message

object CodecSetRsMode extends NetworkHandler.Codec[MsgSetRsMode] {
  override def encodeMsg(m: MsgSetRsMode, p: FriendlyByteBuf): Unit =
    p.writeByte(m.rsMode.id)

  override def decodeMsg(p: FriendlyByteBuf): MsgSetRsMode =
    MsgSetRsMode(RSMode(p.readByte()))
}