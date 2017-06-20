package com.fivevsthree.puzzlecube3;

import java.text.DateFormat;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;

public class MainActivity extends AndroidApplication implements
		PlatformHandler, GameHelperListener {

	private GameHelper gameHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		cfg.useWakelock = true;
		cfg.useGL20 = true;

		initialize(new PuzzleCube(this), cfg);

		gameHelper = new GameHelper(this);
		gameHelper.enableDebugLog(true, "Game Helper");
		gameHelper.setup(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		gameHelper.onStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		gameHelper.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		gameHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public DateFormat getDateFormat() {
		return android.text.format.DateFormat
				.getDateFormat(getApplicationContext());
	}

	@Override
	public void signIn() {
		runOnUiThread(new Runnable() {
			public void run() {
				gameHelper.beginUserInitiatedSignIn();
			}
		});
	}

	@Override
	public void signOut() {
		runOnUiThread(new Runnable() {
			public void run() {
				gameHelper.signOut();
			}
		});
	}

	@Override
	public boolean getSignedIn() {
		return gameHelper.isSignedIn();
	}

	@Override
	public void onSignInFailed() {
	}

	@Override
	public void onSignInSucceeded() {
	}

}