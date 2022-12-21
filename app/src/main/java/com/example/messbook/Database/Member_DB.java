package com.example.messbook.Database;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;
import com.example.messbook.Model.MemberModel;

import java.io.File;
import java.util.ArrayList;

public class Member_DB extends SQLiteOpenHelper {

    static final String NAME = "membersInfo";
    static final int VERSION = 1;

    public Member_DB(@Nullable Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "create table member_Info" +
                        "(id INTEGER primary key AUTOINCREMENT," +
                        "memberName text,"+
                        "memberAmount int,"+
                        "memberMeal float)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists member_Info");
        onCreate(sqLiteDatabase);
    }

    //add new member
    public void insertMemberData(MemberModel model){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("memberName",model.getName());
        contentValues.put("memberAmount",model.getMoney());
        contentValues.put("memberMeal",model.getMeal());

        database.insert("member_Info",null,contentValues);
        database.close();
    }

    //Update members info
    public void updateMember(String name,int amount, float meal){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("memberName",name);
        values.put("memberAmount",amount);
        values.put("memberMeal",meal);

        db.update("member_Info",values,"name=?",new String[] {name});
        db.close();
    }

    public ArrayList<MemberModel> getMemberData(){
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor;
        cursor = database.rawQuery("select memberName,memberAmount,memberMeal from member_Info",null);
        ArrayList<MemberModel> list;
        if (cursor.moveToFirst()){
            list = new ArrayList<>();
            do{
                MemberModel model = new MemberModel(cursor.getString(0),cursor.getInt(1),cursor.getFloat(2));
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

    public ArrayList<String> getMemberName(){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select memberName from member_Info",null);
        ArrayList<String > list;
        if(cursor.moveToFirst()){
            list = new ArrayList<>();
            do {
                String s = new String(cursor.getString(0));
                list.add(s);
            }while (cursor.moveToNext());
        }
        else{
            list = new ArrayList<>();
        }
        cursor.close();
        sqLiteDatabase.close();
        return list;
    }

    //return total amount
    public int getSumOfAmount(){
        int total=0;
        Cursor c = getWritableDatabase().rawQuery("select sum(memberAmount) from member_Info",null);
        if(c.moveToFirst()){
            total = c.getInt(0);
        }
        return total;
    }

    //return total meal
    public float getSumOfMeal(){
        float total=0;
        Cursor c = getWritableDatabase().rawQuery("select sum(memberMeal) from member_Info",null);
        if(c.moveToFirst()){
            total = c.getFloat(0);
        }
        return total;
    }



}
