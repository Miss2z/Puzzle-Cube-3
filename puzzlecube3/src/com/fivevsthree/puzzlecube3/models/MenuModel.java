package com.fivevsthree.puzzlecube3.models;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.fivevsthree.puzzlecube3.views.MenuItem;

public class MenuModel extends Model {

	public Stage stage;

	public Array<MenuItem> menuItems;

	public int puzzleSize;
	public int quality;

	public boolean canSignIn = false;
	public boolean isSignedIn = false;

	// Menu
	public static final int MAIN = 0;

	// Main Menu
	public static final int SIGN_IN = 1;
	public static final int PLAY = 2;
	public static final int OPTIONS = 3;
	public static final int SCORES = 4;
	public static final int HELP = 5;
	public static final int EXIT = 6;

	// Play Menu
	public static final int RESUME = 7;
	public static final int NEW = 8;
	public static final int LOAD = 9;
	public static final int SAVE = 10;

	// New Menu
	public static final int SIZE = 100;
	public static final int TYPE = 110;
	public static final int STICKER = 120;

	// Puzzle Size Menu
	public static final int SIZE_2 = SIZE + 2;
	public static final int SIZE_3 = SIZE + 3;
	public static final int SIZE_4 = SIZE + 4;
	public static final int SIZE_5 = SIZE + 5;
	public static final int SIZE_6 = SIZE + 6;
	public static final int SIZE_7 = SIZE + 7;

	// Puzzle Type Menu
	public static final int TYPE_FREE = TYPE + 1;
	public static final int TYPE_MOVES = TYPE + 2;
	public static final int TYPE_TIME = TYPE + 3;

	// Puzzle Sticker Menu
	public static final int STICKER_COLORS = STICKER + 1;
	public static final int STICKER_PATTERN = STICKER + 2;
	public static final int STICKER_PICTURE = STICKER + 3;
	
	// TEMP - This will go under options menu
	public static final int QUALITY = 130;
	public static final int QUALITY_LOW = QUALITY + 1;
	public static final int QUALITY_MEDIUM = QUALITY + 2;
	public static final int QUALITY_HIGH = QUALITY + 3;
}
