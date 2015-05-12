package fr.ribesg.bukkit.pure.minecraft.r1_8;

import fr.ribesg.bukkit.pure.Pure;
import fr.ribesg.bukkit.pure.util.*;
import r1_8.net.minecraft.server.*;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.*;
import java.util.logging.Level;

import static org.bukkit.World.Environment;

/**
 * @author Ribesg
 * @author coelho
 */
public class ProxyChunkGenerator extends ChunkGenerator {

    static {
        // Required static constructor to initialize NMS classes
        try {
            // Bootstrap.alreadyRegistered = true;
            ReflectionUtils.set(od.class, null, "b", true);
        } catch (final ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
        atr.R(); // Block.registerBlocks();
        alq.t(); // Item.registerItems();
    }

    private final ProxyBlockPopulator blockPopulator;
    private final Environment         environment;
    private       boolean             nmsInitialized;

    private bfe nmsGenerator;   // IChunkProvider

    public ProxyChunkGenerator(final Environment environment) {
        this.blockPopulator = new ProxyBlockPopulator();
        this.environment = environment == null ? Environment.NORMAL : environment;
        this.nmsInitialized = false;
    }

    @SuppressWarnings("deprecation" )
    @Override
    public short[][] generateExtBlockSections(final World world, final Random random, final int x, final int z, final BiomeGrid biomes) {
        // Make sure that we initialize the NMS part first. Should be called once.
        if (!this.nmsInitialized && !this.initializeNms(world)) {
            return null;
        }

        /*
         * Generate or load a chunk using the NMS IChunkProvider and gets this chunk's sections.
         * - (bfh)             is the obfuscated class  name of Chunk
         * - (bfe.d(int, int)) is the obfuscated method name of IChunkProvider.loadChunk(int, int)
         * - (bfm)             is the obfuscated class  name of ExtendedBlockStorage
         * - (bfh.h())         is the obfuscated method name of ExtendedBlockStorage.getBlockStorageArray()
         */
        final bfh nmsChunk = this.nmsGenerator.d(x, z);
        final bfm[] nmsChunkSections = nmsChunk.h();

        /*
         * Copy biome-related data from the NMS Chunk to the Bukkit biome grid.
         * - (bfh.k())   is the obfuscated method name of Chunk.getBiomeArray()
         * - (arm)       is the obfuscated class  name of BiomeGenBase
         * - (arm.e(int) is the obfuscated method name of BiomeGenBase.getFromId(int)
         * - (arm.ah)    is the obfuscated field  name of BiomeGenBase.biomeName
         */
        final byte[] biomeBytes = nmsChunk.k();
        int i, j; // Reuse i later, don't redeclare j for each iteration
        for (i = 0; i < 16; i++) {
            for (j = 0; j < 16; j++) {
                biomes.setBiome(i, j, Biome.valueOf(BiomeUtils.translateBiomeName(arm.e(biomeBytes[(j << 4) | i] & 0xFF).ah)));
            }
        }

        /*
         * Convert generated chunk data into the format Bukkit wants: a short[][].
         * - (apz.i()) is the obfuscated method name of ExtendedBlockStorage.getBlockLSBArray()
         */
        final int maxHeight = world.getMaxHeight();
        final short[][] result = new short[maxHeight / 16][16 * 16 * 16];
        bfm nmsChunkSection;
        for (i = 0; i < result.length; i++) {
            nmsChunkSection = nmsChunkSections[i];
            if (nmsChunkSection == null) {
                continue;
            }
            final char[] idArray = nmsChunkSection.g();
            for (j = 0; j < idArray.length; j++) {
                result[i][j] = (byte) ((idArray[j] >> 4) & 0xFF);
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

    @SuppressWarnings("deprecation" )
    private boolean initializeNms(final World world) {
        try {
            /*
             * Create an instance of NmsProxyWorldServer then:
             * - Set field world             of NmsProxyWorldServer to the provided Bukkit world
             * - Set field rand (s)          of World               to a new Random instance
             * - Set field worldAccesses (u) of World               to a new ArrayList instance
             * - Set field worldBorder (M)   of World               to a new WorldBorder (bfb) instance
             * Notes:
             * - NmsProxyWorldServer extends WorldServer (qt) which extends World (aqu)
             */
            final aqu nmsWorld = ReflectionUtils.newInstance(NmsProxyWorldServer.class);
            ReflectionUtils.set(nmsWorld.getClass(), nmsWorld, "world", world);
            ReflectionUtils.set(nmsWorld.getClass().getSuperclass().getSuperclass(), nmsWorld, "s", new Random());
            ReflectionUtils.set(nmsWorld.getClass().getSuperclass().getSuperclass(), nmsWorld, "u", new ArrayList());
            ReflectionUtils.set(nmsWorld.getClass().getSuperclass().getSuperclass(), nmsWorld, "M", new bfb());

            /*
             * Create an instance of NmsDummyMapStorage then:
             * - Set field mapStorage (z) of World to the newly created dummy MapStorage
             * Notes:
             * - NmsDummyMapStorage extends MapStorage (brn)
             */
            final NmsDummyMapStorage mapStorage = ReflectionUtils.newInstance(NmsDummyMapStorage.class);
            ReflectionUtils.set(nmsWorld.getClass().getSuperclass().getSuperclass(), nmsWorld, "z", mapStorage);

            /*
             * Create an instance of WorldInfo then:
             * - Set field randomSeed (b)         of WorldInfo to the seed of our Bukkit world
             * - Set field terrainType (c)        of WorldInfo to the converted Bukkit world worldType
             * - Set field generatorOptions (d)   of WorldInfo to nothing // TODO
             * - Set field mapFeaturesEnabled (v) of WorldInfo to the canGenerateStructure parameter of our Bukkit world
             * - Set field difficulty (z)         of WorldInfo to the converted Bukkit difficulty
             * - Set field worldInfo (x)          of World     to the newly created WorldInfo instance
             * Notes:
             * - (bqo)           is the obfuscated class  name of WorldInfo
             * - (are)           is the obfuscated class  name of WorldType
             * - (are.a(String)) is the obfuscated method name of WorldType.parseWorldType(String)
             * - (vt)            is the obfuscated class  name of EnumDifficulty
             * - (vt.a(int))     is the obfuscated method name of EnumDifficulty.getDifficultyEnum(int)
             */
            final bqo nmsWorldData = ReflectionUtils.newInstance(bqo.class);
            ReflectionUtils.set(nmsWorldData.getClass(), nmsWorldData, "b", world.getSeed());
            ReflectionUtils.set(nmsWorldData.getClass(), nmsWorldData, "c", are.a(world.getWorldType().getName()));
            ReflectionUtils.set(nmsWorldData.getClass(), nmsWorldData, "d", "" ); // TODO Generator Options
            ReflectionUtils.set(nmsWorldData.getClass(), nmsWorldData, "v", world.canGenerateStructures());
            ReflectionUtils.set(nmsWorldData.getClass(), nmsWorldData, "z", vt.a(world.getDifficulty().getValue()));
            ReflectionUtils.set(nmsWorld.getClass().getSuperclass().getSuperclass(), nmsWorld, "x", nmsWorldData);

            /*
             * Create an instance of NmsDummyChunkProvider then:
             * - Set field chunkProvider (v) of World to the newly created dummy ChunkProvider instance
             * Notes:
             * - NmsDummyChunkProvider implements IChunkProvider (bfe)
             */
            final NmsDummyChunkProvider nmsChunkProvider = new NmsDummyChunkProvider();
            ReflectionUtils.set(nmsWorld.getClass().getSuperclass().getSuperclass(), nmsWorld, "v", nmsChunkProvider);

            /*
             * Gets a new WorldProvider matching the passed Environment then:
             * - Registers our World instance with the WorldProvider
             * - Set field provider (t) of World to the newly created WorldProvider instance
             * - Create a ChunkGenerator instance using the WorldProvider
             * Notes:
             * - (bgd)        is the obfuscated class  name of WorldProvider
             * - (bgd.a(int)) is the obfuscated method name of WorldProvider.getProviderForDimension(int)
             * - (bgd.a(aqu)) is the obfuscated method name of WorldProvider.registerWorld(World)
             * - (bgd.c())    is the obfuscated method name of WorldProvider.createChunkGenerator()
             */
            @SuppressWarnings("deprecation" )
            final bgd nmsWorldProvider = bgd.a(this.environment.getId());
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
