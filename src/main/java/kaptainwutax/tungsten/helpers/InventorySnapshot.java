package kaptainwutax.tungsten.helpers;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

import java.util.ArrayList;
import java.util.List;

public final class InventorySnapshot {

    public record SlotSnapshot(int slot, ItemStack stack) {}

    private static volatile List<SlotSnapshot> hotbar = null;

    private InventorySnapshot() {}


    public static void capture(PlayerInventory inventory) {
        var stacks = inventory.getMainStacks();
        List<SlotSnapshot> snap = new ArrayList<>(9);
        for (int i = 0; i < 9 && i < stacks.size(); i++) {
            snap.add(new SlotSnapshot(i, stacks.get(i).copy()));
        }
        hotbar = snap;
    }

    public static boolean isAvailable() {
        return hotbar != null;
    }

    public static List<SlotSnapshot> getHotbar() {
        return hotbar;
    }

  
    public static AutoToolHelper.ToolResult getBestTool(BlockState state, BlockPos pos, WorldView world) {
        List<SlotSnapshot> snap = hotbar;
        if (snap == null) return new AutoToolHelper.ToolResult(-1, 200);

        float hardness = state.getHardness(world, pos);
        if (hardness < 0)  return new AutoToolHelper.ToolResult(-1, Integer.MAX_VALUE);
        if (hardness == 0) return new AutoToolHelper.ToolResult(-1, 1);

        int   bestSlot  = -1;
        float handDelta = 1f / hardness / 100f;
        int   bestTicks = Math.max(1, (int) Math.ceil(1.0 / handDelta));

        for (SlotSnapshot ss : snap) {
            if (ss.stack().isEmpty()) continue;
            ItemStack stack    = ss.stack();
            float speed        = stack.getMiningSpeedMultiplier(state);
            boolean suitable   = stack.isSuitableFor(state);

            int effLevel = getEfficiencyLevel(stack);
            if (effLevel > 0 && suitable) speed += effLevel * effLevel + 1;

            float delta = suitable
                ? speed / hardness / 30f
                : 1f / hardness / 100f;
            int ticks = Math.max(1, (int) Math.ceil(1.0 / delta));

            if (ticks < bestTicks) {
                bestTicks = ticks;
                bestSlot  = ss.slot();
            }
        }
        return new AutoToolHelper.ToolResult(bestSlot, bestTicks);
    }

    public static int findPlaceableBlock() {
        List<SlotSnapshot> snap = hotbar;
        if (snap == null) return -1;
        for (SlotSnapshot ss : snap) {
            if (ss.stack().isEmpty()) continue;
            if (ss.stack().getItem() instanceof BlockItem bi) {
                var placed = bi.getBlock().getDefaultState();
                if (!placed.isAir() && placed.isOpaque()) return ss.slot();
            }
        }
        return -1;
    }


    private static int getEfficiencyLevel(ItemStack stack) {
        var effId = net.minecraft.util.Identifier.ofVanilla("efficiency");
        for (var entry : stack.getEnchantments().getEnchantmentEntries()) {
            if (entry.getKey().matchesId(effId)) return entry.getIntValue();
        }
        return 0;
    }
}
