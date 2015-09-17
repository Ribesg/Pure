package fr.ribesg.minecraft.pure.sponge

import com.google.inject.Inject
import fr.ribesg.minecraft.pure.PomData
import fr.ribesg.minecraft.pure.common.Log
import fr.ribesg.minecraft.pure.common.MCVersion
import org.bukkit.World.Environment
import org.slf4j.Logger
import org.spongepowered.api.data.DataContainer
import org.spongepowered.api.event.Subscribe
import org.spongepowered.api.event.world.WorldLoadEvent
import org.spongepowered.api.plugin.Plugin
import org.spongepowered.api.world.WorldCreationSettings
import org.spongepowered.api.world.gen.WorldGenerator
import org.spongepowered.api.world.gen.WorldGeneratorModifier

/**
 * @author Ribesg
 */
@Plugin(id = PomData.ARTIFACT_ID, name = PomData.NAME, version = PomData.VERSION)
object Pure {

    @Inject
    private fun setupLogger(logger: Logger) {
        Log.initSlf4jLogger(logger)
    }

    @Subscribe
    internal fun onWorldLoad(event: WorldLoadEvent) {
        Log.info("Loading world ${event.world.name}")
        val testGen = object : WorldGeneratorModifier {
            override fun modifyWorldGenerator(world: WorldCreationSettings?, settings: DataContainer?, generator: WorldGenerator?) {
                generator?.generatorPopulators?.clear()
                generator?.populators?.clear()
                generator?.baseGeneratorPopulator = TerrainGenerator(Environment.NORMAL, MCVersion.R1_8)
            }

            override fun getId(): String? = "pure"

            override fun getName(): String = "Pure"
        }

        val world = event.world
        val generator = world.worldGenerator
        testGen.modifyWorldGenerator(null, null, generator)
        world.worldGenerator = generator
    }

}
