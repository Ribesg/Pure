package fr.ribesg.bukkit.pure.util;

import fr.ribesg.bukkit.pure.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.*;
import java.util.logging.Level;

/**
 * @author Ribesg
 */
public final class RemapAllMain {

    /**
     * Download, remap and hash all known MC version.
     */
    public static void main(final String[] args) throws Throwable {
        final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);
        for (final MCVersion v : MCVersion.values()) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        MCJarHandler.require(v, false);
                    } catch (final IOException e) {
                        Pure.logger().log(Level.SEVERE, "Failed to require " + v, e);
                    }
                }
            });
            Thread.sleep(250);
        }
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.MINUTES);
        try (final DirectoryStream<Path> s = Files.newDirectoryStream(Paths.get("jars"))) {
            for (final Path p : s) {
                Pure.logger().info(p.getFileName() + "\n\t" + HashUtils.hashSha256(p));
            }
        }
    }
}
