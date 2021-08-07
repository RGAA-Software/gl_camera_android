#version 320 es

precision mediump float;

in vec2 outTex;

uniform sampler2D image;

out vec4 FragColor;

void main() {
    ivec2 texSize = textureSize(image, 0);
    vec2 position = gl_FragCoord.xy;
    float halfWidth = float(texSize.x) / 2.0;
    float halfHeight = float(texSize.y) / 2.0;

    vec2 texCoord = vec2(0.0, 0.0);
    if (position.x < halfWidth && position.y > halfHeight) {
        texCoord = vec2(position.x / halfWidth, (position.y - halfHeight)/halfHeight );
    } else if (position.x < halfWidth && position.y < halfHeight) {
        texCoord = vec2(position.x / halfWidth, 1.0 - (position.y - halfHeight)/halfHeight );
    } else if (position.x > halfWidth && position.y > halfHeight) {
        texCoord = vec2(1.0 - position.x / halfWidth, (position.y - halfHeight)/halfHeight );
    } else {
        texCoord = vec2(1.0 - position.x / halfWidth, 1.0 - (position.y - halfHeight)/halfHeight );
    }

    FragColor = texture(image, texCoord);
}
