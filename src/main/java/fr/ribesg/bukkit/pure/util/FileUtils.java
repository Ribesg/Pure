package fr.ribesg.bukkit.pure.util;

import com.tonicsystems.jarjar.Main;
import fr.ribesg.bukkit.pure.MCVersion;
import fr.ribesg.bukkit.pure.Pure;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.*;
import java.util.Enumeration;
import java.util.zip.*;

/**
 * Used to download things.
 *
 * @author Ribesg
 */
public final class FileUtils {

    private static final int MAX_DOWNLOAD_ATTEMPS = 5;

    /**
     * Downloads a file.
     *
     * @param destinationFolder the folder in which the file will be saved
     * @param sourceUrl         the URL to the file to download
     * @param wantedFileName    the final name of the saved file
     * @param wantedHash        the required hash for the file or null if there is none
     *
     * @return the path to the downloaded file
     */
    public static Path download(final Path destinationFolder, final URL sourceUrl, final String wantedFileName, final String wantedHash) throws IOException {
        Pure.logger().entering(FileUtils.class.getName(), "download");

        if ((!Files.exists(destinationFolder) || !Files.isDirectory(destinationFolder)) && !destinationFolder.toFile().mkdirs()) {
            throw new IOException("Folder " + destinationFolder.toString() + " doesn't exist and cannot be created");
        }
        final File finalFile = new File(destinationFolder.toFile(), wantedFileName);

        int attempt = 1;
        while (true) {
            try (
                final InputStream is = sourceUrl.openStream();
                final ReadableByteChannel source = Channels.newChannel(is);
                final FileOutputStream out = new FileOutputStream(finalFile)
            ) {
                Pure.logger().fine("Downloading " + sourceUrl + " ...");
                out.getChannel().transferFrom(source, 0, Long.MAX_VALUE);
                if (wantedHash != null) {
                    Pure.logger().fine("Done! Checking hash...");
                    final String hash = HashUtils.hashSha256(finalFile.toPath());
                    if (hash.equals(wantedHash)) {
                        Pure.logger().fine("The downloaded file is correct!");
                        break;
                    } else {
                        Pure.logger().warning("The downloaded file is incorrect!");
                        throw new IOException("Download file hash doesn't match awaited hash\nAwaited: " + wantedHash + "\nReceived: " + hash);
                    }
                } else {
                    Pure.logger().fine("Done!");
                    break;
                }
            } catch (final IOException e) {
                Pure.logger().warning("Attempt n°" + attempt + " failed!");
                if (attempt == FileUtils.MAX_DOWNLOAD_ATTEMPS) {
                    throw new IOException("Failed to download file", e);
                } else {
                    Pure.logger().throwing(FileUtils.class.getName(), "download", e);
                }
            }
            attempt++;
        }

        Pure.logger().exiting(FileUtils.class.getName(), "download");

        return finalFile.toPath();
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
    public static void relocateJarContent(final Path inputJar, final Path outputJar, final MCVersion version, final boolean checkHash) throws IOException {
        Pure.logger().entering(FileUtils.class.getName(), "relocateJarContent");

        final String prefix = version.name().toLowerCase();
        final String rulesFilePath = inputJar.toAbsolutePath().toString() + ".tmp";

        // Create JarJar rules file
        final File rulesFile = Paths.get(rulesFilePath).toFile();
        if (rulesFile.exists()) {
            if (!rulesFile.delete()) {
                throw new IOException("Failed to remove old rules file " + rulesFilePath);
            }
        }
        if (!rulesFile.createNewFile()) {
            throw new IOException("Failed to create rules file " + rulesFilePath);
        }

        // Generate and write rules
        try (
            final ZipFile zipFile = new ZipFile(inputJar.toFile());
            final BufferedWriter writer = Files.newBufferedWriter(rulesFile.toPath())
        ) {
            final Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
            ZipEntry entry;
            String entryName;
            while (enumeration.hasMoreElements()) {
                entry = enumeration.nextElement();
                entryName = entry.getName();

                if (!entryName.contains("META-INF")) {
                    if (entry.isDirectory()) {
                        writer.write("rule " + entryName.replace('/', '.') + "* " + prefix + ".@0\n");
                    } else if (!entryName.contains("/")) {
                        if (entryName.endsWith(".class")) {
                            final String nameWithoutClass = entryName.replace(".class", "");
                            if (nameWithoutClass.contains(".")) {
                                writer.write("rule " + nameWithoutClass + " " + prefix + ".@0\n");
                            } else {
                                writer.write("rule " + nameWithoutClass + " " + prefix + ".net.minecraft.server.@0\n");
                            }
                        }
                    }
                }
            }
        }

        // Execute JarJar
        try {
            Pure.logger().fine("Executing JarJar...");
            Main.main(new String[] {
                "process",
                rulesFilePath,
                inputJar.toString(),
                outputJar.toString()
            });
            Pure.logger().fine("Done!");

            // Remove META-INF folder in final jar
            FileUtils.removePrefixedBy("META-INF", outputJar);

            if (checkHash) {
                final String wantedHash = version.getRemappedHash();
                final String hash = HashUtils.hashSha256(outputJar);
                if (hash.equals(wantedHash)) {
                    Pure.logger().fine("The remapped file is correct!");
                } else {
                    throw new IOException("Remapped file hash doesn't match awaited hash\nAwaited: " + wantedHash + "\nReceived: " + hash);
                }
            }
        } catch (final Exception e) {
            throw new IOException("Failed to execute JarJar", e);
        } finally {
            if (!rulesFile.delete()) {
                Pure.logger().warning("Failed to remove rules file after execution");
            }
        }

        Pure.logger().exiting(FileUtils.class.getName(), "relocateJarContent");
    }

    /**
     * Removes any file in the provided zip file whose path starts with
     * provided prefix.
     *
     * @param prefix the prefix
     * @param zip    the file
     *
     * @throws IOException if anything goes wrong
     */
    public static void removePrefixedBy(final String prefix, final Path zip) throws IOException {
        // Rename original file to tmp file
        final Path tmp = Paths.get(zip.toAbsolutePath().toString() + ".tmp");
        Files.move(zip, tmp, StandardCopyOption.REPLACE_EXISTING);

        // Rebuild original file from tmp file while filtering content
        try (
            final ZipInputStream in = new ZipInputStream(Files.newInputStream(tmp));
            final ZipOutputStream out = new ZipOutputStream(Files.newOutputStream(zip))
        ) {
            final byte[] buffer = new byte[1024];
            int read;
            ZipEntry entry;
            while ((entry = in.getNextEntry()) != null) {
                final String entryName = entry.getName();
                if (!entryName.startsWith(prefix)) {
                    // Create entry in new file
                    out.putNextEntry(new ZipEntry(entry));
                    // Copy content of entry to new file
                    while ((read = in.read(buffer)) > 0) {
                        out.write(buffer, 0, read);
                    }
                }
            }
        } finally {
            if (!tmp.toFile().delete()) {
                Pure.logger().warning("Failed to remove tmp file after execution");
            }
        }
    }
}
