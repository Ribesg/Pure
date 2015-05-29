package fr.ribesg.minecraft.pure.util.remap

import fr.ribesg.minecraft.pure.common.Log
import fr.ribesg.minecraft.pure.common.MCJarHandler
import fr.ribesg.minecraft.pure.common.MCVersion
import fr.ribesg.minecraft.pure.util.FileUtils
import fr.ribesg.minecraft.pure.util.HashUtils
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.Proxy.Type
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.logging.Logger

/**
 * @author Ribesg
 */

/**
 * Download, remap and hash all known MC version.
 */
public fun main(args: Array<String>) {
    // Default format is on 2 lines, it's horrible!
    System.setProperty(
        "java.util.logging.SimpleFormatter.format",
        "[%1\$tY-%1\$tm-%1\$td %1\$tH:%1\$tM:%1\$tS] %4\$s: %5\$s%n"
    )
    Log.initJavaLogger(Logger.getLogger("RemapAllMain"))
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
        Log.error("Invalid arguments", e)
    }

    val executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1)
    for (v in MCVersion.values()) {
        executor.submit({
            try {
                MCJarHandler.require(Paths.get(""), v, false)
            } catch (e: IOException) {
                Log.error("Failed to require " + v, e)
            }
        })
        Thread.sleep(250)
    }
    executor.shutdown()
    executor.awaitTermination(5, TimeUnit.MINUTES)
    Files.newDirectoryStream(Paths.get("jars")).use { directory ->
        for (file in directory) {
            Log.info(HashUtils.hashSha256(file) + " - " + file.getFileName())
        }
    }
}
