package fr.ribesg.bukkit.pure;

import org.bukkit.World.Environment;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is Pure.
 *
 * @author Ribesg
 */
public final class Pure extends JavaPlugin {

    /**
     * Static Pure Logger accessor.
     *
     * @return the Logger of the Pure Bukkit plugin
     */
    public static Logger logger() {
        if (Pure.instance == null) {
            return Logger.getLogger("Pure");
        }
        return Pure.instance.getLogger();
    }

    /**
     * Static Pure data folder accessor.
     *
     * @return the data folder of the Pure Bukkit plugin
     */
    public static File getFolder() {
        if (Pure.instance == null) {
            return new File("");
        }
        return Pure.instance.getDataFolder();
    }

    /**
     * Private instance, to be used by static accessors.
     */
    private static Pure instance = null;

    /**
     * Metrics
     */
    private PureMetrics metrics;

    @Override
    public void onEnable() {
        Pure.instance = this;
        this.metrics = new PureMetrics(this);
    }

    @Override
    public void onDisable() {
        Pure.instance = null;
        this.metrics = null;
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(final String worldName, final String id) {
        if (id == null || id.isEmpty()) {
            Pure.logger().severe("Parameters are required for the Pure world generator.");
            return null;
        }

        final String[] split = id.split(",");
        if (split.length > 2) {
            Pure.logger().severe("Invalid id: " + id);
            return null;
        }

        final MCVersion version;
        final Environment environment;
        try {
            version = MCVersion.valueOf(split[0].toUpperCase());
        } catch (final IllegalArgumentException e) {
            Pure.logger().severe("Invalid MC version String: " + split[0].toUpperCase());
            return null;
        }
        if (split.length > 1) {
            try {
                environment = Environment.valueOf(split[1].toUpperCase());
            } catch (final IllegalArgumentException e) {
                Pure.logger().severe("Invalid Bukkit Environment String: " + split[1].toUpperCase());
                return null;
            }
        } else {
            environment = null;
        }

        try {
            MCJarHandler.require(version, true);
        } catch (final IOException e) {
            Pure.logger().log(Level.SEVERE, "Failed to install MC Version " + version, e);
            return null;
        }

        try {
            final ChunkGenerator generator = version.getChunkGenerator(environment);
            this.metrics.newGenerator(version, generator);
            return generator;
        } catch (final IllegalStateException e) {
            Pure.logger().log(Level.SEVERE, "Failed to get Chunk Generator for version " + version, e);
            return null;
        }
    }
}
