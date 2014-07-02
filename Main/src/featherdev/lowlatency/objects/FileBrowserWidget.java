package featherdev.lowlatency.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class FileBrowserWidget extends Table {

    class ListItem {
        FileHandle handle;
        String text;
        public String toString() {
            return text;
        }
    }
    List<ListItem> filelist;
    Label pathLabel;
    public FileHandle selection;

    public FileBrowserWidget(String directory, Skin skin){
        // set us up the skin
        setSkin(skin);

        // create list & define click behaviour
        filelist = new List<ListItem>(skin);
        filelist.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (filelist.getSelected().handle.isDirectory())
                    setDirectory(filelist.getSelected().handle);
                else
                    selection = filelist.getSelected().handle;

                super.clicked(event, x, y);
            }
        });
        // stick it in a scrollpane
        ScrollPane sp = new ScrollPane(filelist);
        // create path label
        pathLabel = new Label(directory, skin);

        // populate table
        row().padBottom(15);
        add("Directory: ").right();
        add(pathLabel).expandX().fill().left();
        row();
        add(sp).expand().fill().colspan(2);

        // set directory
        setDirectory(directory);
    }

    public void setDirectory(String directory){
        setDirectory(Gdx.files.absolute(directory));
    }
    public void setDirectory(FileHandle directory){
        // fail if the path doesn't exist
        if (!directory.exists()){
            Gdx.app.log("[!] ERROR", "Directory does not exist");
            return;
        }

        Array<ListItem> t = new Array<ListItem>(true, directory.list().length);
        for (FileHandle h : directory.list()){
            // don't list hidden dirs
            if (h.name().charAt(0) == '.')
                continue;

            // build a list item
            ListItem l = new ListItem();
            l.handle = h;
            l.text = h.name();

            // add it to the temp array
            t.add(l);
        }
        // add a parent item (..)
        ListItem l = new ListItem();
        l.handle = directory.parent();
        l.text = "..";
        t.insert(0, l);

        // update the list
        filelist.setItems(t);
        // update the displayed path
        pathLabel.setText(directory.path());
    }

}
