package com.jakubflis.elektronik;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL30;

public class GameScreen extends AbstractGameScreen {
    private boolean _isPaused;

    public GameScreen(JFElektronik game) {
        super(game);
    }

    public enum GameState {
        GAME_WON, GAME_LOST, GAME_ON
    }

    @Override
    public void render (float deltaTime) {
        if (!_isPaused) {
            game.worldController.update(deltaTime);
        }

        Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f,0xed /
                255.0f, 0xff / 255.0f);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        game.worldRenderer.render();
    }

    @Override
    public void resize (int width, int height) {
        game.worldRenderer.resize(width, height);
    }

    @Override
    public void show () {
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void hide () {
        game.worldRenderer.dispose();
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void pause () {
        _isPaused = true;
    }

    @Override
    public void resume () {
        super.resume();
        _isPaused = false;
    }
}
