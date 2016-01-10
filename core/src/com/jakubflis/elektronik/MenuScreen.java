package com.jakubflis.elektronik;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;

public class MenuScreen extends AbstractGameScreen {
    private static final String TAG = MenuScreen.class.getName();

    public MenuScreen(Game game) {
        super(game);
    }

    @Override
    public void render (float deltaTime) {
        Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f,0xed /
                255.0f, 0xff / 255.0f);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        if(Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));

            Gdx.app.debug(TAG, "TOUCHED");
        }
    }

    @Override public void resize (int width, int height) { }
    @Override public void show () { }
    @Override public void hide () { }
    @Override public void pause () { }
}
