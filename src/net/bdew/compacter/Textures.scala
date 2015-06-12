package net.bdew.compacter

import net.bdew.compacter.blocks.{CraftMode, RecurseMode}
import net.bdew.lib.gui._
import net.bdew.lib.multiblock.data.RSMode
import net.minecraft.util.ResourceLocation

object Textures {
  val image = new ResourceLocation("compacter", "textures/gui/compacter.png")

  val buttonBase = Texture(image, Rect(178, 2, 16, 16))
  val buttonHover = Texture(image, Rect(194, 2, 16, 16))

  val modeTextures = Map[AnyRef, Texture](
    RecurseMode.DISABLED -> Texture(image, Rect(211, 3, 14, 14)),
    RecurseMode.ENABLED -> Texture(image, Rect(227, 3, 14, 14)),

    RSMode.RS_OFF -> Texture(image, Rect(179, 19, 14, 14)),
    RSMode.RS_ON -> Texture(image, Rect(195, 19, 14, 14)),
    RSMode.ALWAYS -> Texture(image, Rect(211, 19, 14, 14)),
    RSMode.NEVER -> Texture(image, Rect(227, 19, 14, 14)),

    CraftMode.TWO_THREE -> Texture(image, Rect(179, 35, 14, 14)),
    CraftMode.THREE_TWO -> Texture(image, Rect(195, 35, 14, 14)),
    CraftMode.THREE_ONLY -> Texture(image, Rect(211, 35, 14, 14)),
    CraftMode.TWO_ONLY -> Texture(image, Rect(227, 35, 14, 14))
  )
}
