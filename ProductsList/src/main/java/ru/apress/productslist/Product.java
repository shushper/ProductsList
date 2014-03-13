package ru.apress.productslist;

/**
 * Created by shushper on 12.03.14.
 */
public class Product {
    private int mId;
    private String mName;
    private int mImagesCnt;
    private Image[] mImages;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public int getImagesCnt() {
        return mImagesCnt;
    }

    public void setImagesCnt(int imagesCnt) {
        this.mImagesCnt = imagesCnt;
    }

    public Image[] getImages() {
        return mImages;
    }

    public void setImages(Image[] images) {
        this.mImages = images;
    }
}
