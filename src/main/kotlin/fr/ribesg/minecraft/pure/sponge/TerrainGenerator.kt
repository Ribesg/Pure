package fr.ribesg.minecraft.pure.sponge

import fr.ribesg.minecraft.pure.common.Log
import fr.ribesg.minecraft.pure.common.MCVersion
import org.bukkit.World.Environment
import org.bukkit.generator.ChunkGenerator
import org.spongepowered.api.util.gen.BiomeBuffer
import org.spongepowered.api.util.gen.MutableBlockBuffer
import org.spongepowered.api.world.World
import org.spongepowered.api.world.gen.GeneratorPopulator

/**
 * @author Ribesg
 */
internal class TerrainGenerator(env: Environment, version: MCVersion) : GeneratorPopulator {

    private val bukkitGenerator: ChunkGenerator

    init {
        this.bukkitGenerator = version.getChunkGenerator(env)
    }

    override fun populate(world: World?, buffer: MutableBlockBuffer?, biomes: BiomeBuffer?) {
        Log.info("Populating:")
        Log.info("\tMin: ${buffer?.blockMin}")
        Log.info("\tMax: ${buffer?.blockMax}")
        Log.info("\tSize: ${buffer?.blockSize}")
    }

}
