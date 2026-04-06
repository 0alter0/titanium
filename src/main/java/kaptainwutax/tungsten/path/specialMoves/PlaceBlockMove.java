package kaptainwutax.tungsten.path.specialMoves;

import kaptainwutax.tungsten.TungstenModDataContainer;
import kaptainwutax.tungsten.agent.Agent;
import kaptainwutax.tungsten.helpers.DirectionHelper;
import kaptainwutax.tungsten.helpers.PlaceBlockHelper;
import kaptainwutax.tungsten.path.BlockAction;
import kaptainwutax.tungsten.path.Node;
import kaptainwutax.tungsten.path.PathInput;
import kaptainwutax.tungsten.path.blockSpaceSearchAssist.BlockNode;
import kaptainwutax.tungsten.render.Color;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldView;

public class PlaceBlockMove {

    public static Node generateMove(Node parent, BlockNode nextBlockNode,
                                    BlockPos blockToPlace, int placeSlot) {

        WorldView world = TungstenModDataContainer.world;
        Agent agent = parent.agent;

        PlaceBlockHelper.PlaceResult result = PlaceBlockHelper.findPlacementFace(blockToPlace, world);
        if (result == null) {
            return parent;
        }

        float placeYaw = (float) DirectionHelper.calcYawFromVec3d(
            agent.getPos(),
            Vec3d.ofCenter(result.anchorPos())
        );
        float anchorCenterY = result.anchorPos().getY() + 0.5f;
        float eyeY = (float)(agent.posY + 1.62);
        float dx = (float)(result.anchorPos().getX() + 0.5 - agent.posX);
        float dz = (float)(result.anchorPos().getZ() + 0.5 - agent.posZ);
        float hDist = Math.max(0.001f, (float)Math.sqrt(dx * dx + dz * dz));
        float placePitch = (float)-Math.toDegrees(Math.atan2(anchorCenterY - eyeY, hDist));
        placePitch = Math.max(-90f, Math.min(90f, placePitch));

        double placeCost = 20.0; 

        Node current = parent;

        if (placeSlot >= 0) {
            BlockAction switchAction = new BlockAction(BlockAction.Type.PLACE, blockToPlace, result.face(), placeSlot);
            PathInput switchInput = new PathInput(
                false, false, false, false, false, false, false,
                placePitch, placeYaw, switchAction
            );
            current = new Node(current, world, switchInput, new Color(0, 200, 255), current.cost + 0.5);
        }

        BlockAction placeAction = new BlockAction(BlockAction.Type.PLACE, blockToPlace, result.face(), -1);
        PathInput placeInput = new PathInput(
            false, false, false, false, false, false, false,
            placePitch, placeYaw, placeAction
        );
        current = new Node(current, world, placeInput, new Color(0, 200, 255), current.cost + placeCost);

        PathInput idleInput = new PathInput(false, false, false, false, false, false, false, placePitch, placeYaw);
        current = new Node(current, world, idleInput, new Color(0, 200, 255), current.cost + 1.0);

        return current;
    }
}
