package com.sawtoothdev.mgoa.objects;

import java.util.Hashtable;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sawtoothdev.mgoa.MainGame;

public class FileBrowser extends Table {

	private Hashtable<TextButton, FileHandle> currentElements;
	FileHandle selection = null;
	
	TextButton upDirBtn;
	private Table viewer;
	private ClickListener elementListener = new ClickListener() {
		@Override
		public void clicked(InputEvent event, float x, float y) {

			Actor actor = event.getListenerActor();

			FileHandle choice = currentElements.get(actor);
			
			if (choice.isDirectory())
				updateViewer(choice);
			else 
				selection = choice;

			event.cancel();
		}
	};

	public FileBrowser(FileHandle root) {
		currentElements = new Hashtable<TextButton, FileHandle>();
		viewer = new Table();
		ScrollPane sp = new ScrollPane(viewer);
		upDirBtn = new TextButton("Root", MainGame.ui.uiTextButtonStyle);
		
		viewer.defaults().padBottom(10);
		
		upDirBtn.addListener(elementListener);
		updateViewer(root);
		
		this.add(sp).expand().fill();
		this.row();
		this.add(upDirBtn).bottom();
	}

	private void updateViewer(FileHandle directory) {

		// don't play tricks on me
		if (!directory.isDirectory())
			Gdx.app.error("filebrowser", "not directory");

		// clear the viewer & element map
		viewer.clear();
		currentElements.clear();

		// repopulate the element map with the children of the new directory
		for (FileHandle child : directory.list()) {
			
			// skip dot[files|directories]
			if (child.name().charAt(0) == '.')
				continue;
			
			TextButton element = new TextButton(child.name(), MainGame.ui.uiTextButtonStyle);
			element.setName(child.path());
			element.addListener(elementListener);
			
			currentElements.put(element, child);
		}
		
		// update the viewer to match the element map
		for (Entry<TextButton, FileHandle> e : currentElements.entrySet()){
			FileHandle handle = e.getValue();
			TextButton element = e.getKey();

			if (!handle.isDirectory()){
				String ext = handle.extension();
				if (ext.equalsIgnoreCase("mp3") || ext.equalsIgnoreCase("ogg"))
					element.getLabel().setColor(Color.GREEN);
			}
			
			viewer.add(element).expandX();
			viewer.row();
		}

		currentElements.put(upDirBtn, directory.parent());

		if (directory.path() == "") {
			upDirBtn.setText("ROOT");
			upDirBtn.setColor(Color.BLACK);
		} else {
			upDirBtn.setText("^ Previous Directory ^");
			upDirBtn.setColor(Color.WHITE);
		}

	}
	
	public FileHandle getSelection(){
		return selection;
	}
}
	