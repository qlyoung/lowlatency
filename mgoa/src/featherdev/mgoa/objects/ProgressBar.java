package featherdev.mgoa.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import featherdev.mgoa.screens.IDrawable;
import featherdev.mgoa.screens.IUpdateable;

public class ProgressBar implements IDrawable, IUpdateable {
	
	private Texture unit;
	private final int maxlength = 30;
	private int blocks;
	private Vector2 position;
	
	public ProgressBar (){
		this.unit = new Texture(Gdx.files.internal("textures/progbarunit.png"));
	}

	@Override
	public void update(float delta) {

	}

	@Override
	public void draw(SpriteBatch batch) {
		float previousx = position.x;
		for (int i = 0; i < blocks; i++){
			batch.draw(unit, previousx, position.y);
			previousx += unit.getWidth();
		}

	}

	public void setPercent(float percentOutOfOne){
		blocks = (int) Math.floor(percentOutOfOne * maxlength);
	}
	
	public void setPosition(Vector2 screencoords){
		this.position = screencoords;
	}
	
}
