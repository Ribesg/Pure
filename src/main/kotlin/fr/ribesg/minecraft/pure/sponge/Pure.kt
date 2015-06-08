package fr.ribesg.minecraft.pure.sponge

import com.google.inject.Inject
import fr.ribesg.minecraft.pure.PomData
import fr.ribesg.minecraft.pure.common.Log
import org.slf4j.Logger
import org.spongepowered.api.plugin.Plugin

/**
 * @author Ribesg
 */
Plugin(id = PomData.ARTIFACT_ID, name = PomData.NAME, version = PomData.VERSION)
public object Pure {

    Inject private fun setupLogger(logger: Logger) {
        Log.initSlf4jLogger(logger)
    }

    // TODO Everything

}
