package ecs;

import java.util.ArrayList;
import java.util.List;

public abstract class EntitySystem {
	protected List<Entity> entities = new ArrayList<>();
	
	public void add(Entity entity) {
		entities.add(entity);
	}
	
	public void remove(Entity entity) {
		entities.remove(entity);
	}
	
	public abstract void initialize(Engine engine);
	public abstract void update(float dt);
}
