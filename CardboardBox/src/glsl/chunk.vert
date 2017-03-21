#version 330 core

layout (location = 0) in vec3 position;

out vec3 outTexCoord;

uniform mat4 worldMatrix;
uniform mat4 projectionMatrix;

void main() {
    gl_Position = projectionMatrix * worldMatrix * vec4(position, 1.0);
    outTexCoord = position;
}
