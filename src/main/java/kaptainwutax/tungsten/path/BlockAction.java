package kaptainwutax.tungsten.path;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;


public class BlockAction {

    public enum Type {
        BREAK,
        PLACE
    }

    public final Type      type;
    public final BlockPos  pos;
    public final Direction face;
    public final int       hotbarSlot;

    public BlockAction(Type type, BlockPos pos, Direction face, int hotbarSlot) {
        this.type       = type;
        this.pos        = pos;
        this.face       = face;
        this.hotbarSlot = hotbarSlot;
    }

    @Override
    public String toString() {
        return type + "@" + pos + " face=" + face + " slot=" + hotbarSlot;
    }
}
