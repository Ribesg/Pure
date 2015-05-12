package fr.ribesg.pure.file;

import fr.ribesg.bukkit.pure.util.HashUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Tests for FileUtils.
 *
 * @author Ribesg
 */
public class FileUtilsTest {

    @Test
    public void testHash() {
        final String input = "Hello World!";
        final String output = "7F83B1657FF1FC53B92DC18148A1D65DFC2D4B1FA3D677284ADDD200126D9069";

        try {
            final Path file = Files.createTempFile("testHash", null);
            Files.write(file, input.getBytes());
            Assert.assertEquals("Hash is incorrect", output, HashUtils.hashSha256(file));
            Files.delete(file);
        } catch (final IOException e) {
            Assert.fail("Failed to create temporary file");
        }
    }
}
