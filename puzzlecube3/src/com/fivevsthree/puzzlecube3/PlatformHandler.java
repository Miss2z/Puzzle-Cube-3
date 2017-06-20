package com.fivevsthree.puzzlecube3;

import java.text.DateFormat;

public interface PlatformHandler {

	public DateFormat getDateFormat();

	public void signIn();

	public void signOut();

	public boolean getSignedIn();

}
