package com.jakubflis.elektronik;

import com.badlogic.gdx.Screen;

/**
 * Abstrakcyjna klasa implementąca interfejs Screen,
 * będący odpowiednikiem ekranu wyświetlanego w oknie
 * gry.
 *
 * @author  Jakub Flis
 * @version 1.0
 */
public class AbstractGameScreen implements Screen {
    protected JFElektronik game;

    public AbstractGameScreen (JFElektronik game) {
        this.game = game;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
