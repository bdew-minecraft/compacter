package net.bdew.compacter

import net.bdew.lib.config.{ConfigSection, PowerConfig}
import net.minecraftforge.common.ForgeConfigSpec

class CompacterConfig(spec: ForgeConfigSpec.Builder) extends PowerConfig(spec, 5000, 20000) {
  val craftEnergyUsed: () => Float = floatVal(spec, "CraftEnergyUsed", "Energy used per craft operation (FE)", 20)
}

object Config {
  private val serverBuilder = new ForgeConfigSpec.Builder

  val Compacter: CompacterConfig = ConfigSection(serverBuilder, "Compacter", new CompacterConfig(serverBuilder))

  val SERVER: ForgeConfigSpec = serverBuilder.build()
}
