package net.bdew.compacter.misc

import net.bdew.lib.data.base.{DataSlot, DataSlotContainer, UpdateKind}
import net.bdew.lib.nbt
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound

case class DataSlotItemQueue(name: String, parent: DataSlotContainer) extends DataSlot {
  private val queue = collection.mutable.Queue.empty[ItemStack]

  setUpdate(UpdateKind.SAVE)

  import nbt._

  def push(v: ItemStack) = queue += v
  def pushAll(v: List[ItemStack]) = queue ++= v
  def pop(): ItemStack = queue.dequeue()
  def isEmpty = queue.isEmpty
  def nonEmpty = queue.nonEmpty

  override def save(t: NBTTagCompound, kind: UpdateKind.Value): Unit = {
    t.setList(name, queue map (x => NBT.from(x.writeToNBT)))
  }

  override def load(t: NBTTagCompound, kind: UpdateKind.Value): Unit = {
    queue.clear()
    queue ++= t.getList[NBTTagCompound](name) map ItemStack.loadItemStackFromNBT
  }
}
