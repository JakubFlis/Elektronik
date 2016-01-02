package com.jakubflis.elektronik;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class BoardPointSprite extends Sprite {
    public boolean isCollected;
    public boolean isCollectable;

    public BoardPointSprite(Texture texture) {
        super(texture);
    }
}
