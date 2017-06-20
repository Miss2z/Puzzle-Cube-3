package com.fivevsthree.puzzlecube3.views;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.fivevsthree.puzzlecube3.events.MenuItemListener;

public class MenuItem extends TextButton {

	private int itemNumber;

	private MenuItemListener menuItemListener;

	public int getItemNumber() {
		return itemNumber;
	}

	public MenuItem(String text, Skin skin, int itemNumber) {
		super(text, skin);
		this.itemNumber = itemNumber;
		addListener(menuItemListener());
	}

	public MenuItem(String text, Skin skin, String styleName, int itemNumber) {
		super(text, skin, styleName);
		this.itemNumber = itemNumber;
		addListener(menuItemListener());
	}

	public MenuItem(String text, TextButtonStyle style, int itemNumber) {
		super(text, style);
		this.itemNumber = itemNumber;
		addListener(menuItemListener());
	}

	public void setListener(MenuItemListener listener) {
		menuItemListener = listener;
	}

	private ClickListener menuItemListener() {
		return new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (menuItemListener != null) {
					menuItemListener.onMenuItemClicked(itemNumber);
				}
			}
		};
	}
}
