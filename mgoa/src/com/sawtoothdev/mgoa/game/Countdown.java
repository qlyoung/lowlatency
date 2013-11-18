package com.sawtoothdev.mgoa.game;

import com.sawtoothdev.mgoa.IUpdateable;

public class Countdown implements IUpdateable {

	private HUD hud;
	private float timer;
	private int lastSecond;
	private boolean finished = false;
	
	public Countdown(HUD hud, int seconds){
		this.hud = hud;
		this.timer = seconds;
	}
	
	@Override
	public void update(float delta) {
		
		if (!finished) {
			timer -= delta;
			
			if (lastSecond != (int) timer){
				lastSecond = (int) timer;
				hud.showMessage(String.valueOf(lastSecond));
			}
			
			if (timer <= 0){
				timer = 0;
				finished = true;
			}
		}
		
	}
	
	public boolean isFinished(){
		return finished;
	}

}
