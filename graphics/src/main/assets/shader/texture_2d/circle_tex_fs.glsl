#version 320 es

precision mediump float;

in vec2 outTex;

uniform sampler2D image;

out vec4 FragColor;

void main() {
    ivec2 _texSize = textureSize(image, 0);
    vec2 texSize = vec2(_texSize.x, _texSize.y);
    vec2 position = gl_FragCoord.xy;
    vec2 center = texSize/2.0;

    float radius = min(center.x, center.y);

    vec4 color = texture(image, outTex);

    if (length(position - center) > radius) {
        discard;
    }

    FragColor = color;
}
