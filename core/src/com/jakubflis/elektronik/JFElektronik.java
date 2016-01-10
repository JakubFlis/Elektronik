package com.jakubflis.elektronik;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class JFElektronik extends Game {
	private static final String TAG = JFElektronik.class.getName();
	
	@Override
	public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        setScreen(new MenuScreen(this));
    }
}