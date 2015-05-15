package fr.ribesg.bukkit.pure.minecraft.r1_6_4;

import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import r1_6_4.net.minecraft.server.*;

/**
 * @author coelho
 * @author Ribesg
 */
public class NmsProxyTileMobSpawner extends asj {

    private final CreatureSpawner creatureSpawner;

    public NmsProxyTileMobSpawner(final CreatureSpawner creatureSpawner) {
        this.creatureSpawner = creatureSpawner;
    }

    @Override
    public abn a() {
        return new NmsProxyMobSpawner();
    }

    public class NmsProxyMobSpawner extends abn {

        public abw a() {
            return null; // NOP
        }

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

        public int b() {
            return 0; // NOP
        }

        public int c() {
            return 0; // NOP
        }

        public int d() {
            return 0; // NOP
        }
    }
}
