#version 320 es

precision mediump float;

in vec2 outTex;

uniform sampler2D image;

out vec4 FragColor;

void main() {
    ivec2 _texSize = textureSize(image, 0);
    vec2 texSize = vec2(_texSize.x, _texSize.y);
    vec2 position = gl_FragCoord.xy;

    vec4 color = texture(image, outTex);
    if (position.x < texSize.x/2.0 && position.y < texSize.y/2.0) {
        FragColor = color * vec4(1.0, 0.0, 0.0, 1.0);
    } else if (position.x > texSize.x/2.0 && position.y < texSize.y/2.0) {
        FragColor = color * vec4(0.0, 1.0, 0.0, 1.0);
    } else if (position.x > texSize.x/2.0 && position.y > texSize.y/2.0) {
        FragColor = color * vec4(0.0, 0.0, 1.0, 1.0);
    } else {
        FragColor = color * vec4(0.0, 1.0, 1.0, 1.0);
    }
}
