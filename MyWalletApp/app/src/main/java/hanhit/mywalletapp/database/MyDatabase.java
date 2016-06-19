package hanhit.mywalletapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import hanhit.mywalletapp.model.Category;
import hanhit.mywalletapp.model.Item;

/**
 * Created by hanh.tran on 6/17/2016.
 */
public class MyDatabase extends SQLiteOpenHelper {

    // Table Items
    public static final String TABLE_ITEMS = "items";
    public static final String COL_ID_ITEM = "idItem";
    public static final String COL_TYPE_ITEM = "typeItem";
    public static final String COL_NAME_ITEM = "nameItem";
    public static final String COL_DATE_ITEM = "dateItem";
    public static final String COL_VALUE_ITEM = "valueItem";
    public static final String COL_CATEGORY_ID_ITEM = "categoryIdItem";

    // Create table
    private static final String CREATE_TABLE_ITEM = "create table " + TABLE_ITEMS + "("
            + COL_ID_ITEM + " integer primary key autoincrement, "
            + COL_TYPE_ITEM + " integer not null, "
            + COL_NAME_ITEM + " text, "
            + COL_DATE_ITEM + " text not null, "
            + COL_VALUE_ITEM + " integer not null, "
            + COL_CATEGORY_ID_ITEM + " integer not null);";

    // Table Categories
    public static final String TABLE_CATEGORY = "categories";
    public static final String COL_ID_CATEGORY = "idCategory";
    public static final String COL_NAME_CATEGORY = "nameCategory";
    public static final String COL_NAME_IMAGE = "nameImage";

    //Create table Category
    private static final String CREATE_TABLE_CATEGORY = "create table " + TABLE_CATEGORY + "("
            + COL_ID_CATEGORY + " integer primary key autoincrement, "
            + COL_NAME_CATEGORY + " text not null, "
            + COL_NAME_IMAGE + " integer not null);";

    private static final String DATABASE_NAME = "my_wallet";
    private static final int DATABASE_VERSION = 1;

    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ITEM);
        db.execSQL(CREATE_TABLE_CATEGORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        onCreate(db);
    }

    public Item getItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from items where idItem = " + id + "", null);
        Item item = new Item(
                res.getInt(res.getColumnIndex(COL_ID_ITEM)),
                res.getInt(res.getColumnIndex(COL_TYPE_ITEM)),
                res.getString(res.getColumnIndex(COL_NAME_ITEM)),
                res.getString(res.getColumnIndex(COL_DATE_ITEM)),
                res.getInt(res.getColumnIndex(COL_VALUE_ITEM)),
                res.getInt(res.getColumnIndex(COL_CATEGORY_ID_ITEM)));
        return item;
    }

    public Category getCategory(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from categories where idCategory = " + id + "", null);

        Category category = new Category(
                res.getInt(res.getColumnIndex(COL_ID_CATEGORY)),
                res.getString(res.getColumnIndex(COL_NAME_CATEGORY)),
                res.getString(res.getColumnIndex(COL_NAME_IMAGE))
        );
        return category;
    }

    public int numberItem() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_ITEMS);
        return numRows;
    }

    public boolean addItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_ID_ITEM, item.getIdItem());
        cv.put(COL_TYPE_ITEM, item.getTypeItem());
        cv.put(COL_NAME_ITEM, item.getNameItem());
        cv.put(COL_DATE_ITEM, item.getDateItem());
        cv.put(COL_VALUE_ITEM, item.getValueItem());
        cv.put(COL_CATEGORY_ID_ITEM, item.getIdCategoryItem());

        db.insert(TABLE_ITEMS, null, cv);
        return true;
    }

    public boolean updateItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_TYPE_ITEM, item.getTypeItem());
        cv.put(COL_NAME_ITEM, item.getNameItem());
        cv.put(COL_DATE_ITEM, item.getDateItem());
        cv.put(COL_VALUE_ITEM, item.getValueItem());
        cv.put(COL_CATEGORY_ID_ITEM, item.getIdCategoryItem());
        db.update(TABLE_ITEMS, cv, COL_ID_ITEM + " = " + item.getIdItem(), null);
        return true;
    }

    public Integer deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_ITEMS, COL_ID_ITEM + " = " + id, null);
    }

    public ArrayList<Item> getAllItem() {
        ArrayList<Item> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from items", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(new Item(
                    res.getInt(res.getColumnIndex(COL_ID_ITEM)),
                    res.getInt(res.getColumnIndex(COL_TYPE_ITEM)),
                    res.getString(res.getColumnIndex(COL_NAME_ITEM)),
                    res.getString(res.getColumnIndex(COL_DATE_ITEM)),
                    res.getInt(res.getColumnIndex(COL_VALUE_ITEM)),
                    res.getInt(res.getColumnIndex(COL_CATEGORY_ID_ITEM)))
            );
            res.moveToNext();
        }
        return array_list;
    }
}
