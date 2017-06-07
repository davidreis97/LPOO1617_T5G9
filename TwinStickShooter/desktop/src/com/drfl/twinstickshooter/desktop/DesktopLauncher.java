package com.drfl.twinstickshooter.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.drfl.twinstickshooter.TSSGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "DroidStick";
		config.useGL30 = false;
		config.width = 1280;
		config.height = 720;
//        config.width = 1920;
//        config.height = 1080;
//        config.width = 1024;
//        config.height = 768;
		config.resizable = false;
		
		new LwjglApplication(new TSSGame(), config);
	}
}