#version 300 es
layout (location = 0) in vec2 aPos;
layout (location = 1) in vec2 aTex;

uniform mat4 proj;
uniform mat4 model;
uniform mat4 uCamTexMatrix;
out vec2 outTex;

void main() {
    gl_Position  = proj * model * vec4(aPos, 0.0, 1.0);

    outTex = (uCamTexMatrix * vec4(aTex, 0.0, 1.0)).xy;
}