package hanhit.mywalletapp.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by hanh.tran on 6/16/2016.
 */
public class Item implements Serializable{
    private int idItem;
    private int typeItem;
    private String nameItem;
    private String dateItem;
    private int valueItem;
    private int idCategoryItem;

    public Item(int idItem, int typeItem, String nameItem, String dateItem, int valueItem, int idCategoryItem) {
        this.idItem = idItem;
        this.typeItem = typeItem;
        this.nameItem = nameItem;
        this.dateItem = dateItem;
        this.valueItem = valueItem;
        this.idCategoryItem = idCategoryItem;
    }

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public int getTypeItem() {
        return typeItem;
    }

    public void setTypeItem(int typeItem) {
        this.typeItem = typeItem;
    }

    public String getNameItem() {
        return nameItem;
    }

    public void setNameItem(String nameItem) {
        this.nameItem = nameItem;
    }

    public String getDateItem() {
        return dateItem;
    }

    public void setDateItem(String dateItem) {
        this.dateItem = dateItem;
    }

    public int getValueItem() {
        return valueItem;
    }

    public void setValueItem(int valueItem) {
        this.valueItem = valueItem;
    }

    public int getIdCategoryItem() {
        return idCategoryItem;
    }

    public void setIdCategoryItem(int idCategoryItem) {
        this.idCategoryItem = idCategoryItem;
    }
}