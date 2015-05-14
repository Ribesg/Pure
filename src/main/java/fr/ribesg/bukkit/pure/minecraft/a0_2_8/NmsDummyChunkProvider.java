package fr.ribesg.bukkit.pure.minecraft.a0_2_8;

import a0_2_8.net.minecraft.server.*;

/**
 * @author coelho
 * @author Ribesg
 */
public class NmsDummyChunkProvider implements bu {

    protected ju chunk;

    @Override
    public boolean b() {
        return false; // NOP
    }

    @Override
    public void a(final bu arg0, final int arg1, final int arg2) {
        // NOP
    }

    @Override
    public ju b(final int x, final int y) {
        if (this.chunk == null) {
            return null;
        }
        if (this.chunk.j != x || this.chunk.k != y) {
            return new NmsDummyChunk(this.chunk.d, x, y);
        }
        return this.chunk;
    }

    @Override
    public boolean a(final int x, final int y) {
        return this.chunk != null && !(this.chunk.j != x || this.chunk.k != y);
    }

    @Override
    public boolean a(final boolean arg0, final jc arg1) {
        return false; // NOP
    }

    @Override
    public boolean a() {
        return false; // NOP
    }
}
