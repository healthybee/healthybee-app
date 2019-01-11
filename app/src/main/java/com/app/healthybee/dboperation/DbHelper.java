package com.app.healthybee.dboperation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.app.healthybee.models.Address;
import com.app.healthybee.models.CategoryItem;

import java.util.ArrayList;
import java.util.List;


public class DbHelper extends SQLiteOpenHelper {
    // DATABASE VERSION
    private static final int DATABASE_VERSION = 1;
    // DATABASE NAME
    private static final String DATABASE_NAME = "HealthyBee.db";

    private SQLiteDatabase db;

    // table name
    private static final String TABLE_CART = "cart";
    private static final String KEY_ID = "id";
    private static final String item_id = "item_id";
    private static final String price = "price";
    private static final String add_on = "add_on";
    private static final String description = "description";
    private static final String image_url = "image_url";
    private static final String food_type = "food_type";
    private static final String old_price = "old_price";
    private static final String nutrition = "nutrition";
    private static final String name = "name";
    private static final String category = "category";
    private static final String count = "count";
    private static final String CREATE_TABLE_CART = "CREATE TABLE " + TABLE_CART + "(" +
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            item_id + " INTEGER," +
            price + " TEXT," +
            add_on + " TEXT," +
            description + " TEXT," +
            image_url + " TEXT," +
            food_type + " TEXT," +
            old_price + " TEXT," +
            nutrition + " TEXT," +
            name + " TEXT," +
            category + " TEXT," +
            count + " INTEGER)";


    // table name
    private static final String TABLE_ADDRESS = "address";
    private static final String AddressType = "AddressType";
    private static final String AddressLine1 = "AddressLine1";
    private static final String AddressLine2 = "AddressLine2";
    private static final String AddressCity = "AddressCity";
    private static final String AddressState = "AddressState";
    private static final String AddressZipCode = "AddressZipCode";
    private static final String AddressLandmark = "AddressLandmark";
    private static final String CREATE_TABLE_ADDRESS = "CREATE TABLE " + TABLE_ADDRESS + "(" +
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            AddressType + " INTEGER," +
            AddressLine1 + " TEXT," +
            AddressLine2 + " TEXT," +
            AddressCity + " TEXT," +
            AddressState + " TEXT," +
            AddressZipCode + " TEXT," +
            AddressLandmark + " TEXT)";




    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_CART);
        sqLiteDatabase.execSQL(CREATE_TABLE_ADDRESS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_CART);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_ADDRESS);
        onCreate(sqLiteDatabase);
    }
    public List<Address> getAddressList() {
        openDB();
        ArrayList<Address> list = new ArrayList<>();
        Cursor cursor = null;
        cursor = db.rawQuery("SELECT * FROM address", null);
        if (null != cursor){
            if (cursor.moveToFirst()) {

                do {
                    Address address = new Address();
                    address.setId(cursor.getInt(cursor
                            .getColumnIndex(KEY_ID)));
                    address.setAddressType(cursor.getString(cursor
                            .getColumnIndex(AddressType)));
                    address.setAddressLine1(cursor.getString(cursor
                            .getColumnIndex(AddressLine1)));
                    address.setAddressLine2(cursor.getString(cursor
                            .getColumnIndex(AddressLine2)));
                    address.setAddressCity(cursor.getString(cursor
                            .getColumnIndex(AddressCity)));
                    address.setAddressState(cursor.getString(cursor
                            .getColumnIndex(AddressState)));
                    address.setAddressZipCode(cursor.getString(cursor
                            .getColumnIndex(AddressZipCode)));
                    address.setAddressLandmark((cursor.getString(cursor
                            .getColumnIndex(AddressLandmark))));

                    list.add(address);
                } while (cursor.moveToNext());
            }
    }

        cursor.close();
        closeDB();
        return list;

    }
    private static boolean CheckIsAddressAlreadyInDBorNot(SQLiteDatabase db, Address address) {
        String Query = "Select * from " + TABLE_ADDRESS + " where " + KEY_ID + " = " + address.getId();
        Cursor cursor = db.rawQuery(Query, null);
        if (null!=cursor) {
            if (cursor.getCount() <= 0) {
                cursor.close();
                return false;
            }
        }
        cursor.close();
        return true;
    }

    public long insertUpdateAddress(Address address) {
        openDB();
        ContentValues values = new ContentValues();
        long valueinsert;
        if (CheckIsAddressAlreadyInDBorNot(db,address)){
            // update existing record
            values.put(AddressType, address.getAddressType());
            values.put(AddressLine1, address.getAddressLine1());
            values.put(AddressLine2, address.getAddressLine2());
            values.put(AddressCity, address.getAddressCity());
            values.put(AddressState, address.getAddressState());
            values.put(AddressZipCode, address.getAddressZipCode());
            values.put(AddressLandmark, address.getAddressLandmark());
            valueinsert= db.update(TABLE_ADDRESS, values, KEY_ID + " = ?", new String[] {String.valueOf(address.getId())});

        }else {
            //insert new record
            values.put(AddressType, address.getAddressType());
            values.put(AddressLine1, address.getAddressLine1());
            values.put(AddressLine2, address.getAddressLine2());
            values.put(AddressCity, address.getAddressCity());
            values.put(AddressState, address.getAddressState());
            values.put(AddressZipCode, address.getAddressZipCode());
            values.put(AddressLandmark, address.getAddressLandmark());


            valueinsert= db.insert(TABLE_ADDRESS, null, values);

        }
        closeDB();
        return valueinsert;
    }
    public long insertUpdateCart(CategoryItem categoryItem) {
        openDB();
        ContentValues values = new ContentValues();
        long valueinsert;
        if (CheckIsCartAlreadyInDBorNot(db,categoryItem)){
            // update existing record
            values.put(count, categoryItem.getCount());
            valueinsert= db.update(TABLE_CART, values, name + " = ?",
                    new String[] { String.valueOf(categoryItem.getName()) });

        }else {
            //insert new record
            values.put(item_id, categoryItem.getId());
            values.put(price, categoryItem.getPrice());
            values.put(add_on, categoryItem.getAdd_on());
            values.put(description, categoryItem.getDescription());
            values.put(image_url, categoryItem.getImage_url());
            values.put(food_type, categoryItem.getFood_type());
            values.put(old_price, categoryItem.getOld_price());
            values.put(nutrition, categoryItem.getNutrition());
            values.put(name, categoryItem.getName());
            values.put(category, categoryItem.getCategory());
            values.put(count, categoryItem.getCount());

            valueinsert= db.insert(TABLE_CART, null, values);

        }
        closeDB();
        return valueinsert;
    }
    public List<CategoryItem> getCartList() {
       openDB();
        ArrayList<CategoryItem> list=new ArrayList<>();
        Cursor cursor = null;
        cursor = db.rawQuery("SELECT * FROM cart",null);

        if (cursor.moveToFirst()) {
            do {
                CategoryItem categoryItem = new CategoryItem();
                categoryItem.setId(String.valueOf(Integer.parseInt(String.valueOf(cursor.getInt(cursor.getColumnIndex(item_id))))));
//                categoryItem.setId(cursor.getInt(cursor
//                        .getColumnIndex(item_id)));
                categoryItem.setPrice(cursor.getString(cursor
                        .getColumnIndex(price)));
                categoryItem.setAdd_on(cursor.getString(cursor
                        .getColumnIndex(add_on)));
                categoryItem.setDescription(cursor.getString(cursor
                        .getColumnIndex(description)));
                categoryItem.setImage_url(cursor.getString(cursor
                        .getColumnIndex(image_url)));
                categoryItem.setFood_type(cursor.getString(cursor
                        .getColumnIndex(food_type)));
                categoryItem.setOld_price(cursor.getString(cursor
                        .getColumnIndex(old_price)));
                categoryItem.setNutrition((cursor.getString(cursor
                        .getColumnIndex(nutrition))));
                categoryItem.setName((cursor.getString(cursor
                        .getColumnIndex(name))));
                categoryItem.setCategory((cursor.getString(cursor
                        .getColumnIndex(category))));
                categoryItem.setCount((cursor.getInt(cursor
                        .getColumnIndex(count))));
                list.add(categoryItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeDB();
        return list;

    }
    private static boolean CheckIsCartAlreadyInDBorNot(SQLiteDatabase db, CategoryItem categoryItem) {
        String Query = "Select * from " + TABLE_CART + " where " + name + " = '" + categoryItem.getName()+ "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (null!=cursor) {
            if (cursor.getCount() <= 0) {
                cursor.close();
                return false;
            }
        }
        cursor.close();
        return true;
    }
    public void deleteCartRow(String name1) {
        openDB();
        db.delete(TABLE_CART, name +  "='" + name1+"'", null);
        Log.e("", "Record Deleted");
        closeDB();
    }
    public int getCartCount(String name1) {
        openDB();
        int cnt=0;
        Cursor cursor = db.rawQuery("SELECT * FROM cart where " + name + " = '" + name1+ "'",null);
        if (cursor.moveToFirst()) {
            do {
                cnt=cursor.getInt(cursor.getColumnIndex(count));
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeDB();
        return cnt;
    }
    public void openDB() {
         db = this.getWritableDatabase();
    }
    public void closeDB() {
        db.close();
    }
}
