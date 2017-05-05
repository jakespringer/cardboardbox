#version 330 core

layout(location = 0) in vec3 vertices;
layout(location = 1) in vec3 xyz;
layout(location = 2) in vec4 color;

out vec4 blockColor;

uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

void main()
{
    gl_Position = projectionMatrix * viewMatrix * vec4(xyz + vertices, 1.0);
    blockColor = color;
}
