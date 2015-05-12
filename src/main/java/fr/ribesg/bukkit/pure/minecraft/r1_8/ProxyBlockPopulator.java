package fr.ribesg.bukkit.pure.minecraft.r1_8;

import fr.ribesg.bukkit.pure.util.HashUtils;
import r1_8.net.minecraft.server.*;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

import java.util.*;

/**
 * @author Ribesg
 * @author coelho
 */
public class ProxyBlockPopulator extends BlockPopulator {

    private final Map<Long, bfh> nmsChunks = new HashMap<>();
    /*package */ bfe                   nmsGenerator;            // IChunkProvider
    /*package */ aqu                   nmsWorld;                // World
    /*package */ NmsDummyChunkProvider nmsChunkProvider;

    public void addChunk(final bfh chunk) {
        /*
         * (bfh) is the obfuscated class name of Chunk, (bfh.a) and (bfh.b) obviously
         * represents the chunk's xPosition and zPosition.
         */
        this.nmsChunks.put(HashUtils.toLong(chunk.a, chunk.b), chunk);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void populate(final World world, final Random random, final Chunk chunk) {
        /*
         * Comments on the following obfuscated things:
         * - (bfh)                  is the obfuscated class  name of Chunk
         * - (bfe.d(int, int))      is the obfuscated method name of IChunkProvider.loadChunk(int, int)
         * - (bfm)                  is the obfuscated class  name of ExtendedBlockStorage
         * - (bfh.h())              is the obfuscated method name of Chunk.getBlockStorageArray()
         * - (bfm.g())              is the obfuscated method name of ExtendedBlockStorage.getData()
         * - (bfe.a(bfe, int, int)) is the obfuscated method name of IChunkProvider.populate(IChunkProvider, int, int)
         */

        bfh nmsChunk = this.nmsChunks.remove(HashUtils.toLong(chunk.getX(), chunk.getZ()));
        if (nmsChunk == null) {
            nmsChunk = this.nmsGenerator.d(chunk.getX(), chunk.getZ());
        }
        final bfm[] nmsChunkSections = nmsChunk.h();
        bfm nmsChunkSection;
        int i, x, y, z, meta;
        char[] ids;
        for (i = 0; i < nmsChunkSections.length; i++) {
            nmsChunkSection = nmsChunkSections[i];
            if (nmsChunkSection == null) {
                continue;
            }

            ids = nmsChunkSection.g();
            for (y = 0; y < 16; y++) {
                for (x = 0; x < 16; x++) {
                    for (z = 0; z < 16; z++) {
                        meta = ids[(y << 8 | z << 4 | x)] & 0xF;
                        if (meta == 0) {
                            continue;
                        }
                        chunk.getBlock(x, (i * 16) + y, z).setData((byte) meta);
                    }
                }
            }
        }
        this.nmsChunkProvider.chunk = nmsChunk;
        this.nmsGenerator.a((bfe) null, chunk.getX(), chunk.getZ());
        this.nmsChunkProvider.chunk = null;
    }
}
