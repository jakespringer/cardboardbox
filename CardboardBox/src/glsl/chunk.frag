#version 330 core

in vec3 outTexCoord;

out vec4 color;

uniform sampler3D texture_sampler;

void main() {
    //color = vec4(1.0f, 0.5f, 0.2f, 1.0f);
    //color = texture(colors, outTexCoord);
    color = vec4(outTexCoord/100, 1.0f) + texture(texture_sampler, outTexCoord);
}