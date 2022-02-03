package net.bdew.compacter.misc

import net.bdew.lib.PimpVanilla._
import net.bdew.lib.data.base.{DataSlot, DataSlotContainer, UpdateKind}
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.ItemStack

case class DataSlotItemQueue(name: String, parent: DataSlotContainer) extends DataSlot {
  private val queue = collection.mutable.Queue.empty[ItemStack]

  setUpdate(UpdateKind.SAVE)

  def push(v: ItemStack): Unit = queue += v
  def pushAll(v: List[ItemStack]): Unit = queue ++= v
  def pop(): ItemStack = queue.dequeue()
  def isEmpty: Boolean = queue.isEmpty
  def nonEmpty: Boolean = queue.nonEmpty

  override def save(t: CompoundTag, kind: UpdateKind.Value): Unit = {
    t.setListVals(name, queue)
  }

  override def load(t: CompoundTag, kind: UpdateKind.Value): Unit = {
    queue.clear()
    queue ++= t.getListVals[ItemStack](name)
  }
}
