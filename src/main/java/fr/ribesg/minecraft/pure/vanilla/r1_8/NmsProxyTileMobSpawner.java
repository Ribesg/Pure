package fr.ribesg.minecraft.pure.vanilla.r1_8;

import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import r1_8.net.minecraft.server.*;

/**
 * @author coelho
 * @author Ribesg
 */
public class NmsProxyTileMobSpawner extends bdg {

    private final CreatureSpawner creatureSpawner;

    public NmsProxyTileMobSpawner(final CreatureSpawner creatureSpawner) {
        this.creatureSpawner = creatureSpawner;
    }

    @Override
    public aqi b() {
        return new NMSProxyMobSpawner();
    }

    public class NMSProxyMobSpawner extends aqi {

        @Override
        public aqu a() {
            return null; // NOP
        }

        @Override
        public void a(final int arg0) {
            // NOP
        }

        @Override
        public void a(String mob) {
            mob = mob.toUpperCase();
            if (mob.equals("CAVESPIDER")) {
                mob = "CAVE_SPIDER";
            }
            NmsProxyTileMobSpawner.this.creatureSpawner.setSpawnedType(EntityType.valueOf(mob));
        }

        @Override
        public dt b() {
            return null; // NOP
        }
    }
}
