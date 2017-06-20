package com.fivevsthree.puzzlecube3.views;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.fivevsthree.puzzlecube3.models.LoadingModel;

public class LoadingView extends View<LoadingModel> {

	private Stage stage;
	private Texture texture;
	private Image image;
	private Table table;

	public LoadingView(LoadingModel model) {
		super(model);
	}

	@Override
	public void create() {
		stage = new Stage();
	}

	@Override
	public void load(AssetManager assets) {
		texture = assets.get("textures/splash.png");
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		image = new Image(texture);
		if (model != null)
			model.image = image;

		table = new Table();
		table.setFillParent(true);
		table.setTransform(false);
		table.add(image).center();
	}

	@Override
	public void show() {
		stage.clear();
		stage.addActor(table);
	}

	@Override
	public void render(float delta) {
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, true);
	}

	@Override
	public void dispose() {
		if (stage != null) {
			stage.dispose();
		}
	}

}
