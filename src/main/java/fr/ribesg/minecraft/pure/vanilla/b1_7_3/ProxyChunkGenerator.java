package fr.ribesg.minecraft.pure.vanilla.b1_7_3;

import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author Ribesg
 */
public class ProxyChunkGenerator extends ChunkGenerator {

    static {
        // TODO Init Item and Block registers if needed
    }

    private final ProxyBlockPopulator blockPopulator;
    private final Environment         environment;
    private       boolean             nmsInitialized;

    private Object nmsGenerator;

    public ProxyChunkGenerator(final Environment environment) {
        this.blockPopulator = new ProxyBlockPopulator();
        this.environment = environment == null ? Environment.NORMAL : environment;
        this.nmsInitialized = false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public short[][] generateExtBlockSections(final World world, final Random random, final int x, final int z, final BiomeGrid biomes) {
        return null; // TODO
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(final World world) {
        if (!this.nmsInitialized && !this.initializeNms(world)) {
            return null;
        }
        return Collections.singletonList((BlockPopulator) this.blockPopulator);
    }

    private boolean initializeNms(final World world) {
        return false; // TODO
    }
}
