package com.fivevsthree.puzzlecube3.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.utils.Array;

public class PuzzleModel extends Model {

	public static final Color DEFAULT_TOP_COLOR = new Color(1f, 0.1f, 0.1f, 1f);
	public static final Color DEFAULT_BOTTOM_COLOR = new Color(0.5f, 0.3f, 0.8f, 1f);
	public static final Color DEFAULT_RIGHT_COLOR = new Color(0f, 0.8f, 0.2f, 1f);
	public static final Color DEFAULT_LEFT_COLOR = new Color(0f, 0.4f, 0.9f, 1f);
	public static final Color DEFAULT_FRONT_COLOR = new Color(1f, 1f, 1f, 1f);
	public static final Color DEFAULT_BACK_COLOR = new Color(1f, 1f, 0.2f, 1f);
	
	public PerspectiveCamera camera;

	public Array<ModelInstance> cubes;

	public int puzzleSize = 7;
	public int quality = 3;

	public Color topColor = DEFAULT_TOP_COLOR;
	public Color bottomColor = DEFAULT_BOTTOM_COLOR;
	public Color rightColor = DEFAULT_RIGHT_COLOR;
	public Color leftColor = DEFAULT_LEFT_COLOR;
	public Color frontColor = DEFAULT_FRONT_COLOR;
	public Color backColor = DEFAULT_BACK_COLOR;

}
