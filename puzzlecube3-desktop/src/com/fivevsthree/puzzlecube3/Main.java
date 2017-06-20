package com.fivevsthree.puzzlecube3;

import java.text.DateFormat;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main implements PlatformHandler {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Puzzle Cube 3";
		cfg.useGL20 = true;
		cfg.width = 1280;
		cfg.height = 720;

		new LwjglApplication(new PuzzleCube(new Main()), cfg);
	}

	@Override
	public DateFormat getDateFormat() {
		return DateFormat.getDateInstance();
	}

	@Override
	public void signIn() {
	}

	@Override
	public void signOut() {
	}

	@Override
	public boolean getSignedIn() {
		return false;
	}

}
