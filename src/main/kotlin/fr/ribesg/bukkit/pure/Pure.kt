package fr.ribesg.bukkit.pure

import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.generator.ChunkGenerator
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.IOException
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.platform.platformStatic

/**
 * This is Pure.
 *
 * @author Ribesg
 */
class Pure : JavaPlugin() {

    companion object {

        /**
         * Static Pure Logger accessor.
         *
         * @return the Logger of the Pure Bukkit plugin
         */
        platformStatic
        public fun logger(): Logger =
            this.instance?.getLogger() ?: {
                // Default format is on 2 lines, it's horrible!
                System.setProperty(
                    "java.util.logging.SimpleFormatter.format",
                    "[%1\$tY-%1\$tm-%1\$td %1\$tH:%1\$tM:%1\$tS] %4\$s: %5\$s%n"
                )
                Logger.getLogger("Pure")
            }()

        /**
         * Static Pure data folder accessor.
         *
         * @return the data folder of the Pure Bukkit plugin
         */
        public fun getFolder(): File = this.instance?.getDataFolder() ?: File("")

        /**
         * Private instance, to be used by static accessors.
         */
        private var instance: Pure? = null
    }

    /**
     * Metrics
     */
    private var metrics: PureMetrics? = null

    override fun onEnable() {
        instance = this
        this.metrics = PureMetrics(this)

        // XXX For debugging that snow bug in 1.6.4
        this.getServer().getPluginManager().registerEvents(object : Listener {
            EventHandler
            fun onPlayerInteract(event: PlayerInteractEvent) {
                val b = when (event.getAction()) {
                    Action.LEFT_CLICK_BLOCK  -> event.getClickedBlock()
                    Action.RIGHT_CLICK_BLOCK -> event.getClickedBlock().getRelative(event.getBlockFace())
                    else                     -> null
                }
                if (b != null) {
                    event.getPlayer().sendMessage(b.getType().name() + " - " + b.getData());
                }
            }
        }, this)
    }

    override fun onDisable() {
        instance = null
        this.metrics = null
    }

    override fun getDefaultWorldGenerator(worldName: String, id: String): ChunkGenerator? {
        if (id.isEmpty()) {
            logger().severe("Parameters are required for the Pure world generator.")
            return null
        }

        val split = id.split(",")
        if (split.size() > 2) {
            logger().severe("Invalid id: " + id)
            return null
        }

        var version: MCVersion
        var environment: World.Environment?
        try {
            version = MCVersion.valueOf(split[0].toUpperCase())
        } catch (e: IllegalArgumentException) {
            logger().severe("Invalid MC version String: " + split[0].toUpperCase())
            this.suggestVersionString(split[0].toUpperCase())
            return null
        }
        if (split.size() > 1) {
            try {
                environment = World.Environment.valueOf(split[1].toUpperCase())
            } catch (e: IllegalArgumentException) {
                logger().severe("Invalid Bukkit Environment String: " + split[1].toUpperCase())
                return null
            }
        } else {
            environment = null
        }

        try {
            MCJarHandler.require(version, true)
        } catch (e: IOException) {
            logger().log(Level.SEVERE, "Failed to install MC Version " + version, e)
            return null
        }

        try {
            val generator = version.getChunkGenerator(environment)
            this.metrics?.newGenerator(version, generator)
            return generator
        } catch (e: IllegalStateException) {
            logger().log(Level.SEVERE, "Failed to get Chunk Generator for version " + version, e)
            return null
        }
    }

    private fun suggestVersionString(version: String) {
        val withUnderscores = version.replace(".", "_")
        try {
            MCVersion.valueOf(withUnderscores)
            logger().info("Did you mean '" + withUnderscores + "' ?")
            return
        } catch (ignored: IllegalArgumentException) {
            // Just continue
        }
        when (withUnderscores) {
            "A1_2_6" ->
                logger().info("You have to provide SERVER version, for Alpha 1.2.6 the correct version is 'A0_2_8'")
            else     ->
                logger().info("Find a list of all available versions on https://github.com/Ribesg/Pure !")
        }
    }
}
