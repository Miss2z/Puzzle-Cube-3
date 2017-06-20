package com.fivevsthree.puzzlecube3;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.Model;
import com.fivevsthree.puzzlecube3.controllers.Controller;
import com.fivevsthree.puzzlecube3.controllers.DemoPuzzleController;
import com.fivevsthree.puzzlecube3.controllers.LoadingController;
import com.fivevsthree.puzzlecube3.controllers.MenuController;
import com.fivevsthree.puzzlecube3.events.Event;
import com.fivevsthree.puzzlecube3.events.GameHelperListener;
import com.fivevsthree.puzzlecube3.events.Listener;
import com.fivevsthree.puzzlecube3.events.LoadingEvent;
import com.fivevsthree.puzzlecube3.events.MenuEvent;
import com.fivevsthree.puzzlecube3.models.MenuModel;
import com.fivevsthree.puzzlecube3.models.PuzzleModel;

public class PuzzleCube extends Game implements Listener, GameHelperListener {

	private PlatformHandler platformHandler;

	private AssetManager assets;

	private ArrayList<Controller<?>> screens;
	private ArrayList<Controller<?>> screensToLoad;
	private ArrayList<Controller<?>> screensToRender;
	private ArrayList<Controller<?>> renderScreens;

	private LoadingController loadingController;
	private DemoPuzzleController demoPuzzleController;
	private MenuController menuController;

	private PuzzleModel demoPuzzleModel;

	private boolean stillLoading;

	public PuzzleCube(PlatformHandler platformHandler) {
		this.platformHandler = platformHandler;

		assets = new AssetManager();

		screens = new ArrayList<Controller<?>>();
		screensToLoad = new ArrayList<Controller<?>>();
		screensToRender = new ArrayList<Controller<?>>();
		renderScreens = new ArrayList<Controller<?>>();

		loadingController = new LoadingController();
		loadingController.addListener(this);

		demoPuzzleModel = new PuzzleModel();
		demoPuzzleController = new DemoPuzzleController(demoPuzzleModel);

		menuController = new MenuController();
		menuController.addListener(this);

		// Controllers will be managed for us
		screens.add(demoPuzzleController);
		screens.add(menuController);

		// Load called for all screens after assets have finished loading
		for (Controller<?> screen : screens) {
			screensToLoad.add(screen);
		}

		// We will manually call Load for this controller
		screens.add(loadingController);

		stillLoading = true;
	}

	@Override
	public void create() {
		if (!Gdx.graphics.isGL20Available()) {
			Gdx.app.exit();
		}

		// Wait for loading assets
		assets.load("textures/loadingScreen.png", Texture.class);
		assets.finishLoading();

		// Notify loading controller that assets are ready
		loadingController.load(assets);

		// Queue the reset of the assets
		assets.load("skins/default.fnt", BitmapFont.class);
		assets.load("models/cube-low.g3db", Model.class);
		assets.load("models/cube-high.g3db", Model.class);
		assets.load("textures/mask.png", Texture.class);

		synchronized (screens) {
			for (Controller<?> screen : screens) {
				screen.create();
			}
		}

		show(loadingController);
	}

	public void show(Controller<?>... screens) {
		synchronized (screensToRender) {
			for (Controller<?> screen : screens) {
				screensToRender.add(screen);
				screen.show();
			}
		}
	}

	public void show(int index, Controller<?> screen) {
		synchronized (screensToRender) {
			screensToRender.add(index, screen);
			screen.show();
		}
	}

	public void hide(Controller<?>... screens) {
		synchronized (screensToRender) {
			for (Controller<?> screen : screens) {
				screensToRender.remove(screen);
				screen.hide();
			}
		}
	}

	@Override
	public void pause() {
		synchronized (screens) {
			for (Controller<?> screen : screens) {
				screen.pause();
			}
		}
	}

	@Override
	public void resume() {
		synchronized (screens) {
			for (Controller<?> screen : screens) {
				screen.resume();
			}
		}
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		if (stillLoading && assets.update()) {
			// Assets have finished loading
			loadingController.notifyLoadingDone();

			if (screensToLoad.size() > 0) {
				// Notify all screens that assets are ready
				synchronized (screensToLoad) {
					for (Controller<?> screen : screensToLoad) {
						screen.load(assets);
					}
					// We only want to call Load once
					screensToLoad.clear();
					screensToLoad.trimToSize();
				}

				stillLoading = false;
			}
		}

		// Create a temporary list of screens to render to prevent concurrency
		// exceptions
		renderScreens.addAll(screensToRender);
		for (Controller<?> screen : renderScreens) {
			screen.render(Gdx.graphics.getDeltaTime());
		}
		renderScreens.clear();
	}

	@Override
	public void resize(int width, int height) {
		int rotation = Gdx.input.getRotation();

		// Adjust resolution based on orientation
		switch (Gdx.input.getNativeOrientation()) {
		case Landscape:
			if (rotation == 0 || rotation == 180) {
				width = 1280;
				height = 720;
			} else {
				width = 720;
				height = 1280;
			}
			break;

		case Portrait:
			if (rotation == 0 || rotation == 180) {
				width = 720;
				height = 1280;
			} else {
				width = 1280;
				height = 720;
			}
			break;
		}

		synchronized (screens) {
			for (Controller<?> screen : screens) {
				screen.resize(width, height);
			}
		}
	}

	@Override
	public void dispose() {
		if (screens != null) {
			screens.clear();
		}
		if (screensToRender != null) {
			screensToRender.clear();
		}
		if (renderScreens != null) {
			renderScreens.clear();
		}
		synchronized (screens) {
			for (Controller<?> screen : screens) {
				screen.dispose();
			}
		}
		if (assets != null) {
			assets.dispose();
		}
	}

	@Override
	public boolean handle(Object sender, Event event) {
		// Handle events for loading controller
		if (sender.equals(loadingController)) {
			switch (event.type) {
			case LoadingEvent.DONE:
				// Loading is done so display menu
				hide(loadingController);
				show(demoPuzzleController, menuController);
				return true;
			}
		}

		MenuModel menuModel = (MenuModel) event.model;

		// Handle events for menu controller
		if (sender.equals(menuController)) {
			switch (event.type) {
			case MenuEvent.SIGN_IN:
				platformHandler.signIn();
				return true;

			case MenuEvent.SIZE:
				if (menuModel != null
						&& demoPuzzleModel.puzzleSize != menuModel.puzzleSize) {
					hide(demoPuzzleController);
					demoPuzzleModel.puzzleSize = menuModel.puzzleSize;
					show(0, demoPuzzleController);
				}
				return true;

			case MenuEvent.QUALITY:
				if (menuModel != null
						&& demoPuzzleModel.quality != menuModel.quality) {
					hide(demoPuzzleController);
					demoPuzzleModel.quality = menuModel.quality;
					show(0, demoPuzzleController);
				}
				return true;

			case MenuEvent.EXIT:
				// Exit the game
				Gdx.app.exit();
				return true;
			}
		}

		return false;
	}

	@Override
	public void onSignInFailed() {
		menuController.notifySignIn(false);
	}

	@Override
	public void onSignInSucceeded() {
		hide(menuController);
		menuController.notifySignIn(true);
		show(menuController);
	}

}
