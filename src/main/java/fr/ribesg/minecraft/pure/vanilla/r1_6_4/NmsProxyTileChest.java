package fr.ribesg.minecraft.pure.vanilla.r1_6_4;

import fr.ribesg.minecraft.pure.common.Log;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import r1_6_4.net.minecraft.server.*;

/**
 * @author Ribesg
 */
public class NmsProxyTileChest extends ary /* TileEntityChest */ {

    private final Inventory inv;

    public NmsProxyTileChest(final Chest chest) {
        this.inv = chest.getBlockInventory();
    }

    /*
     * Note that (ary) implements (mo).
     * (ary)           is the obfuscated class  name of TileEntityChest
     * (mo)            is the obfuscated class  name of IInventory
     * (ye)            is the obfuscated class  name of ItemStack
     * (mo.a(int,ye))  is the obfuscated method name of IInventory.setInventorySlotContents(int, ItemStack)
     */
    @Override
    public void a(final int index, final ye yeArg) {
        /*
         * (ye.d)   is the obfuscated field  name of ItemStack.id
         * (ye.b)   is the obfuscated field  name of ItemStack.stackSize
         * (ye.k()) is the obfuscated method name of ItemStack.getItemDamage()
         */
        @SuppressWarnings("deprecation")
        final ItemStack item = new ItemStack(
            yeArg.d,
            yeArg.b,
            (short) yeArg.k()
        );

        /*
         * (by)           is the obfuscated class  name of NBTTagCompound
         * (ye.d)         is the obfuscated field  name of ItemStack.stackTagCompound
         * (cg)           is the obfuscated class  name of NBTTagList
         * (by.a(String)) is the obfuscated method name of NBTTagCompound.getTag(String)
         * (cg.c())       is the obfuscated method name of NBTTagList.tagCount()
         * (cg.b(int))    is the obfuscated method name of NBTTagList.getCompoundTagAt(int)
         */
        // Enchanted Books can be generated, let's handle that.
        if (item.getType() == Material.ENCHANTED_BOOK) {
            final by itemNbt = yeArg.e;
            if (itemNbt != null) {
                final cg storedEnchNbt = (cg) itemNbt.a("StoredEnchantments");
                if (storedEnchNbt != null && storedEnchNbt.c() > 0) {
                    final EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
                    for (int i = 0; i < storedEnchNbt.c(); i++) {
                        this.addMcEnchant(meta, (by) storedEnchNbt.b(i));
                    }
                    item.setItemMeta(meta);
                }
            }
        }

        this.inv.setItem(index, item);
    }

    /*
     * (dh.a(String)) is the obfuscated method name of NBTTagCompound.getTag(String)
     * (dw)           is the obfuscated class  name of NBTTagShort
     * (dw.e())       is the obfuscated method name of NBTTagShort.getShort()
     */
    private void addMcEnchant(final EnchantmentStorageMeta meta, final by enchNbt) {
        try {
            // Here we are not using (dh.e(String)) because it does not fail correctly:
            // it returns 0 instead of throwing an exception.
            // Instead, we use (dw.e()). This way either the cast will fail or a NPE will be thrown.
            final short enchId = ((cj) enchNbt.a("r1_6_4/net/minecraft/server/id")).a;
            final short enchLvl = ((cj) enchNbt.a("lvl")).a;
            @SuppressWarnings("deprecation")
            final Enchantment ench = Enchantment.getById(enchId);
            if (ench == null) {
                Log.warn("Unknown Enchantment ID (" + enchId + "), ignored.");
            } else {
                meta.addStoredEnchant(ench, enchLvl, false);
            }
        } catch (final RuntimeException e) {
            if (e instanceof ClassCastException || e instanceof NullPointerException) {
                Log.error("Failed to add Enchantment to Enchanted Book, ignored.\nThe NBT was: " + enchNbt, e);
            } else {
                throw e;
            }
        }
    }
}
