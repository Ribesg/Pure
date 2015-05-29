package fr.ribesg.minecraft.pure.vanilla.r1_6_4;

import fr.ribesg.minecraft.pure.util.HashUtils;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import r1_6_4.net.minecraft.server.*;

import java.util.*;

/**
 * @author Ribesg
 */
public class ProxyBlockPopulator extends BlockPopulator {

    private final Map<Long, adr> nmsChunks = new HashMap<>();   // Map<Long, Chunk>
    /*package */ ado                   nmsGenerator;            // IChunkProvider
    /*package */ abw                   nmsWorld;                // World
    /*package */ NmsDummyChunkProvider nmsChunkProvider;

    public void addChunk(final adr chunk) {
        /*
         * (adr) is the obfuscated class name of Chunk, (adr.g) and (adr.h) obviously
         * represents the chunk's xPosition and zPosition.
         */
        this.nmsChunks.put(HashUtils.toLong(chunk.g, chunk.h), chunk);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void populate(final World world, final Random random, final Chunk chunk) {
        /*
         * Comments on the following obfuscated things:
         * - (adr)                  is the obfuscated class  name of Chunk
         * - (ado.c(int, int))      is the obfuscated method name of IChunkProvider.loadChunk(int, int)
         * - (ads)                  is the obfuscated class  name of ExtendedBlockStorage
         * - (adr.i())              is the obfuscated method name of Chunk.getBlockStorageArray()
         * - (ads.b(int, int, int)) is the obfuscated method name of ExtendedBlockStorage.getExtBlockMetadata(int, int, int)
         * - (ado.a(ado, int, int)) is the obfuscated method name of IChunkProvider.populate(IChunkProvider, int, int)
         */

        adr nmsChunk = this.nmsChunks.remove(HashUtils.toLong(chunk.getX(), chunk.getZ()));
        if (nmsChunk == null) {
            nmsChunk = this.nmsGenerator.c(chunk.getX(), chunk.getZ());
        }
        final ads[] nmsChunkSections = nmsChunk.i();
        ads nmsChunkSection;
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
