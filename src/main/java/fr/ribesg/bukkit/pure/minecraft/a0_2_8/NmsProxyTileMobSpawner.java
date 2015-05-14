package fr.ribesg.bukkit.pure.minecraft.a0_2_8;

import a0_2_8.net.minecraft.server.cf;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;

/**
 * @author coelho
 * @author Ribesg
 */
public class NmsProxyTileMobSpawner extends cf implements Runnable {

    private final CreatureSpawner creatureSpawner;

    public NmsProxyTileMobSpawner(final CreatureSpawner creatureSpawner) {
        this.creatureSpawner = creatureSpawner;
    }

    public void run() {
        this.creatureSpawner.setSpawnedType(EntityType.valueOf(this.f.toUpperCase()));
    }
}
