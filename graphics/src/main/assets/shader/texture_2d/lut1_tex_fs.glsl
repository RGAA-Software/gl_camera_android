#version 320 es

precision mediump float;

in vec2 outTex;

uniform sampler2D lut;
uniform sampler2D image;

out vec4 FragColor;

void main() {

    highp vec3 origin = texture(image, outTex).rgb;

    highp float blueLevel = origin.b * 63.0;
    highp vec2 quad;

    quad.y = floor(floor(blueLevel) / 8.0);
    quad.x = floor(blueLevel) - (quad.y * 8.0);

    highp vec2 texCoord;
    texCoord.x = (quad.x * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * origin.r);
    texCoord.y = (quad.y * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * origin.g);

    vec4 lutColor = texture(lut, texCoord);
    FragColor = lutColor;
}
