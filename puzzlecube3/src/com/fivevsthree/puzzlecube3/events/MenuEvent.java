package com.fivevsthree.puzzlecube3.events;

public class MenuEvent extends Event {

	public static final int EXIT = 1;
	public static final int SIZE = 2;
	public static final int SIGN_IN = 3;
	public static final int QUALITY = 4;

	public MenuEvent(int type) {
		super(type);
	}

}
