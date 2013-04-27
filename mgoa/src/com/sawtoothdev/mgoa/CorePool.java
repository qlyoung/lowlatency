package com.sawtoothdev.mgoa;

import box2dLight.RayHandler;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;

public class CorePool extends Pool<BeatCore> {

	private World world;
	private RayHandler rayHandler;
	
	public CorePool(World world, RayHandler rayHandler){
		this.world = world;
		this.rayHandler = rayHandler;
	}
	
	@Override
	protected BeatCore newObject() {
		return new BeatCore(world, rayHandler);
	}

}
