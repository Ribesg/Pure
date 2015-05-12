package fr.ribesg.bukkit.pure.minecraft.r1_7_10;

import fr.ribesg.bukkit.pure.Pure;
import r1_7_10.net.minecraft.server.*;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.logging.Level;

/**
 * @author Ribesg
 */
public class NmsProxyTileChest extends aow /* TileEntityChest */ {

    private final Inventory inv;

    public NmsProxyTileChest(final Chest chest) {
        this.inv = chest.getBlockInventory();
    }

    /*
     * Note that (aow) implements (rb).
     * (aow)           is the obfuscated class  name of TileEntityChest
     * (rb)            is the obfuscated class  name of IInventory
     * (add)           is the obfuscated class  name of ItemStack
     * (rb.a(int,add)) is the obfuscated method name of IInventory.setInventorySlotContents(int, ItemStack)
     */
    @Override
    public void a(final int index, final add addArg) {
        /*
         * (adb)        is the obfuscated class  name of Item
         * (adb.b(adb)) is the obfuscated method name of Item.getIdFromItem(Item)
         * (add.b())    is the obfuscated method name of ItemStack.getItem()
         * (add.b)      is the obfuscated field  name of ItemStack.stackSize
         * (add.k())    is the obfuscated method name of ItemStack.getItemDamage()
         */
        @SuppressWarnings("deprecation")
        final ItemStack item = new ItemStack(
            adb.b(addArg.b()),
            addArg.b,
            (short) addArg.k()
        );

        /*
         * (dh)           is the obfuscated class  name of NBTTagCompound
         * (add.d)        is the obfuscated field  name of ItemStack.stackTagCompound
         * (dq)           is the obfuscated class  name of NBTTagList
         * (dh.a(String)) is the obfuscated method name of NBTTagCompound.getTag(String)
         * (dq.c())       is the obfuscated method name of NBTTagList.tagCount()
         * (dq.b(int))    is the obfuscated method name of NBTTagList.getCompoundTagAt(int)
         */
        // Enchanted Books can be generated, let's handle that.
        if (item.getType() == Material.ENCHANTED_BOOK) {
            final dh itemNbt = addArg.d;
            if (itemNbt != null) {
                final dq storedEnchNbt = (dq) itemNbt.a("StoredEnchantments");
                if (storedEnchNbt != null && storedEnchNbt.c() > 0) {
                    final EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
                    for (int i = 0; i < storedEnchNbt.c(); i++) {
                        this.addMcEnchant(meta, storedEnchNbt.b(i));
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
    private void addMcEnchant(final EnchantmentStorageMeta meta, final dh enchNbt) {
        try {
            Pure.logger().info(enchNbt.toString());
            // Here we are not using (dh.e(String)) because it does not fail correctly:
            // it returns 0 instead of throwing an exception.
            // Instead, we use (dw.e()). This way either the cast will fail or a NPE will be thrown.
            final short enchId = ((dw) enchNbt.a("net/minecraft/server/r1_7_10/id")).e();
            final short enchLvl = ((dw) enchNbt.a("lvl")).e();
            @SuppressWarnings("deprecation")
            final Enchantment ench = Enchantment.getById(enchId);
            if (ench == null) {
                Pure.logger().warning("Unknown Enchantment ID (" + enchId + "), ignored.");
            } else {
                meta.addStoredEnchant(ench, enchLvl, false);
            }
        } catch (final ClassCastException | NullPointerException e) {
            Pure.logger().log(Level.SEVERE, "Failed to add Enchantment to Enchanted Book, ignored.\nThe NBT was: " + enchNbt, e);
        }
    }
}
