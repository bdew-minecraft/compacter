package net.bdew.compacter

import net.bdew.compacter.misc.CompacterCaches
import net.bdew.compacter.network.NetworkHandler
import net.bdew.compacter.registries.{Blocks, Containers, Items}
import net.minecraft.client.resources.ReloadListener
import net.minecraft.profiler.IProfiler
import net.minecraft.resources.IResourceManager
import net.minecraftforge.client.event.RecipesUpdatedEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.AddReloadListenerEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import org.apache.logging.log4j.{LogManager, Logger}

@Mod(Compacter.ModId)
object Compacter {
  final val ModId = "compacter"

  val log: Logger = LogManager.getLogger

  def logDebug(msg: String, args: Any*): Unit = log.debug(msg.format(args: _*))
  def logInfo(msg: String, args: Any*): Unit = log.info(msg.format(args: _*))
  def logWarn(msg: String, args: Any*): Unit = log.warn(msg.format(args: _*))
  def logError(msg: String, args: Any*): Unit = log.error(msg.format(args: _*))
  def logWarnException(msg: String, t: Throwable, args: Any*): Unit = log.warn(msg.format(args: _*), t)
  def logErrorException(msg: String, t: Throwable, args: Any*): Unit = log.error(msg.format(args: _*), t)

  Items.init()
  Blocks.init()
  Containers.init()
  NetworkHandler.init()

  ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER)

  MinecraftForge.EVENT_BUS.register(this)

  @SubscribeEvent
  def addReloadListener(event: AddReloadListenerEvent): Unit = {
    event.addListener(
      new ReloadListener[Void]() {
        override protected def prepare(manager: IResourceManager, profiler: IProfiler): Void = {
          null
        }

        override protected def apply(nothing: Void, resourceManagerIn: IResourceManager, profilerIn: IProfiler): Unit = {
          CompacterCaches.invalidate()
        }
      })
  }

  @SubscribeEvent
  def recipesUpdated(event: RecipesUpdatedEvent): Unit = {
    CompacterCaches.invalidate()
  }
}
