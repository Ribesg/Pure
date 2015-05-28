package fr.ribesg.bukkit.pure

import fr.ribesg.bukkit.pure.log.Log
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.generator.ChunkGenerator
import org.bukkit.plugin.java.JavaPlugin
import java.io.IOException

/**
 * This is Pure.
 *
 * @author Ribesg
 */
class Pure : JavaPlugin() {

    private var metrics: PureMetrics? = null

    override fun onEnable() {
        Log.initJavaLogger(this.getLogger())

        this.metrics = PureMetrics(this)

        // XXX For debugging that snow bug in 1.6.4
        this.getServer().getPluginManager().registerEvents(object : Listener {
            EventHandler fun onPlayerInteract(event: PlayerInteractEvent) {
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
        this.metrics = null

        Log.dereferenceLogger()
    }

    override fun getDefaultWorldGenerator(worldName: String, id: String): ChunkGenerator? {
        if (id.isEmpty()) {
            Log.error("Parameters are required for the Pure world generator.")
            return null
        }

        val split = id.split(",")
        if (split.size() > 2) {
            Log.error("Invalid id: " + id)
            return null
        }

        var version: MCVersion
        var environment: World.Environment?
        try {
            version = MCVersion.valueOf(split[0].toUpperCase())
        } catch (e: IllegalArgumentException) {
            Log.error("Invalid MC version String: " + split[0].toUpperCase())
            this.suggestVersionString(split[0].toUpperCase())
            return null
        }
        if (split.size() > 1) {
            try {
                environment = World.Environment.valueOf(split[1].toUpperCase())
            } catch (e: IllegalArgumentException) {
                Log.error("Invalid Bukkit Environment String: " + split[1].toUpperCase())
                return null
            }
        } else {
            environment = null
        }

        try {
            MCJarHandler.require(this.getDataFolder().toPath().resolve("jars"), version, true)
        } catch (e: IOException) {
            Log.error("Failed to install MC Version $version", e)
            return null
        }

        try {
            val generator = version.getChunkGenerator(environment)
            this.metrics?.newGenerator(version, generator)
            return generator
        } catch (e: IllegalStateException) {
            Log.error("Failed to get Chunk Generator for version $version", e)
            return null
        }
    }

    private fun suggestVersionString(version: String) {
        val withUnderscores = version.replace(".", "_")
        try {
            MCVersion.valueOf(withUnderscores)
            Log.info("Did you mean '$withUnderscores'?")
            return
        } catch (ignored: IllegalArgumentException) {
            // Just continue
        }
        when (withUnderscores) {
            "A1_2_6" ->
                Log.info("You have to provide SERVER version, for Alpha 1.2.6 the correct version is 'A0_2_8'")
            else     ->
                Log.info("Find a list of all available versions on https://github.com/Ribesg/Pure !")
        }
    }
}
