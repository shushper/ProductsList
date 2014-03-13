package ru.apress.productslist;

/**
 * Класс хранит в себе информации об одном продкуте.
 */
public class Product {

    /* id продукта */
    private int mId;
    /* имя продукта */
    private String mName;
    /* количество изображений */
    private int mImagesCnt;
    /* массив изображений*/
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
