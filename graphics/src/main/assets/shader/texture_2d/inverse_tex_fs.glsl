#version 320 es

precision mediump float;

in vec2 outTex;

uniform sampler2D image;

out vec4 FragColor;

void main() {
    FragColor = texture(image, outTex);
    FragColor = vec4(1.0 - FragColor.r, 1.0 - FragColor.g, 1.0 - FragColor.b, 1.0);
}
