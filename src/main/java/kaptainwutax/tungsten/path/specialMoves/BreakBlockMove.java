package kaptainwutax.tungsten.path.specialMoves;

import kaptainwutax.tungsten.TungstenModDataContainer;
import kaptainwutax.tungsten.agent.Agent;
import kaptainwutax.tungsten.helpers.AutoToolHelper;
import kaptainwutax.tungsten.helpers.DirectionHelper;
import kaptainwutax.tungsten.path.BlockAction;
import kaptainwutax.tungsten.path.Node;
import kaptainwutax.tungsten.path.PathInput;
import kaptainwutax.tungsten.path.blockSpaceSearchAssist.BlockNode;
import kaptainwutax.tungsten.render.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldView;

public class BreakBlockMove {

    public static Node generateMove(Node parent, BlockNode nextBlockNode,
                                    BlockPos blockToBreak, int ticksNeeded, int toolSlot) {

        WorldView world = TungstenModDataContainer.world;
        Agent agent = parent.agent;

        float breakYaw = (float) DirectionHelper.calcYawFromVec3d(
            agent.getPos(),
            Vec3d.ofCenter(blockToBreak)
        );
        float blockCenterY = blockToBreak.getY() + 0.5f;
        float eyeY = (float)(agent.posY + 1.62);
        float dx = (float)(blockToBreak.getX() + 0.5 - agent.posX);
        float dz = (float)(blockToBreak.getZ() + 0.5 - agent.posZ);
        float hDist = (float)Math.sqrt(dx * dx + dz * dz);
        float breakPitch = (float)-Math.toDegrees(Math.atan2(blockCenterY - eyeY, hDist));
        breakPitch = Math.max(-90f, Math.min(90f, breakPitch));

        double costPerBreakTick = 1.5; 
        double totalBreakCost = ticksNeeded * costPerBreakTick;

        Direction breakFace = getFaceFromPlayer(agent, blockToBreak);

        Node current = parent;

        if (toolSlot >= 0) {
            BlockAction switchAction = new BlockAction(BlockAction.Type.BREAK, blockToBreak, breakFace, toolSlot);
            PathInput switchInput = new PathInput(
                false, false, false, false, false, false, false,
                breakPitch, breakYaw, switchAction
            );
            current = new Node(current, world, switchInput, new Color(255, 100, 0), current.cost + 0.5);
        }

        for (int t = 0; t < ticksNeeded; t++) {
            BlockAction breakAction = new BlockAction(BlockAction.Type.BREAK, blockToBreak, breakFace, -1);
            PathInput breakInput = new PathInput(
                false, false, false, false, false, false, false,
                breakPitch, breakYaw, breakAction
            );
            current = new Node(current, world, breakInput,
                new Color(255, Math.max(0, 255 - t * (255 / Math.max(ticksNeeded, 1))), 0),
                current.cost + costPerBreakTick
            );
        }

        return current;
    }

    private static Direction getFaceFromPlayer(Agent agent, BlockPos target) {
        double dx = agent.posX - (target.getX() + 0.5);
        double dy = agent.posY + 1.62 - (target.getY() + 0.5);
        double dz = agent.posZ - (target.getZ() + 0.5);

        if (Math.abs(dy) > Math.abs(dx) && Math.abs(dy) > Math.abs(dz)) {
            return dy > 0 ? Direction.UP : Direction.DOWN;
        } else if (Math.abs(dx) > Math.abs(dz)) {
            return dx > 0 ? Direction.EAST : Direction.WEST;
        } else {
            return dz > 0 ? Direction.SOUTH : Direction.NORTH;
        }
    }
}
