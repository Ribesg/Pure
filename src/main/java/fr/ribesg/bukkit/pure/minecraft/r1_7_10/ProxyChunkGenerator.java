package fr.ribesg.bukkit.pure.minecraft.r1_7_10;

import fr.ribesg.bukkit.pure.Pure;
import fr.ribesg.bukkit.pure.util.BiomeUtils;
import fr.ribesg.bukkit.pure.util.ReflectionUtils;
import r1_7_10.net.minecraft.server.*;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.*;
import java.util.logging.Level;

/**
 * @author Ribesg
 * @author coelho
 */
public class ProxyChunkGenerator extends ChunkGenerator {

    static {
        // Required static constructor to initialize NMS classes
        aji.p(); // Block.registerBlocks();
        adb.l(); // Item.registerItems();
    }

    private final ProxyBlockPopulator blockPopulator;
    private final Environment         environment;
    private       boolean             nmsInitialized;

    private apu nmsGenerator;   // IChunkProvider

    public ProxyChunkGenerator(final Environment environment) {
        this.blockPopulator = new ProxyBlockPopulator();
        this.environment = environment == null ? Environment.NORMAL : environment;
        this.nmsInitialized = false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public short[][] generateExtBlockSections(final World world, final Random random, final int x, final int z, final BiomeGrid biomes) {
        // Make sure that we initialize the NMS part first. Should be called once.
        if (!this.nmsInitialized && !this.initializeNms(world)) {
            return null;
        }

        /*
         * Generate or load a chunk using the NMS IChunkProvider and gets this chunk's sections.
         * - (apx)             is the obfuscated class  name of Chunk
         * - (apu.c(int, int)) is the obfuscated method name of IChunkProvider.loadChunk(int, int)
         * - (apz)             is the obfuscated class  name of ExtendedBlockStorage
         * - (apx.i())         is the obfuscated method name of Chunk.getBlockStorageArray()
         */
        final apx nmsChunk = this.nmsGenerator.c(x, z);
        final apz[] nmsChunkSections = nmsChunk.i();

        /*
         * Copy biome-related data from the NMS Chunk to the Bukkit biome grid.
         * - (apx.m())   is the obfuscated method name of Chunk.getBiomeArray()
         * - (ahu)       is the obfuscated class  name of BiomeGenBase
         * - (ahu.d(int) is the obfuscated method name of BiomeGenBase.getFromId(int)
         * - (ahu.af)    is the obfuscated field  name of BiomeGenBase.biomeName
         */
        final byte[] biomeBytes = nmsChunk.m();
        int i, j; // Reuse i later, don't redeclare j for each iteration
        for (i = 0; i < 16; i++) {
            for (j = 0; j < 16; j++) {
                biomes.setBiome(i, j, Biome.valueOf(BiomeUtils.translateBiomeName(ahu.d(biomeBytes[(j << 4) | i] & 0xFF).af)));
            }
        }

        /*
         * Convert generated chunk data into the format Bukkit wants: a short[][].
         * - (apz.i()) is the obfuscated method name of ExtendedBlockStorage.getBlockLSBArray()
         */
        final int maxHeight = world.getMaxHeight();
        final short[][] result = new short[maxHeight / 16][16 * 16 * 16];
        apz nmsChunkSection;
        for (i = 0; i < result.length; i++) {
            nmsChunkSection = nmsChunkSections[i];
            if (nmsChunkSection == null) {
                continue;
            }
            final byte[] idArray = nmsChunkSection.g();
            for (j = 0; j < idArray.length; j++) {
                result[i][j] = idArray[j];
            }
        }

        // Pass the chunk to our BlockPopulator
        this.blockPopulator.addChunk(nmsChunk);

        return result;
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
             * - Set field rand (s)          of World               to a new Random instance
             * - Set field worldAccesses (u) of World               to a new ArrayList instance
             * Notes:
             * - NmsProxyWorldServer extends WorldServer (mt) which extends World (ahb)
             */
            final ahb nmsWorld = ReflectionUtils.newInstance(NmsProxyWorldServer.class);
            ReflectionUtils.set(nmsWorld.getClass(), nmsWorld, "world", world);
            ReflectionUtils.set(nmsWorld.getClass().getSuperclass().getSuperclass(), nmsWorld, "s", new Random());
            ReflectionUtils.set(nmsWorld.getClass().getSuperclass().getSuperclass(), nmsWorld, "u", new ArrayList());

            /*
             * Create an instance of NmsDummyMapStorage then:
             * - Set field mapStorage (z) of World to the newly created dummy MapStorage
             * Notes:
             * - NmsDummyMapStorage extends MapStorage (azq)
             */
            final NmsDummyMapStorage mapStorage = ReflectionUtils.newInstance(NmsDummyMapStorage.class);
            ReflectionUtils.set(nmsWorld.getClass().getSuperclass().getSuperclass(), nmsWorld, "z", mapStorage);

            /*
             * Create an instance of WorldInfo then:
             * - Set field randomSeed (a)         of WorldInfo to the seed of our Bukkit world
             * - Set field terrainType (b)        of WorldInfo to the converted Bukkit world worldType
             * - Set field mapFeaturesEnabled (t) of WorldInfo to the canGenerateStructure parameter of our Bukkit world
             * - Set field worldInfo (x)          of World     to the newly created WorldInfo instance
             * Notes:
             * - (ays)           is the obfuscated class  name of WorldInfo
             * - (ahm)           is the obfuscated class  name of WorldType
             * - (ahm.a(String)) is the obfuscated method name of WorldType.parseWorldType(String)
             */
            final ays nmsWorldData = ReflectionUtils.newInstance(ays.class);
            ReflectionUtils.set(nmsWorldData.getClass(), nmsWorldData, "a", world.getSeed());
            ReflectionUtils.set(nmsWorldData.getClass(), nmsWorldData, "b", ahm.a(world.getWorldType().getName()));
            ReflectionUtils.set(nmsWorldData.getClass(), nmsWorldData, "t", world.canGenerateStructures());
            ReflectionUtils.set(nmsWorld.getClass().getSuperclass().getSuperclass(), nmsWorld, "x", nmsWorldData);

            /*
             * Create an instance of NmsDummyChunkProvider then:
             * - Set field chunkProvider (v) of World to the newly created dummy ChunkProvider instance
             * Notes:
             * - NmsDummyChunkProvider implements IChunkProvider (apu)
             */
            final NmsDummyChunkProvider nmsChunkProvider = new NmsDummyChunkProvider();
            ReflectionUtils.set(nmsWorld.getClass().getSuperclass().getSuperclass(), nmsWorld, "v", nmsChunkProvider);

            /*
             * Gets a new WorldProvider matching the passed Environment then:
             * - Registers our World instance with the WorldProvider
             * - Set field provider (t) of World to the newly created WorldProvider instance
             * - Create a ChunkGenerator instance using the WorldProvider
             * Notes:
             * - (aqo)        is the obfuscated class  name of WorldProvider
             * - (aqo.a(int)) is the obfuscated method name of WorldProvider.getProviderForDimension(int)
             * - (aqo.a(ahb)) is the obfuscated method name of WorldProvider.registerWorld(World)
             * - (aqo.c())    is the obfuscated method name of WorldProvider.createChunkGenerator()
             */
            @SuppressWarnings("deprecation")
            final aqo nmsWorldProvider = aqo.a(this.environment.getId());
            nmsWorldProvider.a(nmsWorld);
            ReflectionUtils.set(nmsWorld.getClass().getSuperclass().getSuperclass(), nmsWorld, "t", nmsWorldProvider);
            this.nmsGenerator = nmsWorldProvider.c();

            // Here wa "transfer" some of the NMS objects we created to our BlockPopulator
            this.blockPopulator.nmsGenerator = this.nmsGenerator;
            this.blockPopulator.nmsWorld = nmsWorld;
            this.blockPopulator.nmsChunkProvider = nmsChunkProvider;
        } catch (final ReflectiveOperationException e) {
            Pure.logger().log(Level.SEVERE, "Error while initializing ProxyChunkGenerator", e);
            return false;
        }
        this.nmsInitialized = true;
        return true;
    }
}
