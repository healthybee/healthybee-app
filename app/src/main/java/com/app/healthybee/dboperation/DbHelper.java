package com.app.healthybee.dboperation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.app.healthybee.models.Address;
import com.app.healthybee.models.CartLocal;
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
    private static final String count = "count";
    private static final String cart_id = "cart_id";
    private static final String CREATE_TABLE_CART = "CREATE TABLE " + TABLE_CART + "(" +
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            item_id + " TEXT," +
            count + " INTEGER,"+
            cart_id + " TEXT)";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_CART);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_CART);
        onCreate(sqLiteDatabase);
    }

    public long insertUpdateCart(CartLocal cartLocal) {
        openDB();
        ContentValues values = new ContentValues();
        long valueinsert;
        if (CheckIsCartAlreadyInDBorNot(db,cartLocal)){
            // update existing record
            values.put(count, cartLocal.getCount());
            valueinsert= db.update(TABLE_CART, values, item_id + " = ?",
                    new String[] { String.valueOf(cartLocal.getProductId()) });

        }else {
            //insert new record
            values.put(item_id, cartLocal.getProductId());
            values.put(count, cartLocal.getCount());
            values.put(cart_id, cartLocal.getCartId());


            valueinsert= db.insert(TABLE_CART, null, values);

        }
        closeDB();
        return valueinsert;
    }
    public List<CartLocal> getCartList() {
       openDB();
        ArrayList<CartLocal> list=new ArrayList<>();
        Cursor cursor = null;
        cursor = db.rawQuery("SELECT * FROM cart",null);

        if (cursor.moveToFirst()) {
            do {
                CartLocal cartLocal = new CartLocal();
                cartLocal.setProductId(cursor.getString(cursor
                        .getColumnIndex(item_id)));
                cartLocal.setCount((cursor.getInt(cursor
                        .getColumnIndex(count))));
                cartLocal.setCartId(cursor.getString(cursor
                        .getColumnIndex(cart_id)));
                list.add(cartLocal);
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeDB();
        return list;

    }
    private static boolean CheckIsCartAlreadyInDBorNot(SQLiteDatabase db, CartLocal cartLocal) {
        String Query = "Select * from " + TABLE_CART + " where " + item_id + " = '" + cartLocal.getProductId()+ "'";
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
    public void deleteCartRow(String productId) {
        openDB();
        db.delete(TABLE_CART, item_id +  "='" + productId+"'", null);
        Log.e("", "Record Deleted");
        closeDB();
    }
    public int getCartCount(String productId) {
        openDB();
        int cnt=0;
        Cursor cursor = db.rawQuery("SELECT * FROM cart where " + item_id + " = '" + productId+ "'",null);
        if (cursor.moveToFirst()) {
            do {
                cnt=cursor.getInt(cursor.getColumnIndex(count));
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeDB();
        return cnt;
    }
    public String getCartId(String productId) {
        openDB();
        String cartId = null;
        Cursor cursor = db.rawQuery("SELECT * FROM cart where " + item_id + " = '" + productId+ "'",null);
        if (cursor.moveToFirst()) {
            do {
                cartId=cursor.getString(cursor.getColumnIndex(cart_id));
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeDB();
        return cartId;
    }
    public void openDB() {
         db = this.getWritableDatabase();
    }
    public void closeDB() {
        db.close();
    }
}
