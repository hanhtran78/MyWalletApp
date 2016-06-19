package hanhit.mywalletapp.model;

import java.io.Serializable;

/**
 * Created by hanh.tran on 6/16/2016.
 */
public class Category implements Serializable{
    private int idCategory;
    private String nameCategory;
    private String nameIconCategory;

    public Category(int idCategory, String nameCategory, String nameIconCategory) {
        this.idCategory = idCategory;
        this.nameCategory = nameCategory;
        this.nameIconCategory = nameIconCategory;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public String getNameIconCotegory() {
        return nameIconCategory;
    }

    public void setNameIconCotegory(String nameIconCotegory) {
        this.nameIconCategory = nameIconCotegory;
    }
}
