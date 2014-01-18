package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class FileBrowser extends Table {

	private Table viewer;
	private TextButton upDirectoryButton;
	
	private ClickListener elementListener = new ClickListener() {
		@Override
		public void clicked(InputEvent event, float x, float y) {

			// get the TextButton that was clicked
			Actor actor = event.getListenerActor();

			// construct a FileHandle pointing at the path that the
			// TextButton represents
			FileHandle newPath = Gdx.files.external(actor.getName());

			if (newPath.isDirectory())
				updateViewer(newPath);
			else 
				selection = newPath;

			event.cancel();
		}
	};
	private ClickListener upDirectoryButtonListener = new ClickListener() {
		public void clicked(InputEvent event, float x, float y) {
			updateViewer(parent);
			
			super.clicked(event, x, y);
		}
	};
	
	FileHandle parent;
	FileHandle selection = null;

	public FileBrowser(FileHandle root) {
		viewer = new Table();
		viewer.defaults().padBottom(10);
		
		Label ss = new Label("Select Song", MGOA.ui.uiLabelStyle);
		upDirectoryButton = new TextButton("Root", MGOA.ui.uiTextButtonStyle);
		upDirectoryButton.addListener(upDirectoryButtonListener);
		ScrollPane sp = new ScrollPane(viewer);
		
		updateViewer(root);
		
		this.add(ss);
		this.row();
		this.add(sp).expand().fill();
		this.row();
		this.add(upDirectoryButton).bottom();
	}

	private void updateViewer(FileHandle directory) {

		// don't play tricks on me
		if (!directory.isDirectory())
			Gdx.app.error("filebrowser", "not directory");

		// clear the viewer
		viewer.clear();

		// repopulate the viewer with the children of the new directory
		for (FileHandle child : directory.list()) {

			TextButton element = new TextButton(child.name(), MGOA.ui.uiTextButtonStyle);
			element.setName(child.path());
			element.addListener(elementListener);
			
			if (child.isDirectory()) {
				
				viewer.add(element).expandX();
				viewer.row();

			} else {
				String extension = child.extension().toLowerCase();

				if (extension.contains("mp3") || extension.contains("ogg")){
					viewer.add(element).expandX();
					viewer.row();
					
					element.getLabel().setColor(Color.GREEN);
				}
			}
			
		}

		// set the parent
		parent = directory.parent();

		if (directory.path() == "") {
			upDirectoryButton.setText("ROOT");
			upDirectoryButton.setColor(Color.BLACK);
		} else {
			upDirectoryButton.setText("^ Previous Directory ^");
			upDirectoryButton.setColor(Color.WHITE);
		}

	}
	
	public FileHandle getSelection(){
		return selection;
	}
}
	