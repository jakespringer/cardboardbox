package skeleton;

import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class EvaluatedSkeletalNode {
	public final Vector3fc absolutePosition;
	public final Quaternionfc absoluteOrientation;
	
	/* package */ EvaluatedSkeletalNode(Vector3f absolutePosition, Quaternionf absoluteOrientation) {
		this.absolutePosition = absolutePosition;
		this.absoluteOrientation = absoluteOrientation;
	}
}
