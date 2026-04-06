package kaptainwutax.tungsten.helpers;

import net.minecraft.block.BlockState;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

public final class AutoToolHelper {

    private AutoToolHelper() {}

    public record ToolResult(int slot, int ticksNeeded) {}

    public static ToolResult getBestTool(PlayerInventory inventory,
                                          BlockState state,
                                          BlockPos pos,
                                          WorldView world) {
        float hardness = state.getHardness(world, pos);
        if (hardness < 0) {
            return new ToolResult(-1, Integer.MAX_VALUE);
        }
        if (hardness == 0) {
            return new ToolResult(-1, 1);
        }

        int   bestSlot  = -1;
        float bestDelta = handDelta(hardness);
        int   bestTicks = deltaTicks(bestDelta);

        var mainStacks = inventory.getMainStacks();
        for (int slot = 0; slot < 9 && slot < mainStacks.size(); slot++) {
            ItemStack stack = mainStacks.get(slot);
            if (stack.isEmpty()) continue;

            float speed    = stack.getMiningSpeedMultiplier(state);
            boolean suitable = stack.isSuitableFor(state);

            int effLevel = getEfficiencyLevel(stack);
            if (effLevel > 0 && suitable) {
                speed += effLevel * effLevel + 1;
            }

            float delta = suitable
                ? speed / hardness / 30f
                : 1f / hardness / 100f;
            int ticks = deltaTicks(delta);

            if (ticks < bestTicks) {
                bestTicks = ticks;
                bestSlot  = slot;
            }
        }

        return new ToolResult(bestSlot, bestTicks);
    }

    public static boolean isBreakable(BlockState state, BlockPos pos, WorldView world) {
        return state.getHardness(world, pos) >= 0;
    }


    private static float handDelta(float hardness) {
        if (hardness <= 0) return 1f;
        return 1f / hardness / 100f;
    }

    private static int deltaTicks(float delta) {
        if (delta <= 0) return Integer.MAX_VALUE;
        return Math.max(1, (int) Math.ceil(1.0 / delta));
    }

    private static int getEfficiencyLevel(ItemStack stack) {
        var efficiencyId = net.minecraft.util.Identifier.ofVanilla("efficiency");
        ItemEnchantmentsComponent enchantments = stack.getEnchantments();
        for (var entry : enchantments.getEnchantmentEntries()) {
            if (entry.getKey().matchesId(efficiencyId)) {
                return entry.getIntValue();
            }
        }
        return 0;
    }
}
