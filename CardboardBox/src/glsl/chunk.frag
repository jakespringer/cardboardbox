#version 330 core

in vec4 blockColor;
in vec3 normal_cameraspace;
in vec3 lightDirection_cameraspace;

out vec4 color;

void main() {
	vec3 n = normalize(normal_cameraspace);
	vec3 l = normalize(lightDirection_cameraspace);
	float cosTheta = clamp(dot(n, l), 0.0, 1.0);

	color = blockColor * cosTheta;
}
