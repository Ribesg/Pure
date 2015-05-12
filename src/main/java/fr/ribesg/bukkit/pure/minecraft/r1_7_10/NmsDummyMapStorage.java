package fr.ribesg.bukkit.pure.minecraft.r1_7_10;

import r1_7_10.net.minecraft.server.*;

/**
 * @author coelho
 * @author Ribesg
 */
public class NmsDummyMapStorage extends azq /* MapStorage */ {

    public NmsDummyMapStorage() {
        super(null);
    }

    @Override
    public void a(final String arg0, final ayl arg1) {
        // NOP
    }

    @Override
    public int a(final String arg0) {
        return 0; // NOP
    }

    @SuppressWarnings("rawtypes")
    @Override
    public ayl a(final Class arg0, final String arg1) {
        return null; // NOP
    }
}
