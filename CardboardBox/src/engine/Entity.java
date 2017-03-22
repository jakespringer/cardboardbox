package engine;

import java.util.ArrayList;
import java.util.List;

public abstract class Entity extends Destructible {

    private static final List<Entity> ENTITY_LIST = new ArrayList();

    public static void drawAll() {
        ENTITY_LIST.forEach(Entity::draw);
    }

    public static void updateAll() {
        new ArrayList<>(ENTITY_LIST).forEach(Entity::update);
    }

    @Override
    public void create() {
        ENTITY_LIST.add(this);
        super.create();
    }

    @Override
    public void destroy() {
        ENTITY_LIST.remove(this);
        super.create();
    }

    public void draw() {
    }

    public void update() {
    }
}
