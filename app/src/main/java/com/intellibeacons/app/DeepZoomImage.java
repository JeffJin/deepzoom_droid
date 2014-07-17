package com.intellibeacons.app;

import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeffjin on 2014-06-16.
 */
public class DeepZoomImage {

    private DeepZoomInfo metaInfo;

    public DeepZoomImage(DeepZoomInfo metaInfo){

        this.metaInfo = metaInfo;
    }

    //<Deepzoom tile calculation>

    /// <summary>
    /// Returns the visible tiles inside a rectangle starting from a chosen
    /// level and moving upwards until there are no missing tiles.
    /// </summary>
    /// <param name="rectangle">The rectangle in which tiles will be searched.</param>
    /// <param name="startingLevel">The level where the search will start. If there are missing tiles in this level,
    /// the next level will be rendered, until a level with no missing tiles is found.</param>
    /// <returns>The visible tiles inside the rectangle at the requested level.</returns>
    public List<DisplayTile> getVisibleTiles(Rect rectangle, int startingLevel, int maxLevel){
        List<DisplayTile> visibleTiles = new ArrayList<DisplayTile>();
        int level = startingLevel;
        boolean isVisibleAreaFullyPainted = false;
        while (level >= 0 && !isVisibleAreaFullyPainted)
        {
            int levelScale = (int)scaleAtLevel(level, maxLevel);
            Rect scaledBounds = new Rect(rectangle.left * levelScale, rectangle.top * levelScale, rectangle.width() * levelScale, rectangle.height() * levelScale);
            //TODO create original image RECT
            List<DisplayTile> tilesInLevel = VisibleTiles(scaledBounds, level, null, 256);

            // If there are no missing tiles, area is fully painted
            isVisibleAreaFullyPainted = checkVisibleAreaFullyPainted(tilesInLevel);

            visibleTiles.addAll(tilesInLevel);
            level--;
        }
        return visibleTiles;
    }

    //!tilesInLevel.Any(t => GetTileLayers(t.Level, t.Column, t.Row) == null);
    private boolean checkVisibleAreaFullyPainted(List<DisplayTile> tilesInLevel) {
        for (DisplayTile tile : tilesInLevel) {
            if (getTileLayers(tile.getLevel(), tile.getColumn(), tile.getRow()) == null) {
                return false;
            }
        }
        return true;
    }

    //TODO check wpf deep zoom
    private List<DisplayTile> getTileLayers(int level, int column, int row){
        return null;
    }

    public double scaleAtLevel(int level, int maxLevel)
    {
        return Math.pow(0.5, maxLevel - level);
    }

    // Returns the visible tiles inside a rectangle on any level
    private List<DisplayTile> VisibleTiles(Rect rectangle, int level, Rect originalImg, int tileSize)
    {
        List<DisplayTile> tiles = new ArrayList<DisplayTile>();
        //make sure the rectangle stays inside the original image
        rectangle.intersect(originalImg);

        int top = (int)Math.floor(rectangle.top / tileSize);
        int left = (int)Math.floor(rectangle.left / tileSize);
        int right = (int)Math.ceil(rectangle.right / tileSize);
        int bottom = (int)Math.ceil(rectangle.bottom / tileSize);

        right = right < ColumnsAtLevel(level) ? right : ColumnsAtLevel(level);
        bottom = bottom < RowsAtLevel(level) ? bottom : RowsAtLevel(level);

        int width = (right - left) > 0 ? (right - left) : 0;
        int height = (bottom - top) > 0 ? (bottom - top) : 0;

        if (top == 0.0 && left == 0.0 && width == 1.0 && height == 1.0) // This level only has one tile
        {
            tiles.add(new DisplayTile(level, 0, 0));
        }
        else
        {
            List<Point> points =  Quadivide(new Rect(left, top, width, height));

            for(int i = 0; i < points.size(); i++){
                Point pt = points.get(i);
                tiles.add(new DisplayTile(level, pt.x, pt.y));
            }
        }
        return tiles;
    }

    private List<Point> Quadivide(Rect rect){
        List<Point> points = new ArrayList<Point>();

        return points;
    }

    //TODO
    private int RowsAtLevel(int level) {
        return 0;
    }

    private int ColumnsAtLevel(int level) {
        return 0;
    }

    public int getMaximumLevel(int width, int height )
    {
        return (int)Math.ceil( Math.log( Math.max( width, height ))/Math.log(2) );
    }

    public List<TileLevel> computeLevels(int width, int height, int tileSize)
    {
        List<TileLevel> tileLevels = new ArrayList<TileLevel>();
        int maxLevel = getMaximumLevel( width, height );

        for( int level = maxLevel; level >= 0; level-- )
        {
            // compute number of rows & columns
            int columns = (int)Math.ceil( width / tileSize );
            int rows = (int)Math.ceil( height / tileSize );

            Log.d("computeLevels", "level " + level + " is " + width + " x " + height
                    + " (" + columns + " columns, " + rows + " rows)");
            tileLevels.add(new TileLevel(level, columns, rows, width, height));

            // compute dimensions of next level
            width  = (int)Math.ceil( width / 2 );
            height = (int)Math.ceil( height / 2 );

        }
        return tileLevels;
    }

    //Url
    public String getTileURL(String url, String extension, int level, int column, int row)
    {
        // source:    Path to the Deep Zoom image descriptor XML
        //            http://127.0.0.1:3000/images/deepzoom/office/1.xml
        // extension: Image format extension, e.g <Image … Format="png"/>
        return url.substring( 0, url.length() - 4 ) + "_files/"
                + level + "/" + column + "_"
                + row + "." + extension;
    }

    public DeepZoomInfo loadDeepZoomImage(String url){
        //TODO load xml using http request
        int tileSize = 256;
        int width = 6201;
        int height = 3201;
        int overlap = 1;
        String format = "jpg";
        //TODO refactor the constructor
        DeepZoomInfo info = new DeepZoomInfo();
        info.Format = format;
        info.TileSize = tileSize;
        info.TileOverlap = overlap;
        info.Width = width;
        info.Height = height;

        return info;
    }

    public Point getTilePosition(int column, int row, DeepZoomInfo info)
    {
        Point position = new Point();

        // tileSize: Dimensions of tile, e.g <Image … TileSize="256"/>
        // tileOverlap: Overlap in pixels, e.g. <Image … Overlap="1"/>
        int offsetX = ( column == 0 ) ? 0 : info.TileOverlap;
        int offsetY = ( row    == 0 ) ? 0 : info.TileOverlap;

        position.x = ( column * info.TileSize ) - offsetX;
        position.y = ( row    * info.TileSize ) - offsetY;

        return position;
    }

    //</Deepzoom tile calculation>
}
