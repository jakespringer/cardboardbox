package ecs;

public class TestMain {
	public static void main(String[] args) {
		ExampleEntity e = new ExampleEntity();
		Systems.physics.step(1);
		Systems.physics.getEntityList().forEach(p -> {
			Systems.physics.stepEntity(p.getValue0(), p.getValue1(), 1);
		});
	}
}
