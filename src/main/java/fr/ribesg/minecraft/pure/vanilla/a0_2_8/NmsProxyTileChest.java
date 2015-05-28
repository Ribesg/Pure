package fr.ribesg.minecraft.pure.vanilla.a0_2_8;

import a0_2_8.net.minecraft.server.hn;
import a0_2_8.net.minecraft.server.ic;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @author Ribesg
 */
public class NmsProxyTileChest extends ic /* TileEntityChest */ {

    private final Inventory inv;

    public NmsProxyTileChest(final Chest chest) {
        this.inv = chest.getBlockInventory();
    }

    /*
     * (ic)            is the obfuscated class  name of TileEntityChest
     * (hn)            is the obfuscated class  name of ItemStack
     * (ic.a(int,add)) is the obfuscated method name of TileEntityChest.setInventorySlotContents(int, ItemStack)
     */
    @SuppressWarnings("deprecation")
    @Override
    public void a(final int index, final hn hnArg) {
        /*
         * (hn.c) is the obfuscated field name of ItemStack.id
         * (hn.a) is the obfuscated field name of ItemStack.stackSize
         * (hn.d) is the obfuscated field name of ItemStack.data
         */

        this.inv.setItem(index, new ItemStack(
            hnArg.c,
            hnArg.a,
            (short) hnArg.d
        ));
    }
}
