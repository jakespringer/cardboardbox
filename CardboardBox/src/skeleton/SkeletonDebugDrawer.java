package skeleton;

import java.util.List;

import ecs.ComponentMapper;
import ecs.Engine;
import ecs.Entity;
import ecs.EntitySystem;
import ecs.Family;

public class SkeletonDebugDrawer extends EntitySystem {
	private List<Entity> entities;
	private ComponentMapper<SkeletonDebugComponent> sdc = ComponentMapper.getFor(SkeletonDebugComponent.class);
	
	@Override
	public void initialize(Engine engine) {
		entities = engine.getEntitiesFor(Family.require(SkeletonDebugComponent.class));
	}
	
	@Override
	public void update(float dt) {
		for (Entity e : entities) {
			SkeletonDebugComponent skeletonDebugComponent = sdc.get(e);
			
		}
	}
}
