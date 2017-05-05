#version 330 core

out vec4 color;

uniform sampler2D texture_sampler;

void main()
{
	color = texture(texture_sampler, vec2(0, 0.5));
}
