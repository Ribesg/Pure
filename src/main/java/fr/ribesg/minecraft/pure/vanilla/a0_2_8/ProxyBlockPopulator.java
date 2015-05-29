package fr.ribesg.minecraft.pure.vanilla.a0_2_8;

import a0_2_8.net.minecraft.server.*;
import fr.ribesg.minecraft.pure.util.HashUtils;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

import java.util.*;

/**
 * @author Ribesg
 * @author coelho
 */
public class ProxyBlockPopulator extends BlockPopulator {

    private final Map<Long, ju> nmsChunks = new HashMap<>();   // Map<Long, Chunk>
    /*package */ bu                    nmsGenerator;            // IChunkProvider
    /*package */ eq                    nmsWorld;                // World
    /*package */ NmsDummyChunkProvider nmsChunkProvider;

    public void addChunk(final ju chunk) {
        /*
         * (ju) is the obfuscated class name of Chunk, (ju.j) and (ju.k) obviously
         * represents the chunk's xPosition and zPosition.
         */
        this.nmsChunks.put(HashUtils.toLong(chunk.j, chunk.k), chunk);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void populate(final World world, final Random random, final Chunk chunk) {
        /*
         * Comments on the following obfuscated things:
         * - (ju)                  is the obfuscated class  name of Chunk
         * - (bu.b(int, int))      is the obfuscated method name of IChunkProvider.loadChunk(int, int)
         * - (ju.e)                is the obfuscated field  name of ??? FIXME
         * Note: (ju.e) is of type (ht).
         * - (ht)                  is the obfuscated class  name of ??? FIXME
         * - (ht.a(int, int, int)) is the obfuscated method name of ??? FIXME
         * - (bu.a(bu, int, int))  is the obfuscated method name of IChunkProvider.populate(IChunkProvider, int, int)
         */

        ju nmsChunk = this.nmsChunks.remove(HashUtils.toLong(chunk.getX(), chunk.getZ()));
        if (nmsChunk == null) {
            nmsChunk = this.nmsGenerator.b(chunk.getX(), chunk.getZ());
        }
        int x, y, z, meta;
        for (y = 0; y < 128; y++) {
            for (x = 0; x < 16; x++) {
                for (z = 0; z < 16; z++) {
                    meta = nmsChunk.e.a(x, y, z);
                    if (meta == 0) {
                        continue;
                    }
                    chunk.getBlock(x, y, z).setData((byte) meta);
                }
            }
        }
        this.nmsChunkProvider.chunk = nmsChunk;
        this.nmsGenerator.a(null, chunk.getX(), chunk.getZ());
        ((Runnable) this.nmsWorld).run();
        this.nmsChunkProvider.chunk = null;
    }
}
