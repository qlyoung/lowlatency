package com.sawtoothdev.mgoa;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import featherdev.mgoa.Mgoa;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "__low_latency__";
		cfg.useGL20 = false;
		cfg.width = 1280;
		cfg.height = 720;
		cfg.resizable = false;
		
		new LwjglApplication(new Mgoa(), cfg);
	}
}	