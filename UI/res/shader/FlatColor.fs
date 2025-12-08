#version 460 core

out vec4 fragColor;
in vec2 UVs;

struct Material
{
    vec4 diffuse;
    vec4 tintColor;
};

uniform sampler2D textureSampler;
uniform Material material;

void main(){
	fragColor = texture(textureSampler, UVs)*material.tintColor + material.diffuse;
}