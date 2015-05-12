package fr.ribesg.bukkit.pure.minecraft.r1_8;

import fr.ribesg.bukkit.pure.Pure;
import r1_8.net.minecraft.server.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.*;

/**
 * @author coelho
 * @author Ribesg
 */
public class NmsProxyWorldServer extends qt {

    @SuppressWarnings("all") // No warning name for "variable is never assigned"
    private World world;

    /*
     * The first argument of the super constructor seems to be @NotNull.
     * There is no @SuppressWarnings rule specifically for this, so all it is.
     */
    @SuppressWarnings("all")
    public NmsProxyWorldServer() {
        super(null, null, null, 0, null);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean a(dt arg0, bec nmsBlock, int arg2) {
        final Block block = this.world.getBlockAt(arg0.n(), arg0.o(), arg0.p());
        block.setTypeId(atr.a(nmsBlock.c()));
        block.setData((byte) nmsBlock.c().c(nmsBlock));
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public atr c(dt arg0) {
        return atr.c(this.world.getBlockAt(arg0.n(), arg0.o(), arg0.p()).getTypeId());
    }

    @Override
    public void d(dt arg0, atr arg1) {
        // NOP
    }

    @Override
    public boolean d(wv arg0) {
        return false; // NOP
    }

    @Override
    public dt m(dt arg0) {
        return new dt(arg0.n(), this.world.getHighestBlockYAt(arg0.n(), arg0.p()), arg0.p());
    }

    @SuppressWarnings("deprecation")
    @Override
    public bec p(dt arg0) {
        final Block block = this.world.getBlockAt(arg0.n(), arg0.o(), arg0.p());
        return atr.c(block.getTypeId()).a(block.getData());
    }

    @Override
    public bcm s(dt arg0) {
        final Block block = this.world.getBlockAt(arg0.n(), arg0.o(), arg0.p());
        if (block.getType() == Material.AIR) {
            return null;
        }
        final BlockState blockState = block.getState();
        if (blockState == null) {
            return null;
        }
        if (blockState instanceof CreatureSpawner) {
            return new NmsProxyTileMobSpawner((CreatureSpawner) blockState);
        } else if (blockState instanceof Chest) {
            return new NmsProxyTileChest((Chest) blockState);
        } else {
            Pure.logger().warning("NmsProxyWorldServer missing: " + blockState.getClass().getName());
        }
        return null;
    }
}
