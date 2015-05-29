package fr.ribesg.minecraft.pure.sponge

import com.google.inject.Inject
import fr.ribesg.minecraft.pure.common.Log
import org.slf4j.Logger
import org.spongepowered.api.event.Subscribe
import org.spongepowered.api.event.state.PreInitializationEvent
import org.spongepowered.api.plugin.Plugin
import kotlin.properties.Delegates

/**
 * @author Ribesg
 */
Plugin(id = "Pure", name = "Pure", version = "maven-version-number")
public object Pure {

    Inject private val logger: Logger by Delegates.notNull()

    Subscribe fun onPreInit(event: PreInitializationEvent) {
        Log.initSlf4jLogger(this.logger)
    }

    // TODO Everything

}
