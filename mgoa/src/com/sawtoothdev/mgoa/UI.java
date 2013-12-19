package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class UI {
	
	public UI(){
		uiFnt = new BitmapFont(Gdx.files.internal("data/fonts/naipol.fnt"), false);
		uiLabelStyle = new LabelStyle();
		uiLabelStyle.font = uiFnt;
		
		uiTextButtonStyle = new TextButtonStyle();
		uiTextButtonStyle.font = uiFnt;
		uiTextButtonStyle.fontColor = Color.WHITE;
		uiTextButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(
				"data/textures/ui/menubtn-up.png"), 2, 0, 253, 64));
		uiTextButtonStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(
				"data/textures/ui/menubtn-down.png"), 2, 0, 253, 64));
		
		TextButtonStyle tStyle = new TextButtonStyle();
		tStyle.font = uiFnt;

		TextureRegionDrawable graybg = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("data/textures/ui/dropdown-bg.png"))));
		TextureRegionDrawable clearbg = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("data/textures/ui/clear-bg.png"))));
		uiSelectBoxStyle = new SelectBoxStyle(uiFnt, Color.WHITE, clearbg, graybg, graybg);
	}
	
	public final LabelStyle uiLabelStyle;
	public final TextButtonStyle uiTextButtonStyle;
	public final SelectBoxStyle uiSelectBoxStyle;
	public final BitmapFont uiFnt;
}