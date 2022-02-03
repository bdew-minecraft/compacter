package net.bdew.compacter.network

import net.bdew.compacter.blocks.compacter.CraftMode
import net.minecraft.network.FriendlyByteBuf

case class MsgSetCraftMode(craftMode: CraftMode.Value) extends NetworkHandler.Message

object CodecSetCraftMode extends NetworkHandler.Codec[MsgSetCraftMode] {
  override def encodeMsg(m: MsgSetCraftMode, p: FriendlyByteBuf): Unit =
    p.writeByte(m.craftMode.id)

  override def decodeMsg(p: FriendlyByteBuf): MsgSetCraftMode =
    MsgSetCraftMode(CraftMode(p.readByte()))
}