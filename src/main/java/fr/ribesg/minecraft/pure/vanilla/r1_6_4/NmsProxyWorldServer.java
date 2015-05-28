package fr.ribesg.minecraft.pure.vanilla.r1_6_4;

import fr.ribesg.bukkit.pure.log.Log;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.*;
import r1_6_4.net.minecraft.server.*;

/**
 * @author coelho
 * @author Ribesg
 */
public class NmsProxyWorldServer extends js /* WorldServer */ {

    @SuppressWarnings("all") // No warning name for "variable is never assigned"
    private World world;

    /*
     * The first and second arguments of the super constructor seems to be
     * @NotNull.
     * There is no @SuppressWarnings rule specifically for this, so all it is.
     */
    @SuppressWarnings("all")
    public NmsProxyWorldServer() {
        super(null, null, null, 0, null, null, null);
    }

    @SuppressWarnings("deprecation")
    @Override
    public int a(final int x, final int y, final int z) {
        return this.world.getBlockAt(x, y, z).getTypeId();
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean f(final int x, final int y, final int z, final int id, final int data, final int arg5) {
        final Block block = this.world.getBlockAt(x, y, z);
        block.setTypeId(id);
        block.setData((byte) data);
        return true;
    }

    @Override
    public boolean d(final nn arg0) {
        return false; // NOP
    }

    @Override
    public void g(final int x, final int y, final int z, final int id) {
        // NOP
    }

    @Override
    public int f(final int x, final int z) {
        return this.world.getHighestBlockYAt(x, z);
    }

    @Override
    public asp r(final int x, final int y, final int z) {
        final Block block = this.world.getBlockAt(x, y, z);
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
