package net.bdew.compacter.blocks

object RecurseMode extends Enumeration {
  val ENABLED = Value(0)
  val DISABLED = Value(1)
}

object CraftMode extends Enumeration {
  val TWO_THREE = Value(0)
  val THREE_TWO = Value(1)
  val THREE_ONLY = Value(2)
  val TWO_ONLY = Value(3)
}
