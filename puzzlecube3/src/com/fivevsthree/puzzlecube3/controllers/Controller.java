package com.fivevsthree.puzzlecube3.controllers;

import java.util.ArrayList;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Disposable;
import com.fivevsthree.puzzlecube3.events.Event;
import com.fivevsthree.puzzlecube3.events.Listener;
import com.fivevsthree.puzzlecube3.views.View;

public abstract class Controller<M> implements Disposable {

	protected M model;
	protected View<M> view;

	protected final ArrayList<Listener> listeners;

	public M getModel() {
		return model;
	}

	public Controller(M model) {
		this.model = model;

		listeners = new ArrayList<Listener>();
	}

	public void create() {
		if (view != null) {
			view.create();
		}
	}

	public void load(AssetManager assets) {
		if (view != null) {
			view.load(assets);
		}
	}

	public void show() {
		if (view != null) {
			view.show();
		}
	}

	public void hide() {
		if (view != null) {
			view.hide();
		}
	}

	public void pause() {
		if (view != null) {
			view.pause();
		}
	}

	public void resume() {
		if (view != null) {
			view.resume();
		}
	}

	public void render(float delta) {
		if (view != null) {
			view.render(delta);
		}
	}

	public void resize(int width, int height) {
		if (view != null) {
			view.resize(width, height);
		}
	}

	@Override
	public void dispose() {
		if (listeners != null) {
			synchronized (listeners) {
				listeners.clear();
			}
		}
		if (view != null) {
			view.dispose();
		}
	}

	public void addListener(Listener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	public void removeListener(Listener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	protected void notifyListeners(Event event) {
		synchronized (listeners) {
			for (Listener listener : listeners) {
				listener.handle(this, event);
			}
		}
	}
}
