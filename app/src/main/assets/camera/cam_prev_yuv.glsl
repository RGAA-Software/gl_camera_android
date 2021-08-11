#version 300 es
precision mediump float;

uniform sampler2D yImage;
uniform sampler2D uImage;
uniform sampler2D vImage;

in vec2 outTex;

out vec4 fragColor;

void main() {
    fragColor = vec4( vec3(texture(yImage, outTex).r), 1.0);
}