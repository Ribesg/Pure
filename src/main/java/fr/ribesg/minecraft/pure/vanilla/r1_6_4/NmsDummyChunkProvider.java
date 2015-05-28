package fr.ribesg.minecraft.pure.vanilla.r1_6_4;

import r1_6_4.net.minecraft.server.*;

import java.util.List;

/**
 * @author Ribesg
 */

public class NmsDummyChunkProvider implements ado /* IChunkProvider */ {

    protected adr chunk;

    @Override
    public aco a(final abw arg0, final String arg1, final int arg2, final int arg3, final int arg4) {
        return null; // NOP
    }

    @Override
    public void a(final ado arg0, final int arg1, final int arg2) {
        // NOP
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List a(final oh arg0, final int arg1, final int arg2, final int arg3) {
        return null; // NOP
    }

    @Override
    public boolean a(final int x, final int y) {
        return this.chunk != null && !(this.chunk.g != x || this.chunk.h != y);
    }

    @Override
    public boolean a(final boolean arg0, final lx arg1) {
        return false; // NOP
    }

    @Override
    public void b() {
        // NOP
    }

    @Override
    public adr c(final int x, final int y) {
        if (this.chunk == null) {
            return null;
        }
        if (this.chunk.g != x || this.chunk.h != y) {
            return new adq(this.chunk.e, x, y);
        }
        return this.chunk;
    }

    @Override
    public adr d(final int x, final int y) {
        return this.c(x, y);
    }

    @Override
    public boolean c() {
        return false; // NOP
    }

    @Override
    public boolean d() {
        return false; // NOP
    }

    @Override
    public void e(final int arg0, final int arg1) {
        // NOP
    }

    @Override
    public String e() {
        return null; // NOP
    }

    @Override
    public int f() {
        return 0; // NOP
    }
}
