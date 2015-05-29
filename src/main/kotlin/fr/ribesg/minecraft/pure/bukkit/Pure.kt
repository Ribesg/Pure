package fr.ribesg.minecraft.pure.bukkit

import fr.ribesg.minecraft.pure.common.Log
import fr.ribesg.minecraft.pure.common.MCJarHandler
import fr.ribesg.minecraft.pure.common.MCVersion
import org.bukkit.World.Environment
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.generator.ChunkGenerator
import org.bukkit.plugin.java.JavaPlugin
import java.io.IOException

/**
 * @author Ribesg
 */
public object Pure : JavaPlugin() {

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

        val split = id.splitBy(",")
        if (split.size() > 2) {
            Log.error("Invalid id: " + id)
            return null
        }

        val version = MCVersion.get(split[0]) ?: return null
        val environment: Environment?
        if (split.size() > 1) {
            try {
                environment = Environment.valueOf(split[1].toUpperCase())
            } catch (e: IllegalArgumentException) {
                Log.error("Invalid Bukkit Environment String: " + split[1].toUpperCase())
                environment = null
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

}
