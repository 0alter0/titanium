package kaptainwutax.tungsten.helpers;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;


public class DirectionHelper {

	public static float calcYawFromRotation(float rotation) {
        float normalized = ((rotation % 360) + 360) % 360;
        
        return normalized;
    }

	public static double calcYawFromVec3d(Vec3d orig, Vec3d dest) {
        double[] delta = {orig.x - dest.x, orig.y - dest.y, orig.z - dest.z};
        double yaw = Math.atan2(delta[0], -delta[2]);
        return yaw * 180.0 / Math.PI;
    }

	public static double calcPitchFromVec3d(Vec3d orig, Vec3d dest) {
	    double deltaY = orig.y - dest.y;
	    double distance = Math.sqrt(Math.pow(orig.x - dest.x, 2) + Math.pow(orig.z - dest.z, 2));
	    double pitch = Math.atan2(deltaY, distance);
	    return pitch * 180.0 / Math.PI;
	}
	
	public static Direction getHorizontalDirectionFromPos(BlockPos orig, BlockPos dest) {
        return getHorizontalDirectionFromYaw(calcYawFromVec3d(new Vec3d(orig.getX(), orig.getY(), orig.getZ()), new Vec3d(dest.getX(), dest.getY(), dest.getZ())));
    }

	public static Direction getHorizontalDirectionFromPos(Vec3d orig, Vec3d dest) {
        return getHorizontalDirectionFromYaw(calcYawFromVec3d(orig, dest));
    }

	public static Direction getHorizontalDirectionFromYaw(double yaw) {
        yaw %= 360.0F;
        if (yaw < 0) {
            yaw += 360.0F;
        }

        if ((yaw >= 45 && yaw < 135) || (yaw >= -315 && yaw < -225)) {
            return Direction.WEST;
        } else if ((yaw >= 135 && yaw < 225) || (yaw >= -225 && yaw < -135)) {
            return Direction.NORTH;
        } else if ((yaw >= 225 && yaw < 315) || (yaw >= -135 && yaw < -45)) {
            return Direction.EAST;
        } else {
            return Direction.SOUTH;
        }
    }
}
