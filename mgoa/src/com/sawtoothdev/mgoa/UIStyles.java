package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class UIStyles {
	
	public static LabelStyle uiLabelStyle;
	
	public static void initializeStyles(){
		uiLabelStyle = new LabelStyle();
		uiLabelStyle.font = new BitmapFont(
				Gdx.files.internal("data/fonts/naipol.fnt"), false);
		
	}

}
