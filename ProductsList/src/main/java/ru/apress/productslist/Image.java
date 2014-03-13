package ru.apress.productslist;

/**
 * Created by shushper on 12.03.14.
 */
public class Image {
    private int mId;
    private int mPosition;
    private String mPathThumb;
    private String mPathBig;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPos(int position) {
        this.mPosition = position;
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
