package com.jakubflis.elektronik;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;

public class GameScreen extends AbstractGameScreen {
    private static final String TAG = GameScreen.class.getName();
    private WorldController _worldController;
    private WorldRenderer _worldRenderer;
    private boolean _isPaused;

    public GameScreen(Game game) {
        super(game);
    }

    @Override
    public void render (float deltaTime) {
        if (!_isPaused) {
            _worldController.update(deltaTime);
        }

        Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f,0xed /
                255.0f, 0xff / 255.0f);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        _worldRenderer.render();
    }

    @Override
    public void resize (int width, int height) {
        _worldRenderer.resize(width, height);
    }

    @Override
    public void show () {
        _worldController = new WorldController(game);
        _worldRenderer = new WorldRenderer(_worldController);
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void hide () {
        _worldRenderer.dispose();
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
