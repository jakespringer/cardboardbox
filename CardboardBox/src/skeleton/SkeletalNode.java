package skeleton;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public class SkeletalNode {
	public Vector3f relativePosition;
	public Quaternionf relativeOrientation;
	
	public SkeletalNode(SkeletalNode parent, SkeletalNode[] children, Vector3f relativePosition, Quaternionf relativeOrientation) {
		this.relativePosition = relativePosition;
		this.relativeOrientation = relativeOrientation;
	}
}
