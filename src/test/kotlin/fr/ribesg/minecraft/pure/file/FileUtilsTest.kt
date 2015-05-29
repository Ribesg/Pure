package fr.ribesg.minecraft.pure.file

import fr.ribesg.minecraft.pure.util.HashUtils
import org.junit.Assert
import org.junit.Test
import java.io.IOException
import java.nio.file.Files

/**
 * Tests for FileUtils.
 *
 * @author Ribesg
 */
public final class FileUtilsTest {

    Test
    public fun testHash() {
        val input = "Hello World!"
        val output = "7F83B1657FF1FC53B92DC18148A1D65DFC2D4B1FA3D677284ADDD200126D9069"

        try {
            val file = Files.createTempFile("testHash", null)
            Files.write(file, input.toByteArray())
            Assert.assertEquals("Hash is incorrect", output, HashUtils.hashSha256(file))
            Files.delete(file)
        } catch (e: IOException) {
            Assert.fail("Failed to create temporary file")
        }
    }

}
