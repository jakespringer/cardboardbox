package skeleton;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import struct.Tree;

public class Skeleton {
	private Tree<SkeletalNode> root;
	
	private Skeleton(Tree<SkeletalNode> root) {
		this.root = root;
	}
	
	public static Skeleton fromSkeletalTree(Tree<SkeletalNode> root) {
		return new Skeleton(root);
	}
	
	public Tree<EvaluatedSkeletalNode> evaluate() {
		return root.mapReduce(new EvaluatedSkeletalNode(new Vector3f(), new Quaternionf()), (parent, elem) -> {
			Quaternionf absoluteOrientation = parent.absoluteOrientation.mul(elem.relativeOrientation, new Quaternionf());
			Vector3f output = new Vector3f();
			Vector3f absolutePosition = parent.absolutePosition
					.add(absoluteOrientation.transform(elem.relativePosition, output), output);
			return new EvaluatedSkeletalNode(absolutePosition, absoluteOrientation);
		});
	}
}
