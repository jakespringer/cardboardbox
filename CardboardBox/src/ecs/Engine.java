package ecs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Engine {
	private List<Entity> entities = new ArrayList<>();
	private List<EntitySystem> systems = new ArrayList<>();
	
	public List<Entity> getEntitiesFor(Family family) {
		return Collections.unmodifiableList(entities.stream()
				.filter(family::checkEntity)
				.collect(Collectors.toList()));
	}
	
	public Entity addEntity(Entity entity) {
		entities.add(entity);
		return entity;
	}
	
	public Entity removeEntity(Entity entity) {
		entities.remove(entity);
		return entity;
	}
	
	public EntitySystem addSystem(EntitySystem system) {
		systems.add(system);
		return system;
	}
	
	public EntitySystem removeSystem(EntitySystem system) {
		systems.remove(system);
		return system;
	}
	
	public void update(float dt) {
		Iterator<EntitySystem> systemsIterator = systems.iterator();
		while (systemsIterator.hasNext()) {
			EntitySystem system = systemsIterator.next();
			system.update(dt);
		}
	}
}
