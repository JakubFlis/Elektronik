package com.jakubflis.elektronik;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;

/**
 * Klasa dziedzicząca po AbstractGameScreen. Określa widok dostępny
 * na ekranie od razu po uruchomieniu gry.
 *
 * @author  Jakub Flis
 * @version 1.0
 */
public class MenuScreen extends AbstractGameScreen {
    private static final String TAG = MenuScreen.class.getName();

    public MenuScreen(JFElektronik game) {
        super(game);
    }

    @Override
    public void render (float deltaTime) {
        Gdx.gl.glClearColor(0x6a / 255.0f, 0x65 / 255.0f, 0x65 / 255.0f, 0xff / 255.0f);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        if(Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
        }

       game.worldRenderer.renderMenu();
    }

    @Override public void resize (int width, int height) {
        game.worldRenderer.resize(width, height);
    }

    @Override public void show () { }
    @Override public void hide () { }
    @Override public void pause () { }
}
