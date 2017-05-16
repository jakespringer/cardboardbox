#version 330 core

layout(location = 0) in vec3 vertices;
layout(location = 1) in vec3 xyz;
layout(location = 2) in vec4 color;
layout(location = 3) in vec3 normals;

out vec4 blockColor;
out vec3 normal_cameraspace;
out vec3 lightDirection_cameraspace;

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;

void main()
{
    gl_Position = projectionMatrix * modelViewMatrix * vec4(xyz + vertices, 1.0);
    blockColor = color;
    normal_cameraspace = normals;
    lightDirection_cameraspace = vec3(200, 100, 50);
}
