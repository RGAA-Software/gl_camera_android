#version 320 es

precision mediump float;

in vec2 outTex;

uniform sampler2D image;

uniform float texOffset;

out vec4 FragColor;

void main() {
    vec4 origin = texture(image, outTex);
    vec4 up = texture(image, outTex + vec2(texOffset, texOffset));
    vec4 down = texture(image, outTex - vec2(texOffset, texOffset));
    FragColor = vec4(origin.r, up.g, down.b, 1.0);
}
