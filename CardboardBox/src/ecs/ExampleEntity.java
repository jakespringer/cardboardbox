package ecs;

import org.joml.Vector3f;

public class ExampleEntity /* implements Entity */ {
	private Component<Vector3f> position = new Component<>(new Vector3f(1, 4, 10));
	private Component<Vector3f> velocity = new Component<>(new Vector3f(0, 1, 0));
	
	public ExampleEntity() {
		Systems.physics.addEntity(position, velocity);
		// the following might allow the removal of the entity from the system?
		// Systems.physics.addEntity(this, position, velocity);
	}
	
	public void destroy() {
		// Systems.physics.removeEntity(this);
	}
}
