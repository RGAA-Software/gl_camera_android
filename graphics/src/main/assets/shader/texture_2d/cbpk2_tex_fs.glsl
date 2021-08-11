#version 320 es

precision mediump float;

in vec2 outTex;

uniform sampler2D image;

uniform float texOffset;
const int kLines = 100;
uniform float offsetLines[kLines];

out vec4 FragColor;


void main() {
    vec4 origin = texture(image, outTex);

    float line = gl_FragCoord.y;
    int index = int(line)/50 % kLines;
    float value = offsetLines[index]/15.0;

    //vec4 move = texture(image, outTex + vec2(0.0, value));
    vec4 move = texture(image, outTex + vec2(value, 0.0));
    FragColor = vec4(origin.r, move.g, move.b, 1.0);

}
