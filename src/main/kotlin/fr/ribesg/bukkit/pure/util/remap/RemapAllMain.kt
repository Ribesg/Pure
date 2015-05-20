package fr.ribesg.bukkit.pure.util.remap

import fr.ribesg.bukkit.pure.MCJarHandler
import fr.ribesg.bukkit.pure.MCVersion
import fr.ribesg.bukkit.pure.Pure
import fr.ribesg.bukkit.pure.util.FileUtils
import fr.ribesg.bukkit.pure.util.HashUtils
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.Proxy.Type
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.logging.Level

/**
 * @author Ribesg
 */

/**
 * Download, remap and hash all known MC version.
 */
public fun main(args: Array<String>) {
    try {
        if (args.size() == 2) {
            val proxyUrl = args[0]
            val proxyPort = try {
                Integer.parseInt(args[1])
            } catch (e: NumberFormatException) {
                throw IllegalArgumentException("Usage: [proxyUrl proxyPort]")
            }
            FileUtils.proxy = Proxy(Type.HTTP, InetSocketAddress(proxyUrl, proxyPort))
        } else if (args.size() != 0) {
            throw IllegalArgumentException("Usage: [proxyUrl proxyPort]")
        }
    } catch(e: IllegalArgumentException) {
        Pure.logger().log(Level.SEVERE, "Invalid arguments", e)
    }

    val executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1)
    for (v in MCVersion.values()) {
        executor.submit({
            try {
                MCJarHandler.require(v, false)
            } catch (e: IOException) {
                Pure.logger().log(Level.SEVERE, "Failed to require " + v, e)
            }
        })
        Thread.sleep(250)
    }
    executor.shutdown()
    executor.awaitTermination(5, TimeUnit.MINUTES)
    Files.newDirectoryStream(Paths.get("jars")).use { directory ->
        for (file in directory) {
            Pure.logger().info(HashUtils.hashSha256(file) + " - " + file.getFileName())
        }
    }
}
