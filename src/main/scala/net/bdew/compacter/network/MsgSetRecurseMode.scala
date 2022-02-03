package net.bdew.compacter.network

import net.bdew.compacter.blocks.compacter.RecurseMode
import net.minecraft.network.FriendlyByteBuf

case class MsgSetRecurseMode(recurseMode: RecurseMode.Value) extends NetworkHandler.Message

object CodecSetRecurseMode extends NetworkHandler.Codec[MsgSetRecurseMode] {
  override def encodeMsg(m: MsgSetRecurseMode, p: FriendlyByteBuf): Unit =
    p.writeByte(m.recurseMode.id)

  override def decodeMsg(p: FriendlyByteBuf): MsgSetRecurseMode =
    MsgSetRecurseMode(RecurseMode(p.readByte()))
}