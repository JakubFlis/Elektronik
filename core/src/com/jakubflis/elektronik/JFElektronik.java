package com.jakubflis.elektronik;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class JFElektronik extends Game {
	private static final String TAG = JFElektronik.class.getName();
    public WorldController worldController;
    public WorldRenderer worldRenderer;
	
	@Override
	public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        HighscoreManager.totalBlowingTime = 0.0f;
        HighscoreManager.totalTime = 0L;
        HighscoreManager.username = "Elektronik";

        worldController = new WorldController(this);
        worldRenderer = new WorldRenderer(worldController);

        setScreen(new MenuScreen(this));
    }
}