package com.jakubflis.elektronik;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class JFElektronik extends Game {
	private static final String TAG = JFElektronik.class.getName();
    public WorldController worldController;
    public WorldRenderer worldRenderer;
	
	@Override
	public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        worldController = new WorldController(this);
        worldRenderer = new WorldRenderer(worldController);

        setScreen(new MenuScreen(this));
    }
}