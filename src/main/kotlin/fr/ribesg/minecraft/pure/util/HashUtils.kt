package fr.ribesg.minecraft.pure.util

import java.io.RandomAccessFile
import java.nio.file.Path
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Used to hash things.
 *
 * @author Ribesg
 * @author coelho
 */
object HashUtils {

    /**
     * Buffer for file reading. Set to 1 Mo.
     */
    private val BUFFER_SIZE = 1024 * 1024 * 1024

    /**
     * An array of hexadecimal characters.
     */
    private val HEXADECIMAL_CHARACTERS = "0123456789ABCDEF".toCharArray()

    /**
     * Hashes a file using the SHA-256 algorithm.
     *
     * @param filePath a file
     */
    fun hashSha256(filePath: Path): String {
        RandomAccessFile(filePath.toFile(), "r").use { file ->
            val digest = try {
                MessageDigest.getInstance("SHA-256")
            } catch (e: NoSuchAlgorithmException) {
                throw RuntimeException("Unable to encrypt using SHA-256", e)
            }
            val buffer = ByteArray(BUFFER_SIZE)
            var lastIndex = 0L
            var endIndex = file.length()
            var readSize: Int
            while (lastIndex < endIndex) {
                readSize = if ((endIndex - lastIndex) >= BUFFER_SIZE) BUFFER_SIZE else (endIndex - lastIndex).toInt()
                file.read(buffer, 0, readSize)
                digest.update(buffer, 0, readSize)
                lastIndex += readSize
            }

            return HashUtils.bytesToHex(digest.digest())
        }
    }

    /**
     * Converts a byte array to an hexadecimal representation of it.
     *
     * @param bytes a byte array
     *
     * @return a String representing the byte array
     */
    private fun bytesToHex(bytes: ByteArray): String {
        val result = CharArray(bytes.size() * 2)
        for (j in bytes.indices) {
            val v = bytes[j].toInt() and 0xFF
            result[j * 2] = HEXADECIMAL_CHARACTERS[v ushr 4]
            result[j * 2 + 1] = HEXADECIMAL_CHARACTERS[v and 0x0F]
        }
        return String(result)
    }

    /**
     * Builds a long hash from two integers.
     *
     * @param a an int
     * @param b another int
     *
     * @return a hash built from the two provided integers
     */
    @JvmStatic
    fun toLong(a: Int, b: Int): Long = (a shl 32).toLong() + b - Integer.MAX_VALUE

}
