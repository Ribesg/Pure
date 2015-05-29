package fr.ribesg.minecraft.pure.vanilla.a0_2_8;

import a0_2_8.net.minecraft.server.*;
import fr.ribesg.minecraft.pure.common.Log;
import fr.ribesg.minecraft.pure.util.ReflectionUtils;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.*;

/**
 * @author Ribesg
 * @author coelho
 */
public class ProxyChunkGenerator extends ChunkGenerator {

    private final ProxyBlockPopulator blockPopulator;
    private final Environment         environment;
    private       boolean             nmsInitialized;

    private bu nmsGenerator;   // IChunkProvider

    public ProxyChunkGenerator(final Environment environment) {
        this.blockPopulator = new ProxyBlockPopulator();
        this.environment = environment == null ? Environment.NORMAL : environment;
        this.nmsInitialized = false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public byte[] generate(final World world, final Random random, final int x, final int z) {
        // Make sure that we initialize the NMS part first. Should be called once.
        if (!this.nmsInitialized && !this.initializeNms(world)) {
            return null;
        }

        /*
         * Generate or load a chunk using the NMS IChunkProvider and gets this chunk's sections.
         * - (ju)             is the obfuscated class  name of Chunk
         * - (ju.b(int, int)) is the obfuscated method name of IChunkProvider.loadChunk(int, int)
         */
        final ju nmsChunk = this.nmsGenerator.b(x, z);

        // Pass the chunk to our BlockPopulator
        this.blockPopulator.addChunk(nmsChunk);

        /*
         * - (ju.b) is the obfuscated field name of Chunk.data
         */
        return nmsChunk.b;
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(final World world) {
        if (!this.nmsInitialized && !this.initializeNms(world)) {
            return null;
        }
        return Collections.singletonList((BlockPopulator) this.blockPopulator);
    }

    private boolean initializeNms(final World world) {
        try {
            /*
             * Create an instance of NmsProxyWorldServer then:
             * - Set field world             of NmsProxyWorldServer to the provided Bukkit world
             * - Set field seed (u)          of World               to the seed of our Bukkit world
             * - Set field rand (l)          of World               to a new Random instance
             * - Set field ??? (A)           of World               to a new ArrayList instance
             * - Set field ??? (r)           of World               to a new ArrayList instance
             * - Set field ??? (D)           of World               to a new TreeSet instance
             * - Set field ??? (C)           of World               to a new TreeSet instance
             * Notes:
             * - NmsProxyWorldServer extends WorldServer (mt) which extends World (ahb)
             */
            final eq nmsWorld = ReflectionUtils.newInstance(NmsProxyWorldServer.class);
            ReflectionUtils.set(nmsWorld.getClass(), nmsWorld, "world", world);
            ReflectionUtils.set(nmsWorld.getClass().getSuperclass().getSuperclass(), nmsWorld, "u", world.getSeed());
            ReflectionUtils.set(nmsWorld.getClass().getSuperclass().getSuperclass(), nmsWorld, "l", new Random());
            ReflectionUtils.set(nmsWorld.getClass().getSuperclass().getSuperclass(), nmsWorld, "A", new ArrayList());
            ReflectionUtils.set(nmsWorld.getClass().getSuperclass().getSuperclass(), nmsWorld, "r", new ArrayList());
            ReflectionUtils.set(nmsWorld.getClass().getSuperclass().getSuperclass(), nmsWorld, "D", new TreeSet());
            ReflectionUtils.set(nmsWorld.getClass().getSuperclass().getSuperclass(), nmsWorld, "C", new TreeSet());

            /*
             * Create an instance of NmsDummyChunkProvider then:
             * - Set field chunkProvider (G) of World to the newly created dummy ChunkProvider instance
             * Notes:
             * - NmsDummyChunkProvider implements IChunkProvider (bu)
             */
            final NmsDummyChunkProvider nmsChunkProvider = new NmsDummyChunkProvider();
            ReflectionUtils.set(nmsWorld.getClass().getSuperclass().getSuperclass(), nmsWorld, "G", nmsChunkProvider);

            /*
             * Gets a new WorldProvider matching the passed Environment then:
             * - Registers our World instance with the WorldProvider
             * - Set field provider (q) of World to the newly created WorldProvider instance
             * - Create a ChunkGenerator instance using the WorldProvider
             * Notes:
             * - (il)        is the obfuscated class  name of WorldProvider
             * - (il.a(int)) is the obfuscated method name of WorldProvider.getProviderForDimension(int)
             * - (il.a(eq))  is the obfuscated method name of WorldProvider.registerWorld(World)
             * - (il.c())    is the obfuscated method name of WorldProvider.createChunkGenerator()
             */
            @SuppressWarnings("deprecation")
            final il nmsWorldProvider = il.a(this.environment.getId());
            nmsWorldProvider.a(nmsWorld);
            ReflectionUtils.set(nmsWorld.getClass().getSuperclass().getSuperclass(), nmsWorld, "q", nmsWorldProvider);
            this.nmsGenerator = nmsWorldProvider.c();

            // Here wa "transfer" some of the NMS objects we created to our BlockPopulator
            this.blockPopulator.nmsGenerator = this.nmsGenerator;
            this.blockPopulator.nmsWorld = nmsWorld;
            this.blockPopulator.nmsChunkProvider = nmsChunkProvider;
        } catch (final ReflectiveOperationException e) {
            Log.error("Error while initializing ProxyChunkGenerator", e);
            return false;
        }
        this.nmsInitialized = true;
        return true;
    }
}
