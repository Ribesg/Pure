package fr.ribesg.bukkit.pure.minecraft.r1_8;

import r1_8.net.minecraft.server.*;

import java.util.List;

/**
 * @author Ribesg
 */
public class NmsDummyChunkProvider implements bfe {

    protected bfh chunk;

    @Override
    public bfh a(final dt arg0) {
        return this.d(arg0.n() >> 4, arg0.p() >> 4);
    }

    @Override
    public boolean a(final int x, final int y) {
        return this.chunk != null && !(this.chunk.a != x || this.chunk.b != y);
    }

    @Override
    public boolean a(final boolean arg0, final uy arg1) {
        return false; // NOP
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List a(final xp arg0, final dt arg1) {
        return null; // NOP
    }

    @Override
    public void a(final bfe arg0, final int arg1, final int arg2) {
        // NOP
    }

    @Override
    public dt a(final aqu arg0, final String arg1, final dt arg2) {
        return null; // NOP
    }

    @Override
    public void a(final bfh arg0, final int arg1, final int arg2) {
        // NOP
    }

    @Override
    public boolean a(final bfe arg0, final bfh arg1, final int arg2, final int arg3) {
        return false; // NOP
    }

    @Override
    public void c() {
        // NOP
    }

    @Override
    public bfh d(final int x, final int y) {
        if (this.chunk == null) {
            return null;
        }
        if (this.chunk.a != x || this.chunk.b != y) {
            return new bfg(this.chunk.p(), x, y);
        }
        return this.chunk;
    }

    @Override
    public boolean d() {
        return false; // NOP
    }

    @Override
    public boolean e() {
        return false; // NOP
    }

    @Override
    public String f() {
        return null; // NOP
    }

    @Override
    public int g() {
        return 0; // NOP
    }
}
