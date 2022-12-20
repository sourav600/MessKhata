package com.example.messbook.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.messbook.Model.CostModel;

import java.util.ArrayList;

public class Cost_DB extends SQLiteOpenHelper {
    static final String COST_DESC = "CostInfo";
    static final int VERSION = 1;

    public Cost_DB(@Nullable Context context) {
        super(context, COST_DESC, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(
                "CREATE TABLE costInfo" +
                        "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "costPERSON TEXT," +
                        "costDATE TEXT," +
                        "costAMOUNT TEXT," +
                        "costDESC TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists costInfo");
        onCreate(sqLiteDatabase);
    }

    public void insertCostData(CostModel model){
        SQLiteDatabase database =  this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("costPERSON", model.getName());
        contentValues.put("costDATE", model.getDate());
        contentValues.put("costAMOUNT",model.getAmount());
        contentValues.put("costDESC", model.getDesc());

        database.insert("costInfo",null,contentValues);
    }

    public ArrayList<CostModel> getCostData(){
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.rawQuery("select costPERSON, costDATE, costAMOUNT, costDESC from costInfo", null);
        ArrayList<CostModel> list;
        if(cursor.moveToFirst()){
            list = new ArrayList<>();
            do{
                CostModel model = new CostModel(cursor.getString(0),cursor.getString(1),cursor.getString(2), cursor.getString(3));
                list.add(model);
            }while (cursor.moveToNext());
        }
        else{
            list = new ArrayList<>();
        }
        cursor.close();
        database.close();
        return list;
    }

    public int getTotalCost(){
        int total = 0;
        Cursor c = getWritableDatabase().rawQuery("select sum(costAMOUNT) from costInfo",null);
        if(c.moveToFirst()){
            total = c.getInt(0);
        }
        return total;
    }
}
