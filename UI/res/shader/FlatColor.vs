#version 460 core

layout (location = 0) in vec3 inPos;
layout (location = 1) in vec2 inUVs;

out vec2 UVs;

uniform mat4 projectionMatrix;
uniform mat4 modelMatrix;
uniform mat4 cameraMatrix;

void main(){
	gl_Position = projectionMatrix * cameraMatrix * modelMatrix * vec4(inPos, 1.0);
	UVs = inUVs;
}