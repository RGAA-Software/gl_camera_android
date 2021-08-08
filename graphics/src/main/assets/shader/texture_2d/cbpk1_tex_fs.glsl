#version 320 es

precision mediump float;

in vec2 outTex;

uniform sampler2D image;

uniform float texOffset;
const int kLines = 1000;
uniform float offsetLines[kLines];

out vec4 FragColor;


void main() {
    vec4 origin = texture(image, outTex);

    float line = gl_FragCoord.y;
    int index = int(line)%kLines;
    float value = offsetLines[index]/35.0;

    vec4 move = texture(image, outTex + vec2(value, 0.0));
    FragColor = vec4(move.r, move.g, origin.b, 1.0);

}
