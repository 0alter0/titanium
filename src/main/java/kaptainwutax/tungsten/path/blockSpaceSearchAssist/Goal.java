package kaptainwutax.tungsten.path.blockSpaceSearchAssist;

import net.minecraft.util.math.BlockPos;

public class Goal {

    public final int x;

    public final int y;

    public final int z;

    public Goal(BlockPos pos) {
        this(pos.getX(), pos.getY(), pos.getZ());
    }

    public Goal(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }


    public boolean isInGoal(int x, int y, int z) {
        return x == this.x && y == this.y && z == this.z;
    }


    public double heuristic(int x, int y, int z) {
        int xDiff = x - this.x;
        int yDiff = y - this.y;
        int zDiff = z - this.z;
        return calculate(xDiff, yDiff, zDiff);
    }

    @Override
    public String toString() {
        return String.format(
                "GoalBlock{x=%s,y=%s,z=%s}",
                Integer.toString(x),
                Integer.toString(y),
                Integer.toString(z)
        );
    }


    public BlockPos getGoalPos() {
        return new BlockPos(x, y, z);
    }

    public static double calculate(double xDiff, int yDiff, double zDiff) {
	    
	    return (Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff));
    }
}
