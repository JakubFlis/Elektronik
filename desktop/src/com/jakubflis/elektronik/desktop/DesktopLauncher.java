package com.jakubflis.elektronik.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.jakubflis.elektronik.JFElektronik;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 800;
		config.height = 480;
		config.title = "Elektronik";
		config.useGL30 = false;
		
		new LwjglApplication(new JFElektronik(), config);
	}
}
