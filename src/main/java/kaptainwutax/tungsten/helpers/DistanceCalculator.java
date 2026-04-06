package kaptainwutax.tungsten.helpers;

import kaptainwutax.tungsten.TungstenMod;
import kaptainwutax.tungsten.agent.Agent;
import kaptainwutax.tungsten.render.Color;
import kaptainwutax.tungsten.render.Cuboid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class DistanceCalculator {

	public static double getHorizontalEuclideanDistance(BlockPos startPos, BlockPos endPos) {
		return getHorizontalEuclideanDistance(new Vec3d(startPos.getX(), startPos.getY(), startPos.getZ()), new Vec3d(endPos.getX(), endPos.getY(), endPos.getZ()));
	}

	public static double getHorizontalEuclideanDistance(Vec3d startPos, Vec3d endPos) {
		double dx = endPos.getX() - startPos.getX();
    	double dz = endPos.getZ() - startPos.getZ();
    	return Math.sqrt(dx * dx + dz * dz);
	}
	

	public static double getEuclideanDistance(Vec3d startPos, Vec3d endPos) {
		double dx = endPos.getX() - startPos.getX();
    	double dy = endPos.getY() - startPos.getY();
    	double dz = endPos.getZ() - startPos.getZ();
    	return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}

	public static double getHorizontalManhattanDistance(BlockPos startPos, BlockPos endPos) {
		return getHorizontalManhattanDistance(new Vec3d(startPos.getX(), startPos.getY(), startPos.getZ()), new Vec3d(endPos.getX(), endPos.getY(), endPos.getZ()));
	}

	public static double getHorizontalManhattanDistance(Vec3d startPos, Vec3d endPos) {
		double dx = endPos.getX() - startPos.getX();
    	double dz = endPos.getZ() - startPos.getZ();
    	return dx + dz;
	}

    public static double getDistanceToEdge(Agent agent) {
        Vec3d position = agent.getPos();
        BlockPos blockPos = agent.getBlockPos();
		float f = agent.pitch * (float) (Math.PI / 180.0);
		float g = -agent.yaw * (float) (Math.PI / 180.0);
		float h = MathHelper.cos(g);
		float i = MathHelper.sin(g);
		float j = MathHelper.cos(f);
		float k = MathHelper.sin(f);
		Vec3d lookDirection =  new Vec3d((double)(i * j), (double)(-k), (double)(h * j)); // Direction player is looking

        double deltaX = position.x - blockPos.getX();
        double deltaZ = position.z - blockPos.getZ();

        double distance = 0;
        if (Math.abs(lookDirection.x) > Math.abs(lookDirection.z)) {
            if (lookDirection.x > 0) {
                distance = 1.0 - deltaX; 
            } else {
                distance = deltaX; 
            }
        } else {
            if (lookDirection.z > 0) {
                distance = 1.0 - deltaZ; 
            } else {
                distance = deltaZ; 
            }
        }

        return MathHelper.clamp(distance, 0.0, 1.0); 
    }

	public static double getJumpHeight(double from, double to) {
		
		double diff = to - from;
		
		if (to > from) {
			return diff > 0 ? diff : diff * -1;
		}
		return diff > 0 ? diff * -1 : diff;
	}

	public static int getJumpHeight(int from, int to) {
		
		int diff = to - from;
		
		if (to > from) {
			return diff > 0 ? diff : diff * -1;
		}
		return diff > 0 ? diff * -1 : diff;
	}
}
