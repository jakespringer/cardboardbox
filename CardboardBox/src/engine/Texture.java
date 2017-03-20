package engine;


import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class Texture extends Destructible implements Activatable {

    private int texture;
    private int type;

    public Texture(int type) {
        texture = glGenTextures();
        this.type = type;
        activate();
        glTexParameteri(type, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(type, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glGenerateMipmap(type);
        deactivate();
    }

    @Override
    public void activate() {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(type, texture);
    }

    @Override
    public void deactivate() {
        glBindTexture(type, 0);
    }

    public int getID() {
        return texture;
    }
}
