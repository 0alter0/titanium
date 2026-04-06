package kaptainwutax.tungsten.path;

import net.minecraft.util.PlayerInput;

public class PathInput {

	public final boolean forward, back, right, left, jump, sneak, sprint;
	public final float pitch, yaw;

	public final BlockAction action;


	public PathInput(boolean forward, boolean back, boolean right, boolean left,
	                 boolean jump, boolean sneak, boolean sprint, float pitch, float yaw) {
		this(forward, back, right, left, jump, sneak, sprint, pitch, yaw, null);
	}

	public PathInput(boolean forward, boolean back, boolean right, boolean left,
	                 boolean jump, boolean sneak, boolean sprint,
	                 float pitch, float yaw, BlockAction action) {
		this.forward = forward;
		this.back    = back;
		this.right   = right;
		this.left    = left;
		this.jump    = jump;
		this.sneak   = sneak;
		this.sprint  = sprint;
		this.pitch   = pitch;
		this.yaw     = yaw;
		this.action  = action;
	}


	public PlayerInput getPlayerInput() {
		return new PlayerInput(forward, back, left, right, jump, sneak, sprint);
	}

	public boolean hasBlockAction() {
		return action != null;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\n");
		sb.append("forward: ").append(forward).append("\n");
		sb.append("back: ").append(back).append("\n");
		sb.append("right: ").append(right).append("\n");
		sb.append("left: ").append(left).append("\n");
		sb.append("jump: ").append(jump).append("\n");
		sb.append("sneak: ").append(sneak).append("\n");
		sb.append("sprint: ").append(sprint).append("\n");
		sb.append("pitch: ").append(pitch).append("\n");
		sb.append("yaw: ").append(yaw).append("\n");
		if (action != null) sb.append("action: ").append(action).append("\n");
		sb.append("}");
		return sb.toString();
	}
}
