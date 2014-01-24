package com.sawtoothdev.mgoa.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sawtoothdev.mgoa.IDrawable;
import com.sawtoothdev.mgoa.IUpdateable;

public class Countdown implements IUpdateable, IDrawable {

	private HudManager hud;
	private float timer;
	private int lastSecond;
	private boolean finished = false;
	
	public Countdown(HudManager hud, int seconds){
		this.hud = hud;
		this.timer = seconds;
	}
	
	@Override
	public void update(float delta) {
		
		if (!finished) {
			timer -= delta;
			
			if (timer <= 0){
				timer = 0;
				finished = true;
			}
		}
	}
	@Override
	public void draw(SpriteBatch batch) {
		if (lastSecond != (int) timer){
			lastSecond = (int) timer;
			hud.showMessage(String.valueOf(lastSecond), Color.CYAN);
		}
	}
	
	public boolean isFinished(){
		return finished;
	}

}
