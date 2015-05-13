package fr.ribesg.bukkit.pure;

import org.bukkit.generator.ChunkGenerator;
import org.mcstats.Metrics;
import org.mcstats.Metrics.Graph;

import java.io.IOException;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.logging.Level;

/**
 * Handles Metrics.
 *
 * @author Ribesg
 */
public final class PureMetrics {

    /**
     * Map of currently used World Generators and their version.
     */
    private final Map<ChunkGenerator, MCVersion> currentGenerators;

    public PureMetrics(final Pure plugin) {
        this.currentGenerators = new WeakHashMap<>();

        try {
            final Metrics metrics = new Metrics(plugin);
            this.createGraphs(metrics);
            metrics.start();
        } catch (final IOException e) {
            Pure.logger().log(Level.SEVERE, "Failed to initialize Metrics", e);
        }
    }

    /**
     * Registers a new World Generator.
     *
     * @param version   the new World Generator version
     * @param generator the new World Generator
     */
    public void newGenerator(final MCVersion version, final ChunkGenerator generator) {
        this.currentGenerators.put(generator, version);
    }

    /**
     * Creates all Metrics graphs.
     *
     * @param metrics the Metrics instance
     */
    private void createGraphs(final Metrics metrics) {
        final Graph worldsGraph = metrics.createGraph("Worlds Generated");

        for (final MCVersion version : MCVersion.values()) {
            worldsGraph.addPlotter(new Metrics.Plotter(version.name()) {
                @Override
                public int getValue() {
                    int value = 0;
                    for (final MCVersion v : PureMetrics.this.currentGenerators.values()) {
                        if (version == v) {
                            value++;
                        }
                    }
                    return value;
                }
            });
        }
    }
}
