#version 320 es

precision mediump float;

in vec2 outTex;

uniform sampler2D image;

out vec4 FragColor;

void main() {
    ivec2 texSize = textureSize(image, 0);
    vec2 position = gl_FragCoord.xy;
    int kernelSize = 17;
    int step = kernelSize/2;
    int xCount = int(position.x)/kernelSize;
    int yCount = int(position.y)/kernelSize;

    float xStep = 1.0/float(texSize.x);
    float yStep = 1.0/float(texSize.y);

    FragColor = texture(image, vec2( float(xCount*kernelSize + step)*xStep , float(yCount*kernelSize + step) * yStep ));
}
