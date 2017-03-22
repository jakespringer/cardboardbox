package opengl;


import engine.Activatable;
import engine.Destructible;
import static org.lwjgl.opengl.ARBVertexArrayObject.*;

public class VertexArrayObject extends Destructible implements Activatable {

    private final int VAO;

    public static VertexArrayObject createVAO(Runnable r) {
        VertexArrayObject VAO = new VertexArrayObject() {
            @Override
            public void createInner() {
                activate();
                r.run();
                deactivate();
            }
        };
        VAO.create();
        return VAO;
    }

    private VertexArrayObject() {
        VAO = glGenVertexArrays();
    }

    @Override
    public void activate() {
        glBindVertexArray(VAO);
    }

    @Override
    public void deactivate() {
        glBindVertexArray(0);
    }

    @Override
    protected void destroyInner() {
        glDeleteVertexArrays(VAO);
    }
}
