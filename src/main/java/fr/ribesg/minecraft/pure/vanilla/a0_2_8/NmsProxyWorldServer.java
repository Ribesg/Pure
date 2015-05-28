package fr.ribesg.minecraft.pure.vanilla.a0_2_8;

import a0_2_8.net.minecraft.server.*;
import fr.ribesg.bukkit.pure.log.Log;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.*;

import java.util.LinkedList;
import java.util.List;

/**
 * @author coelho
 * @author Ribesg
 */
public class NmsProxyWorldServer extends ex implements Runnable {

    @SuppressWarnings("all") // No warning name for "variable is never assigned"
    private World          world;
    private List<Runnable> queue;

    /*
     * The second and third arguments of the super constructor seems to be
     * @NotNull.
     * There is no @SuppressWarnings rule specifically for this, so all it is.
     */
    @SuppressWarnings("all")
    public NmsProxyWorldServer() {
        super(null, null, null, 0);
    }

    @Override
    public int b(final int x, final int y, final int z) {
        return this.a(x, y, z);
    }

    @SuppressWarnings("deprecation")
    @Override
    public int a(final int x, final int y, final int z) {
        return this.world.getBlockAt(x, y, z).getTypeId();
    }

    @Override
    public boolean a(final int x, final int y, final int z, final int type) {
        return this.a(x, y, z, type, 0);
    }

    @Override
    public boolean c(final int x, final int y, final int z, final int type) {
        return this.a(x, y, z, type, 0);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean a(final int x, final int y, final int z, final int type, final int meta) {
        final Block block = this.world.getBlockAt(x, y, z);
        block.setTypeId(type);
        block.setData((byte) meta);
        return true;
    }

    @Override
    public ay k(final int x, final int y, final int z) {
        final Block block = this.world.getBlockAt(x, y, z);
        if (block.getType() == Material.AIR) {
            return null;
        }
        final BlockState blockState = block.getState();
        if (blockState == null) {
            return null;
        }
        if (blockState instanceof CreatureSpawner) {
            final NmsProxyTileMobSpawner mobSpawner = new NmsProxyTileMobSpawner((CreatureSpawner) blockState);
            this.queue(mobSpawner);
            return mobSpawner;
        } else if (blockState instanceof Chest) {
            final NmsProxyTileChest chest = new NmsProxyTileChest((Chest) blockState);
            chest.a = this;
            return chest;
        } else {
            Log.warn("NmsProxyWorldServer missing: " + blockState.getClass().getName());
        }
        return null;
    }

    @Override
    public int e(final int x, final int z) {
        int y = 127;
        while (this.c(x, y, z).c() && y > 0) {
            y--;
        }
        while (y > 0) {
            final int type = this.a(x, y, z);
            if (type == 0 || (!gc.m[type].bs.c() && !gc.m[type].bs.d())) {
                y--;
            } else {
                return y + 1;
            }
        }
        return -1;
    }

    @Override
    public int d(final int x, final int z) {
        if (!this.world.isChunkLoaded(x >> 4, z >> 4)) {
            return 0;
        }
        return this.world.getHighestBlockYAt(x, z);
    }

    @Override
    public boolean g(final int x, final int y, final int z) {
        return y >= this.world.getHighestBlockYAt(x, z);
    }

    @Override
    public int a(final int x, final int y, final int z, final boolean arg3) {
        // dummy - block updates?
        return 0;
    }

    @Override
    public int a(final dn paramdn, final int x, final int y, final int z) {
        // dummy - seems to have something to do with lighting
        return 0;
    }

    @Override
    public void a(final dn paramdn, final int x, final int y, final int z, final int arg4) {
        // dummy - seems to have something to do with lighting
    }

    @Override
    public boolean a(final ea paramea) {
        // dummy
        return false;
    }

    @Override
    public void b(final int arg0, final int arg1, final int arg2, final ay arg3) {
        Log.warn("NmsProxyWorldServer missing: " + arg3);
    }

    public void queue(final Runnable runnable) {
        if (this.queue == null) {
            this.queue = new LinkedList<>();
        }
        this.queue.add(runnable);
    }

    public void run() {
        if (this.queue == null) {
            return;
        }
        for (final Runnable runnable : this.queue) {
            runnable.run();
        }
        this.queue.clear();
    }
}
