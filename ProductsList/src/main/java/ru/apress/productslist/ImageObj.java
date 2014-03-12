package ru.apress.productslist;

/**
 * Created by shushper on 12.03.14.
 */
public class ImageObj {
    private int mId;
    private int mPos;
    private String mPathThumb;
    private String mPathBig;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public int getPos() {
        return mPos;
    }

    public void setPos(int pos) {
        this.mPos = pos;
    }

    public String getPathThumb() {
        return mPathThumb;
    }

    public void setPathThumb(String pathThumb) {
        this.mPathThumb = pathThumb;
    }

    public String getPathBig() {
        return mPathBig;
    }

    public void setPathBig(String pathBig) {
        this.mPathBig = pathBig;
    }
}
