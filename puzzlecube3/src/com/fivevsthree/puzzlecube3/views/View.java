package com.fivevsthree.puzzlecube3.views;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Disposable;

public abstract class View<M> implements Disposable {

	protected M model;

	public View(M model) {
		this.model = model;
	}

	public void create() {
	}

	public void load(AssetManager assets) {
	}

	public void show() {
	}

	public void hide() {
	}

	public void pause() {
	}

	public void resume() {
	}

	public void render(float delta) {
	}

	public void resize(int width, int height) {
	}

	@Override
	public void dispose() {
	}

}
