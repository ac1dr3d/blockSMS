package georgia.fr.blocksms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {
   


   public static final String DATABASE_NAME = "smsblocker.db";
   public static final String TABLE_NAME = "keywords";

   public static final String COLUMN_ID = "id";
   public static final String COLUMN_KEYWORDS = "kw";
   private HashMap hp;

   public DBHelper(Context context)
   {
      super(context, DATABASE_NAME , null, 1);
   }

   @Override
   public void onCreate(SQLiteDatabase db) {
      // TODO Auto-generated method stub
      db.execSQL(
      "create table keywords " +
      "(id integer primary key, kw text)"
      );
      db.execSQL(
              "create table stats " +
                      "(id integer primary key, num text, message text)"
      );
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      // TODO Auto-generated method stub
      db.execSQL("DROP TABLE IF EXISTS keywords");
      onCreate(db);
   }

   public boolean insertKeyword(String kw)
   {
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues contentValues = new ContentValues();
      contentValues.put("kw", kw);
      long rslt = db.insert("keywords", null, contentValues);
      if(rslt==-1)
         return false;
      else
         return true;
   }
   
   public Cursor getKeyword(int id){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select * from keywords where id="+id+"", null );
      return res;
   }
   
   public int numberOfRows(String tbl){
      SQLiteDatabase db = this.getReadableDatabase();
      int numRows = (int) DatabaseUtils.queryNumEntries(db, tbl);
      return numRows;
   }

   public Cursor getAllData(String table){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res = db.rawQuery("select * from "+table, null);
      return res;
   }

   public Cursor getAllDataDesc(String table){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res = db.rawQuery("select * from "+table+" order by id desc", null);
      return res;
   }

   public boolean updateKeyword (String oldkw, String newkw)
   {
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues contentValues = new ContentValues();
      contentValues.put("kw", newkw);
      db.update("keywords", contentValues, "kw = ? ", new String[] { oldkw } );
      return true;
   }

   public Integer deleteContact (Integer id)
   {
      SQLiteDatabase db = this.getWritableDatabase();
      return db.delete("keywords",
      "id = ? ",
      new String[] { Integer.toString(id) });
   }
   
   public ArrayList<String> getAllKeywords()
   {
      ArrayList<String> array_list = new ArrayList<String>();
      
      //hp = new HashMap();
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select * from keywords", null );
      res.moveToFirst();
      
      while(res.isAfterLast() == false){
         array_list.add(res.getString(res.getColumnIndex(COLUMN_KEYWORDS)));
         res.moveToNext();
      }
   return array_list;
   }


   public boolean insertEvent(String num, String message)
   {
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues contentValues = new ContentValues();
      contentValues.put("num", num);
      contentValues.put("message", message);
      long rslt = db.insert("stats", null, contentValues);
      if(rslt==-1)
         return false;
      else
         return true;
   }


}

