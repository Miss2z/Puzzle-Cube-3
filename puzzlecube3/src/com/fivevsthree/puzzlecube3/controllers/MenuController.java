package com.fivevsthree.puzzlecube3.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.fivevsthree.puzzlecube3.events.MenuEvent;
import com.fivevsthree.puzzlecube3.events.MenuItemListener;
import com.fivevsthree.puzzlecube3.models.MenuModel;
import com.fivevsthree.puzzlecube3.views.MenuItem;
import com.fivevsthree.puzzlecube3.views.MenuView;

public class MenuController extends Controller<MenuModel> implements
		MenuItemListener {

	public MenuController() {
		this(new MenuModel());
	}

	public MenuController(MenuModel model) {
		super(model);

		view = new MenuView(model);
	}

	@Override
	public void load(AssetManager assets) {
		super.load(assets);

		for (MenuItem menuItem : model.menuItems) {
			menuItem.setListener(this);
		}
	}

	@Override
	public void show() {
		super.show();
		((MenuView) view).transitionIn(onTransitionInDone());
	}

	public void notifySignIn(boolean isSignedIn) {
		model.isSignedIn = isSignedIn;
	}

	public void notifyCanSignIn(boolean canSignIn) {
		model.canSignIn = canSignIn;
	}

	@Override
	public void onMenuItemClicked(int itemNumber) {
		final int menu = itemNumber;

		MenuEvent event;

		switch (itemNumber) {
		case MenuModel.MAIN:
		case MenuModel.PLAY:
		case MenuModel.NEW:
		case MenuModel.SIZE:
		case MenuModel.QUALITY:
			Gdx.input.setInputProcessor(null);
			((MenuView) view).transitionOut(new Action() {
				@Override
				public boolean act(float delta) {
					((MenuView) view).showMenu(menu);
					((MenuView) view).transitionIn(onTransitionInDone());
					return true;
				}
			});
			break;

		// Main Menu
		case MenuModel.EXIT:
			Gdx.input.setInputProcessor(null);
			((MenuView) view).transitionOut(onMenuItemExit());
			break;

		// Size Menu
		case MenuModel.SIZE_2:
		case MenuModel.SIZE_3:
		case MenuModel.SIZE_4:
		case MenuModel.SIZE_5:
		case MenuModel.SIZE_6:
		case MenuModel.SIZE_7:
			model.puzzleSize = itemNumber - MenuModel.SIZE;
			event = new MenuEvent(MenuEvent.SIZE);
			event.model = model;
			notifyListeners(event);
			break;

		case MenuModel.QUALITY_LOW:
		case MenuModel.QUALITY_MEDIUM:
		case MenuModel.QUALITY_HIGH:
			model.quality = itemNumber - MenuModel.QUALITY - 1;
			event = new MenuEvent(MenuEvent.QUALITY);
			event.model = model;
			notifyListeners(event);
			break;
		}
	}

	private Action onTransitionInDone() {
		return new Action() {
			@Override
			public boolean act(float delta) {
				Gdx.input.setInputProcessor(model.stage);
				return true;
			}
		};
	}

	private Action onMenuItemExit() {
		return new Action() {
			@Override
			public boolean act(float delta) {
				notifyListeners(new MenuEvent(MenuEvent.EXIT));
				return true;
			}
		};
	}
}
