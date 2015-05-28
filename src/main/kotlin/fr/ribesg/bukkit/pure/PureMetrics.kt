package fr.ribesg.bukkit.pure

import fr.ribesg.bukkit.pure.log.Log
import org.bukkit.generator.ChunkGenerator
import org.mcstats.Metrics
import org.mcstats.Metrics.Plotter
import java.io.IOException
import java.util.WeakHashMap

/**
 * Handles Metrics.
 *
 * @author Ribesg
 */
class PureMetrics(plugin: Pure) {

    /**
     * Map of currently used World Generators and their version.
     */
    private val currentGenerators: MutableMap<ChunkGenerator, MCVersion>

    init {
        this.currentGenerators = WeakHashMap()

        try {
            val metrics = Metrics(plugin)
            this.createGraphs(metrics)
            metrics.start()
        } catch (e: IOException) {
            Log.error("Failed to initialize Metrics", e)
        }
    }

    /**
     * Registers a new World Generator.
     *
     * @param version   the new World Generator version
     * @param generator the new World Generator
     */
    fun newGenerator(version: MCVersion, generator: ChunkGenerator) {
        this.currentGenerators.put(generator, version)
    }

    /**
     * Creates all Metrics graphs.
     *
     * @param metrics the Metrics instance
     */
    private fun createGraphs(metrics: Metrics) {
        val worldsGraph = metrics.createGraph("Worlds Generated")

        for (version in MCVersion.values()) {
            worldsGraph.addPlotter(object : Plotter(version.name()) {
                override fun getValue() = this@PureMetrics.currentGenerators.values().count { it == version }
            })
        }
    }
}
