package com.intellibeacons.app;

public class TileLevel {
    private int mLevel;
    private int mNumOfColumns;
    private int mNumOfRows;
    private int mWidth;
    private int mHeight;

    public TileLevel(int level, int col, int row, int w, int h){
        mLevel = level;
        mNumOfColumns = col;
        mNumOfRows = row;
        mWidth = w;
        mHeight = h;
    }


    public int getLevel() {
        return mLevel;
    }

    public int getNumOfColumns() {
        return mNumOfColumns;
    }

    public int getNumOfRows() {
        return mNumOfRows;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }
}
