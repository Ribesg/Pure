package fr.ribesg.minecraft.pure.common

import fr.ribesg.minecraft.pure.util.FileUtils
import fr.ribesg.minecraft.pure.util.HashUtils
import java.io.IOException
import java.net.URL
import java.net.URLClassLoader
import java.nio.file.Files
import java.nio.file.Path
import java.util.EnumMap
import kotlin.platform.platformStatic as static

/**
 * Loads and relocate classes of the Minecraft Server.
 *
 * @author Ribesg
 */
public object MCJarHandler {

    private val loadedVersions: MutableMap<MCVersion, Boolean>

    init {
        this.loadedVersions = EnumMap(javaClass<MCVersion>())
        for (v in MCVersion.values()) {
            this.loadedVersions.put(v, false)
        }
    }

    /**
     * Requires a Minecraft Server Version.
     *
     * This method will first check if the required version exists on the disk.
     * If it doesn't exist it will download the original Minecraft Server jar
     * file from Mojang source then remap the jar classes into a new .remapped.jar
     * file.
     * The method will then initialize a {@link ClassLoader} for this jar file
     * and load all the classes in the jar file.
     *
     * @param version   the required version
     * @param checkHash if we check for file hashes or not
     *
     * @throws IOException if anything goes wrong
     */
    throws(IOException::class)
    public static fun require(folder: Path, version: MCVersion, checkHash: Boolean) {
        if (!this.loadedVersions.get(version)!!) {
            Log.info("Minecraft Version " + version.name() + " required for World Generation")
            // Find (and eventually create) our plugin's folder subfolder containing jar files (plugin/Pure/jars)
            val jarContainerPath = folder.resolve("jars")
            if (!Files.isDirectory(jarContainerPath)) {
                Files.createDirectories(jarContainerPath)
            }

            // Find jar file name from the Minecraft jar URL
            val split = version.getUrl().toString().splitBy("/")
            val inputJarName = split[split.size() - 1]

            // Now we build the final (future?) Path of both our jar file and its remapped version on the file system
            val jarPath = jarContainerPath.resolve(inputJarName)
            val remappedJarPath = jarContainerPath.resolve(
                inputJarName.substring(0, inputJarName.length() - 4) + ".remapped.jar" // -4 == ".jar"
            )

            // Download the Vanilla jar file
            if (!Files.exists(jarPath)) {
                // Doesn't exist, just download it
                Log.info("Downloading file " + version.getUrl() + " ...")
                FileUtils.download(
                    jarContainerPath,
                    version.getUrl(),
                    inputJarName,
                    if (checkHash) version.getVanillaHash() else null
                )
                Log.info("Done downloading file!")
            } else if (checkHash) {
                // Already exists, check hash and redownload it if needed
                Log.info("Hashing existing jar to make sure it's correct...")
                val hash = HashUtils.hashSha256(jarPath)
                if (version.getVanillaHash().equals(hash)) {
                    Log.info("Vanilla jar hash correct!")
                } else {
                    Log.info("Invalid hash, redownloading file " + version.getUrl() + " ...")
                    Files.delete(jarPath)
                    FileUtils.download(jarContainerPath, version.getUrl(), inputJarName, version.getVanillaHash())
                }
            }

            // Relocate the jar classes packages and put that into our remapped jar file
            if (!Files.exists(remappedJarPath)) {
                // Doesn't exist, just relocate it
                Log.info("Relocating jar file classes (this can take up to 5 minutes on a slow PC!)...")
                FileUtils.relocateJarContent(jarPath, remappedJarPath, version, checkHash)
                Log.info("Done relocating jar file classes!")
            } else if (checkHash) {
                // Already exists, check hash and remap if needed
                Log.info("Hashing existing remapped jar to make sure it's correct...")
                val hash = HashUtils.hashSha256(remappedJarPath)
                if (version.getRemappedHash().equals(hash)) {
                    Log.info("Remapped jar hash correct!")
                } else {
                    Log.info("Invalid hash, remapping file...")
                    Files.delete(remappedJarPath)
                    FileUtils.relocateJarContent(jarPath, remappedJarPath, version, true)
                }
            }

            // Load the remapped jar using our current classloader
            try {
                val addURL = javaClass<URLClassLoader>().getDeclaredMethod("addURL", javaClass<URL>())
                addURL.setAccessible(true)
                addURL.invoke(ClassLoader.getSystemClassLoader(), remappedJarPath.toFile().toURI().toURL())
                Log.info("Minecraft Version " + version.name() + " ready!")
            } catch (e: ReflectiveOperationException) {
                Log.error("Failed to load classes of $remappedJarPath", e)
            }
        }
    }

}
