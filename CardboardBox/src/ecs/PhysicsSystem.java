package ecs;

import org.joml.Vector3f;

public class PhysicsSystem extends AbstractSystem implements PairSystem<Vector3f, Vector3f> {
	
	@Override
	public void step(float dt) {
		// doesn't need to do anything here
	}

	@Override
	public void stepEntity(Component<Vector3f> position, Component<Vector3f> velocity, float dt) {
		position.value.fma(dt, velocity.value);
		System.out.println(position.value.toString());
	}
}
