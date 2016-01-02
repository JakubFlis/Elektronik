package com.jakubflis.elektronik;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class JFElektronik extends ApplicationAdapter {
	private static final String TAG = JFElektronik.class.getName();
    private WorldController _worldController;
    private WorldRenderer _worldRenderer;
    private boolean _isPaused;
	
	@Override
	public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        _worldController = new WorldController();
        _worldRenderer = new WorldRenderer(_worldController);
        _isPaused = false;
	}

	@Override
	public void render() {
        if (!_isPaused) {
            _worldController.update(Gdx.graphics.getDeltaTime());
        }

        Gdx.gl.glClearColor(0x64/255.0f, 0x95/255.0f, 0xed/255.0f, 0xff/255.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        _worldRenderer.render();
	}

    @Override
    public void resize (int width, int height) {
        _worldRenderer.resize(width, height);
    }

    @Override
    public void dispose() {
        _worldRenderer.dispose();
    }

    @Override
    public void pause() {
        _isPaused = true;
    }

    @Override
    public void resume() {
        _isPaused = false;
    }
}