package com.intellibeacons.app;

import java.util.logging.Level;

/**
 * Created by Jeff Jin on 2014-06-16.
 */
public class DisplayTile {
    private int level;
    private int column;
    private int row;

    public DisplayTile(int level, int col, int row) {
       this.level = level;
       this.row = row;
       this.column = col;
    }



    public String getTileURL(String url, String extension)
    {
        // source:    Path to the Deep Zoom image descriptor XML
        //            http://127.0.0.1:3000/images/deepzoom/office/1.xml
        // extension: Image format extension, e.g <Image … Format="png"/>
        return url.substring( 0, url.length() - 4 ) + "_files/"
                + this.level + "/" + this.column + "_"
                + this.row + "." + extension;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public int getLevel() {
        return level;
    }
}
