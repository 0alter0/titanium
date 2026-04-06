package kaptainwutax.tungsten.helpers;

import kaptainwutax.tungsten.TungstenMod;
import net.minecraft.block.AmethystClusterBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LadderBlock;
import net.minecraft.block.PointedDripstoneBlock;
import net.minecraft.block.ScaffoldingBlock;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.WorldView;


public class BlockShapeChecker {

	public static double getBlockHeight(BlockPos pos, WorldView world) {
		BlockState state = world.getBlockState(pos);
		
		if (state.isAir()) return 0;
		
		VoxelShape shape = state.getCollisionShape(world, pos);
		double height = shape.getMax(Axis.Y);
		
		return height;
	}
	

	public static double getShapeVolume(BlockPos pos, WorldView world) {
		BlockState state = world.getBlockState(pos);
    	return getShapeVolume(state, pos, world);
    }

	public static double getShapeVolume(BlockState state, BlockPos pos, WorldView world) {
		VoxelShape shape = state.getCollisionShape(world, pos);
//        if (shape.isEmpty()) shape = state.getOutlineShape(world, pos);
        
    	return getShapeVolume(shape);
    }
	
	public static double getShapeVolume(VoxelShape shape) {
    	
    	double maxX = shape.getMax(Direction.Axis.X);
    	double minX = shape.getMin(Direction.Axis.X);
    	double maxZ = shape.getMax(Direction.Axis.Z);
    	double minZ = shape.getMin(Direction.Axis.Z);
    	
    	double blockVolume = (maxX - minX) * (maxZ - minZ);
    	
    	if (Double.isInfinite(blockVolume))
        	blockVolume = 0;
    	
    	return blockVolume;
    }

    
	public static double getBlockHeight(VoxelShape blockShape) {
        return blockShape.getMax(Axis.Y);
    }
    
	public static double getBlockHeight(WorldView world, BlockState state, BlockPos pos) {
    	VoxelShape blockShape = state.getCollisionShape(world, pos);
    	
    	return getBlockHeight(blockShape);
    }
    
    public static boolean hasBiggerCollisionShapeThanAbove(WorldView world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        if (blockState.getBlock() instanceof LadderBlock) return false;
        
        double blockVolume = BlockShapeChecker.getShapeVolume(pos, world);
        double aboveBlockVolume1 = BlockShapeChecker.getShapeVolume(pos.up(1), world);
        double aboveBlockVolume2 = BlockShapeChecker.getShapeVolume(pos.up(2), world);
        
        return blockVolume > aboveBlockVolume1 && blockVolume > aboveBlockVolume2;
    }
	 
   public static boolean isBlockNormalCube(BlockState state) {
	        Block block = state.getBlock();
	        if (block instanceof ScaffoldingBlock
	                || block instanceof ShulkerBoxBlock
	                || block instanceof PointedDripstoneBlock
	                || block instanceof AmethystClusterBlock) {
	            return false;
	        }
	        try {
	            return Block.isShapeFullCube(state.getCollisionShape(null, null));
	        } catch (Exception ignored) {
	        }
	        return false;
    }
}
