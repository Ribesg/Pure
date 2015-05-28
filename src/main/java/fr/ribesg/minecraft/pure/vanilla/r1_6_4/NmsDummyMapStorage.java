package fr.ribesg.minecraft.pure.vanilla.r1_6_4;

import r1_6_4.net.minecraft.server.all;
import r1_6_4.net.minecraft.server.amr;

/**
 * @author coelho
 * @author Ribesg
 */
public class NmsDummyMapStorage extends amr /* MapStorage */ {

    public NmsDummyMapStorage() {
        super(null);
    }

    @Override
    public void a(final String arg0, final all arg1) {
        // NOP
    }

    @Override
    public int a(final String arg0) {
        return 0; // NOP
    }

    @SuppressWarnings("rawtypes")
    @Override
    public all a(final Class arg0, final String arg1) {
        return null; // NOP
    }
}
