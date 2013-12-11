package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class UIResources {
	
	public static LabelStyle uiLabelStyle;
	public static TextButtonStyle uiTextButtonStyle;
	public static SelectBoxStyle uiSelectBoxStyle;
	
	public static TextureRegionDrawable up = new TextureRegionDrawable(new TextureRegion(new Texture(
			"data/textures/ui/menubtn-up.png")));
	public static TextureRegionDrawable down = new TextureRegionDrawable(new TextureRegion(new Texture(
			"data/textures/ui/menubtn-down.png")));
	
	public static void initializeStyles(){
		uiLabelStyle = new LabelStyle();
		uiLabelStyle.font = Resources.uiFnt;
		
		uiTextButtonStyle = new TextButtonStyle();
		uiTextButtonStyle.font = Resources.uiFnt;
		uiTextButtonStyle.fontColor = Color.WHITE;
		uiTextButtonStyle.up = up;
		uiTextButtonStyle.down = down;
		
		TextButtonStyle tStyle = new TextButtonStyle();
		tStyle.font = UIResources.uiLabelStyle.font;

		TextureRegionDrawable graybg = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("data/textures/ui/dropdown-bg.png"))));
		TextureRegionDrawable clearbg = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("data/textures/ui/clear-bg.png"))));
		uiSelectBoxStyle = new SelectBoxStyle(Resources.uiFnt, Color.WHITE, clearbg, graybg, graybg);
	}

}
