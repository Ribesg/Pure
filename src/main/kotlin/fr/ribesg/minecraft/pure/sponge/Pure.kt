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
Plugin(id = PomData.ARTIFACT_ID, name = PomData.NAME, version = PomData.VERSION)
public object Pure {

    Inject private fun setupLogger(logger: Logger) {
        Log.initSlf4jLogger(logger)
    }

    Subscribe fun onWorldLoad(event: WorldLoadEvent) {
        Log.info("Loading world ${event.getWorld().getName()}")
        val testGen = object : WorldGeneratorModifier {
            override fun modifyWorldGenerator(world: WorldCreationSettings?, settings: DataContainer?, generator: WorldGenerator?) {
                generator?.getGeneratorPopulators()?.clear()
                generator?.getPopulators()?.clear()
                generator?.setBaseGeneratorPopulator(TerrainGenerator(Environment.NORMAL, MCVersion.R1_8))
            }

            override fun getId(): String? = "pure"

            override fun getName(): String = "Pure"
        }

        val world = event.getWorld()
        val generator = world.getWorldGenerator()
        testGen.modifyWorldGenerator(null, null, generator)
        world.setWorldGenerator(generator)
    }

}
