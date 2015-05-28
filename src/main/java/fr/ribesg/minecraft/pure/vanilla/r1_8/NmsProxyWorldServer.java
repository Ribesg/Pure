package fr.ribesg.minecraft.pure.vanilla.r1_8;

import fr.ribesg.minecraft.pure.common.Log;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.*;
import r1_8.net.minecraft.server.*;

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
    public boolean a(final dt arg0, final bec nmsBlock, final int arg2) {
        final Block block = this.world.getBlockAt(arg0.n(), arg0.o(), arg0.p());
        block.setTypeId(atr.a(nmsBlock.c()));
        block.setData((byte) nmsBlock.c().c(nmsBlock));
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public atr c(final dt arg0) {
        return atr.c(this.world.getBlockAt(arg0.n(), arg0.o(), arg0.p()).getTypeId());
    }

    @Override
    public void d(final dt arg0, final atr arg1) {
        // NOP
    }

    @Override
    public boolean d(final wv arg0) {
        return false; // NOP
    }

    @Override
    public dt m(final dt arg0) {
        return new dt(arg0.n(), this.world.getHighestBlockYAt(arg0.n(), arg0.p()), arg0.p());
    }

    @SuppressWarnings("deprecation")
    @Override
    public bec p(final dt arg0) {
        final Block block = this.world.getBlockAt(arg0.n(), arg0.o(), arg0.p());
        return atr.c(block.getTypeId()).a(block.getData());
    }

    @Override
    public bcm s(final dt arg0) {
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
            Log.warn("NmsProxyWorldServer missing: " + blockState.getClass().getName());
        }
        return null;
    }
}
