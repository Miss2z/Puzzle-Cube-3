package com.fivevsthree.puzzlecube3.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.fivevsthree.puzzlecube3.models.MenuModel;

public class MenuView extends View<MenuModel> {

	protected BitmapFont font;
	protected Table table;
	protected Array<MenuItem> currentMenu;
	protected Label fpsLabel;

	private IntMap<Array<MenuItem>> menuList;

	public MenuView(MenuModel model) {
		super(model);
		menuList = new IntMap<Array<MenuItem>>();
	}

	@Override
	public void create() {
		model.stage = new Stage();
		model.menuItems = new Array<MenuItem>();
	}

	@Override
	public void load(AssetManager assets) {
		font = assets.get("skins/default.fnt");
		font.getRegion().getTexture()
				.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		TextButtonStyle style = new TextButtonStyle();
		style.font = font;

		// Main Menu
		Array<MenuItem> mainMenu = new Array<MenuItem>();
		mainMenu.add(new MenuItem("Sign In", style, MenuModel.SIGN_IN));
		mainMenu.add(new MenuItem("Play", style, MenuModel.PLAY));
		mainMenu.add(new MenuItem("Options", style, MenuModel.OPTIONS));
		mainMenu.add(new MenuItem("Help", style, MenuModel.HELP));
		mainMenu.add(new MenuItem("Exit", style, MenuModel.EXIT));
		menuList.put(MenuModel.MAIN, mainMenu);
		model.menuItems.addAll(mainMenu);

		Array<MenuItem> playMenu = new Array<MenuItem>();
		playMenu.add(new MenuItem("Resume", style, MenuModel.RESUME));
		playMenu.add(new MenuItem("New", style, MenuModel.NEW));
		playMenu.add(new MenuItem("Load", style, MenuModel.LOAD));
		playMenu.add(new MenuItem("Save", style, MenuModel.SAVE));
		playMenu.add(new MenuItem("Back", style, MenuModel.MAIN));
		menuList.put(MenuModel.PLAY, playMenu);
		model.menuItems.addAll(playMenu);

		Array<MenuItem> newMenu = new Array<MenuItem>();
		newMenu.add(new MenuItem("Size", style, MenuModel.SIZE));
		newMenu.add(new MenuItem("Type", style, MenuModel.TYPE));
		newMenu.add(new MenuItem("Stickers", style, MenuModel.STICKER));
		newMenu.add(new MenuItem("Quality", style, MenuModel.QUALITY));
		newMenu.add(new MenuItem("Back", style, MenuModel.PLAY));
		menuList.put(MenuModel.NEW, newMenu);
		model.menuItems.addAll(newMenu);

		Array<MenuItem> sizeMenu = new Array<MenuItem>();
		sizeMenu.add(new MenuItem("2x2x2", style, MenuModel.SIZE_2));
		sizeMenu.add(new MenuItem("3x3x3", style, MenuModel.SIZE_3));
		sizeMenu.add(new MenuItem("4x4x4", style, MenuModel.SIZE_4));
		sizeMenu.add(new MenuItem("5x5x5", style, MenuModel.SIZE_5));
		sizeMenu.add(new MenuItem("6x6x6", style, MenuModel.SIZE_6));
		sizeMenu.add(new MenuItem("7x7x7", style, MenuModel.SIZE_7));
		sizeMenu.add(new MenuItem("Back", style, MenuModel.NEW));
		menuList.put(MenuModel.SIZE, sizeMenu);
		model.menuItems.addAll(sizeMenu);

		Array<MenuItem> qualityMenu = new Array<MenuItem>();
		qualityMenu.add(new MenuItem("Low", style, MenuModel.QUALITY_LOW));
		qualityMenu.add(new MenuItem("Medium", style, MenuModel.QUALITY_MEDIUM));
		qualityMenu.add(new MenuItem("High", style, MenuModel.QUALITY_HIGH));
		qualityMenu.add(new MenuItem("Back", style, MenuModel.NEW));
		menuList.put(MenuModel.QUALITY, qualityMenu);
		model.menuItems.addAll(qualityMenu);
		
		LabelStyle labelStyle = new LabelStyle(font, Color.WHITE);
		fpsLabel = new Label("0", labelStyle);
		
		Timer.schedule(new Task() {
			@Override
			public void run() {
				fpsLabel.setText(String.valueOf(Gdx.graphics.getFramesPerSecond()));
			}
		}, 0, 1);
	}

	@Override
	public void show() {
		showMenu(MenuModel.MAIN);
	}

	@Override
	public void render(float delta) {
		model.stage.act(delta);
		model.stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		model.stage.setViewport(width, height, true);
	}

	@Override
	public void dispose() {
		if (font != null) {
			font.dispose();
		}
		if (model.stage != null) {
			model.stage.clear();
		}
	}

	protected void updateMenu() {
		table = new Table();
		table.setFillParent(true);
		table.setTransform(false);
		table.setTouchable(Touchable.childrenOnly);

		for (MenuItem menuItem : currentMenu) {
			table.row().expandX();
			table.add(menuItem).center();
		}

		model.stage.clear();
		model.stage.addActor(table);
		model.stage.addActor(fpsLabel);
	}

	public void transitionOut(Action action) {
		int i = 1;
		for (MenuItem menuItem : currentMenu) {
			menuItem.addAction(Actions.parallel(
					Actions.moveBy(200 * i, 0, 0.25f), Actions.fadeOut(0.25f)));
			i *= -1;
		}

		if (action != null) {
			currentMenu.first().addAction(Actions.after(action));
		}
	}

	public void transitionIn(Action action) {
		int i = -1;
		for (MenuItem menuItem : currentMenu) {
			menuItem.addAction(Actions.sequence(Actions.alpha(0), Actions
					.moveBy(200 * i, 0), Actions.visible(true), Actions
					.parallel(Actions.moveBy(200 * -i, 0, 0.25f),
							Actions.fadeIn(0.25f))));
			i *= -1;
		}

		if (action != null) {
			currentMenu.first().addAction(Actions.after(action));
		}
	}

	public void showMenu(int menu) {
		if (menuList.containsKey(menu)) {
			currentMenu = menuList.get(menu);
			for (MenuItem menuItem : currentMenu) {
				menuItem.setVisible(false);
			}
			updateMenu();
		}
	}

	public void notifyBackEvent() {

	}

}
