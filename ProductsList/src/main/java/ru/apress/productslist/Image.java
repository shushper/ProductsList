package ru.apress.productslist;

/**
 * Класс хранит в себе информации об одном изображении.
 */
public class Image {

    /* id изображения */
    private int mId;
    /* порядковый номер в массиве
       изображений одного продукта */
    private int mPosition;
    /* ссылка на миниатюру изображения */
    private String mPathThumb;
    /* ссылка на полное изображение */
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
