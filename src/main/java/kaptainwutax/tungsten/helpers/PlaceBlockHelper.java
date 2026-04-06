package kaptainwutax.tungsten.helpers;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldView;

public final class PlaceBlockHelper {

    private PlaceBlockHelper() {}

    public record PlaceResult(int slot, BlockPos anchorPos, Direction face) {}

    public static int findPlaceableBlock(PlayerInventory inventory) {
        var mainStacks = inventory.getMainStacks();
        for (int slot = 0; slot < 9 && slot < mainStacks.size(); slot++) {
            ItemStack stack = mainStacks.get(slot);
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof BlockItem bi) {
                BlockState placed = bi.getBlock().getDefaultState();
                if (!placed.isAir() && placed.isOpaque()) {
                    return slot;
                }
            }
        }
        return -1;
    }

    public static PlaceResult findPlacementFace(BlockPos targetPos, WorldView world) {
        Direction[] preferred = {
            Direction.DOWN, Direction.NORTH, Direction.SOUTH,
            Direction.WEST, Direction.EAST, Direction.UP
        };
        for (Direction face : preferred) {
            BlockPos anchor = targetPos.offset(face.getOpposite());
            BlockState anchorState = world.getBlockState(anchor);
            if (anchorState.isSolidBlock(world, anchor)) {
                return new PlaceResult(-1, anchor, face);
            }
        }
        return null;
    }

    public static boolean hasPlaceableBlock(PlayerInventory inventory) {
        return findPlaceableBlock(inventory) >= 0;
    }
}
