#version 300 es
#extension GL_OES_EGL_image_external_essl3 : require
precision mediump float;

uniform samplerExternalOES frameImage;
in vec2 outTex;

out vec4 fragColor;

void main() {
    fragColor = texture(frameImage, outTex);
    if (gl_FragCoord.x < 100.0) {
        fragColor = vec4(1.0, 1.0, 1.0, 1.0);
    }
}