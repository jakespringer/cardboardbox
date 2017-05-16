#version 330 core

layout (location = 0) in vec3 position;

out vec2 uv;

uniform mat4 modelMatrix;
uniform mat4 projectionMatrix;

void main()
{
	// not using view matrix, since sun should render in static cameraspace
    gl_Position = projectionMatrix * modelMatrix * vec4(position, 1.0);
    uv = position.xy;
}
