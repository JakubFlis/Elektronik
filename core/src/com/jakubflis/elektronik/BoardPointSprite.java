package com.jakubflis.elektronik;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Klasa dziedzicząca po klasie Sprite, będącej odpowiednikiem
 * rysunku generowanego na ekranie, wzbogacona o flagi potrzebne
 * do prawidłowego działania gry.
 *
 * @author  Jakub Flis
 * @version 1.0
 */
public class BoardPointSprite extends Sprite {
    public boolean isCollected;
    public boolean isCollectable;
    public boolean isOverflowedTile;

    public BoardPointSprite(Texture texture) {
        super(texture);
    }
}
