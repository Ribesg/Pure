package fr.ribesg.bukkit.pure.minecraft.r1_7_10;

import fr.ribesg.bukkit.pure.util.HashUtils;
import r1_7_10.net.minecraft.server.*;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

import java.util.*;

/**
 * @author Ribesg
 * @author coelho
 */
public class ProxyBlockPopulator extends BlockPopulator {

    private final Map<Long, apx> nmsChunks = new HashMap<>();   // Map<Long, Chunk>
    /*package */ apu                   nmsGenerator;            // IChunkProvider
    /*package */ ahb                   nmsWorld;                // World
    /*package */ NmsDummyChunkProvider nmsChunkProvider;

    public void addChunk(final apx chunk) {
        /*
         * (apx) is the obfuscated class name of Chunk, (apx.g) and (apx.h) obviously
         * represents the chunk's xPosition and zPosition.
         */
        this.nmsChunks.put(HashUtils.toLong(chunk.g, chunk.h), chunk);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void populate(final World world, final Random random, final Chunk chunk) {
        /*
         * Comments on the following obfuscated things:
         * - (apx)                  is the obfuscated class  name of Chunk
         * - (apu.c(int, int))      is the obfuscated method name of IChunkProvider.loadChunk(int, int)
         * - (apz)                  is the obfuscated class  name of ExtendedBlockStorage
         * - (apx.i())              is the obfuscated method name of Chunk.getBlockStorageArray()
         * - (apz.b(int, int, int)) is the obfuscated method name of ExtendedBlockStorage.getExtBlockMetadata(int, int, int)
         * - (apu.a(apu, int, int)) is the obfuscated method name of IChunkProvider.populate(IChunkProvider, int, int)
         */

        apx nmsChunk = this.nmsChunks.remove(HashUtils.toLong(chunk.getX(), chunk.getZ()));
        if (nmsChunk == null) {
            nmsChunk = this.nmsGenerator.c(chunk.getX(), chunk.getZ());
        }
        final apz[] nmsChunkSections = nmsChunk.i();
        apz nmsChunkSection;
        int i, x, y, z, meta;
        for (i = 0; i < nmsChunkSections.length; i++) {
            nmsChunkSection = nmsChunkSections[i];
            if (nmsChunkSection == null) {
                continue;
            }

            for (y = 0; y < 16; y++) {
                for (x = 0; x < 16; x++) {
                    for (z = 0; z < 16; z++) {
                        meta = nmsChunkSection.b(x, y, z);
                        if (meta == 0) {
                            continue;
                        }
                        chunk.getBlock(x, (i * 16) + y, z).setData((byte) meta);
                    }
                }
            }
        }
        this.nmsChunkProvider.chunk = nmsChunk;
        this.nmsGenerator.a(null, chunk.getX(), chunk.getZ());
        this.nmsChunkProvider.chunk = null;
    }
}
