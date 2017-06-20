package com.fivevsthree.puzzlecube3.controllers;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.fivevsthree.puzzlecube3.events.LoadingEvent;
import com.fivevsthree.puzzlecube3.models.LoadingModel;
import com.fivevsthree.puzzlecube3.views.LoadingView;

public class LoadingController extends Controller<LoadingModel> {

	private boolean isFadeInDone;
	private boolean isLoadingDone;
	private boolean isFadingOut;

	public LoadingController() {
		this(new LoadingModel());

		isFadeInDone = false;
		isLoadingDone = false;
		isFadingOut = false;
	}

	public LoadingController(LoadingModel model) {
		super(model);

		view = new LoadingView(model);
	}

	@Override
	public void show() {
		model.image.addAction(Actions.sequence(Actions.alpha(0),
				Actions.fadeIn(1), Actions.delay(2), fadeInDone()));
		super.show();
	}

	@Override
	public void render(float delta) {
		if (isLoadingDone && isFadeInDone && !isFadingOut) {
			model.image.addAction(Actions.sequence(Actions.fadeOut(1),
					fadeOutDone()));
			isFadingOut = true;
		}

		super.render(delta);
	}

	public void notifyLoadingDone() {
		isLoadingDone = true;
	}

	private Action fadeInDone() {
		return new Action() {
			@Override
			public boolean act(float delta) {
				isFadeInDone = true;
				return true;
			}
		};
	}

	private Action fadeOutDone() {
		return new Action() {
			@Override
			public boolean act(float delta) {
				notifyListeners(new LoadingEvent(LoadingEvent.DONE));
				return true;
			}
		};
	}

}
