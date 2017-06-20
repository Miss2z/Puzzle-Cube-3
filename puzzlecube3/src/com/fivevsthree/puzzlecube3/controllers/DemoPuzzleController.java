package com.fivevsthree.puzzlecube3.controllers;

import com.badlogic.gdx.math.Vector3;
import com.fivevsthree.puzzlecube3.models.PuzzleModel;
import com.fivevsthree.puzzlecube3.views.PuzzleView;

public class DemoPuzzleController extends Controller<PuzzleModel> {

	private Vector3 right;

	public DemoPuzzleController() {
		this(new PuzzleModel());
	}

	public DemoPuzzleController(PuzzleModel model) {
		super(model);
		view = new PuzzleView(model);
		right = new Vector3();
	}

	@Override
	public void render(float delta) {
		right.set(model.camera.direction).crs(model.camera.up).nor();
		model.camera.rotateAround(Vector3.Zero, right, delta * 50);
		model.camera.rotateAround(Vector3.Zero, Vector3.Y, delta * 50);
		model.camera.update();
		super.render(delta);
	}
}
