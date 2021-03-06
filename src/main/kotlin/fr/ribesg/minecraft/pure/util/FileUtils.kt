package fr.ribesg.minecraft.pure.util

import com.tonicsystems.jarjar.Main
import fr.ribesg.minecraft.pure.common.Log
import fr.ribesg.minecraft.pure.common.MCVersion
import fr.ribesg.minecraft.pure.use
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.Proxy
import java.net.URL
import java.nio.channels.Channels
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * Used to download things.
 *
 * @author Ribesg
 */
object FileUtils {

    private val MAX_DOWNLOAD_ATTEMPTS = 5

    /**
     * Proxy used for downloads
     */
    var proxy: Proxy? = null

    /**
     * Downloads a file.
     *
     * @param destFolder the folder in which the file will be saved
     * @param srcUrl         the URL to the file to download
     * @param fileName    the final name of the saved file
     * @param wantedHash        the required hash for the file or null if there is none
     *
     * @return the path to the downloaded file
     */
    fun download(destFolder: Path, srcUrl: URL, fileName: String, wantedHash: String?): Path {
        if ((!Files.exists(destFolder) || !Files.isDirectory(destFolder)) && !destFolder.toFile().mkdirs()) {
            throw IOException("Folder " + destFolder.toString() + " doesn't exist and cannot be created")
        }
        val finalFile = File(destFolder.toFile(), fileName)

        var attempt = 1
        while (true) {
            try {
                use(
                    Channels.newChannel(
                        if (proxy == null) {
                            srcUrl.openStream()
                        } else {
                            srcUrl.openConnection(this.proxy!!).inputStream
                        }
                    ),
                    FileOutputStream(finalFile)
                ) { src, dst ->
                    Log.debug("Downloading $srcUrl ...")
                    dst.channel.transferFrom(src, 0, Long.MAX_VALUE)
                    if (wantedHash != null) {
                        Log.debug("Done! Checking hash...")
                        val hash = HashUtils.hashSha256(finalFile.toPath())
                        if (hash.equals(wantedHash)) {
                            Log.debug("The downloaded file is correct!")
                        } else {
                            Log.warn("The downloaded file is incorrect!")
                            throw IOException(
                                "Download file hash doesn't match awaited hash\nAwaited: $wantedHash\nReceived: $hash"
                            )
                        }
                    } else {
                        Log.debug("Done!")
                    }
                }
                break
            } catch(e: IOException) {
                Log.warn("Attempt n°$attempt failed!")
                if (attempt == FileUtils.MAX_DOWNLOAD_ATTEMPTS) {
                    throw IOException("Failed to download file", e)
                } else {
                    Log.debug("Error was: ", e)
                }
            }
            attempt++;
        }

        return finalFile.toPath()
    }

    /**
     * Relocates classes in a jar file, building a new modified jar.
     *
     * @param inputJar  input jar file
     * @param outputJar output jar file
     * @param version   Minecraft version of those jar files
     * @param checkHash if we check for file hashes or not
     *
     * @throws IOException if anything goes wrong
     */
    fun relocateJarContent(inputJar: Path, outputJar: Path, version: MCVersion, checkHash: Boolean) {
        val prefix = version.name().toLowerCase()
        val rulesFilePath = inputJar.toAbsolutePath().toString() + ".tmp"

        // Create JarJar rules file
        val rulesFile = Paths.get(rulesFilePath).toFile()
        if (rulesFile.exists()) {
            if (!rulesFile.delete()) {
                throw IOException("Failed to remove old rules file $rulesFilePath")
            }
        }
        if (!rulesFile.createNewFile()) {
            throw IOException("Failed to create rules file $rulesFilePath")
        }

        // Generate and write rules
        use (
            ZipFile(inputJar.toFile()),
            Files.newBufferedWriter(rulesFile.toPath())
        ) { zipFile, writer ->
            val enumeration = zipFile.entries()
            var entry: ZipEntry
            var entryName: String
            while (enumeration.hasMoreElements()) {
                entry = enumeration.nextElement()
                entryName = entry.name

                if (!entryName.contains("META-INF")) {
                    if (entry.isDirectory) {
                        writer.write("rule " + entryName.replace('/', '.') + "* $prefix.@0\n")
                    } else if (!entryName.contains("/") || entryName.startsWith("net/minecraft/server")) {
                        if (entryName.endsWith(".class")) {
                            val filteredName = entryName.replace(".class", "").replace('/', '.')
                            if (filteredName.contains(".")) {
                                writer.write("rule $filteredName $prefix.@0\n")
                            } else {
                                writer.write("rule $filteredName $prefix.net.minecraft.server.@0\n")
                            }
                        }
                    }
                }
            }
        }

        // Execute JarJar
        try {
            Log.debug("Executing JarJar...")
            Main.main(arrayOf(
                "process",
                rulesFilePath,
                inputJar.toString(),
                outputJar.toString()
            ))
            Log.debug("Done!")

            // Remove junk in final jar
            FileUtils.removePrefixedBy(
                outputJar,
                "META-INF", // All
                "null"      // Alpha 0.2.8
            )

            if (checkHash) {
                val wantedHash = version.getRemappedHash()
                val hash = HashUtils.hashSha256(outputJar)
                if (hash.equals(wantedHash)) {
                    Log.debug("The remapped file is correct!")
                } else {
                    throw IOException(
                        "Remapped file hash doesn't match awaited hash\nAwaited: $wantedHash\nReceived: $hash"
                    )
                }
            }
        } catch (e: Exception) {
            throw IOException("Failed to execute JarJar", e)
        } finally {
            if (!rulesFile.delete()) {
                Log.warn("Failed to remove rules file after execution")
            }
        }
    }

    /**
     * Removes any file in the provided zip file whose path starts with
     * one of the provided prefixes.
     *
     * @param prefixes the prefixes
     * @param zip      the file
     *
     * @throws IOException if anything goes wrong
     */
    fun removePrefixedBy(zip: Path, vararg prefixes: String) {
        if (prefixes.size() == 0) {
            return // Nothing to do!
        }

        // Rename original file to tmp file
        val tmp = Paths.get(zip.toAbsolutePath().toString() + ".tmp")
        Files.move(zip, tmp, StandardCopyOption.REPLACE_EXISTING)

        // Rebuild original file from tmp file while filtering content
        try {
            use(
                ZipInputStream(Files.newInputStream(tmp)),
                ZipOutputStream(Files.newOutputStream(zip))
            ) { input, output ->
                val buffer = ByteArray(1024)
                var read: Int
                var ignore: Boolean
                var entry: ZipEntry? = input.nextEntry
                while (entry != null) {
                    val entryName = entry.name
                    ignore = false
                    for (prefix in prefixes) {
                        if (entryName.startsWith(prefix)) {
                            ignore = true
                            break
                        }
                    }
                    if (!ignore) {
                        // Create entry in new file
                        output.putNextEntry(ZipEntry(entry))
                        // Copy content of entry to new file
                        read = input.read(buffer)
                        while (read > 0) {
                            output.write(buffer, 0, read)
                            read = input.read(buffer)
                        }
                    }
                    entry = input.nextEntry
                }
            }
        } finally {
            if (!tmp.toFile().delete()) {
                Log.warn("Failed to remove tmp file after execution")
            }
        }
    }

}
