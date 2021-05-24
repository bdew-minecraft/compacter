package net.bdew.compacter.blocks.compacter

object RecurseMode extends Enumeration {
  val ENABLED: RecurseMode.Value = Value(0)
  val DISABLED: RecurseMode.Value = Value(1)
}

object CraftMode extends Enumeration {
  val TWO_THREE: CraftMode.Value = Value(0)
  val THREE_TWO: CraftMode.Value = Value(1)
  val THREE_ONLY: CraftMode.Value = Value(2)
  val TWO_ONLY: CraftMode.Value = Value(3)
  val ONE_ONLY: CraftMode.Value = Value(4)
  val HOLLOW: CraftMode.Value = Value(5)
}
