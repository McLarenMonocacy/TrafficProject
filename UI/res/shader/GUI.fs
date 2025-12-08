#version 460

in vec2 UVs;
in vec4 frgColor;

uniform sampler2D textureSampler;

out vec4 outColor;

void main()
{
    outColor = frgColor  * texture(textureSampler, UVs);
}