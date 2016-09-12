package tripin.com.tripin_shipper.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Android on 07/09/16.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "TripinShipper.db";
    public static final String CONTACTS_TABLE_NAME = "address_book";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_STATE = "state";
    public static final String CONTACTS_COLUMN_CITY = "city";
    public static final String CONTACTS_COLUMN_SURVEY = "survey_no";
    public static final String CONTACTS_COLUMN_FIRMNAME = "firm_name";
    public static final String CONTACTS_COLUMN_ADDRESS = "address";
    public static final String CONTACTS_COLUMN_LANDMARK = "landmark";
    public static final String CONTACTS_COLUMN_PINCODE = "pincode";
    public static final String CONTACTS_COLUMN_CONTACTPERSON = "name_of_person";
    public static final String CONTACTS_COLUMN_MOBILE = "mobile";
    public static final String CONTACTS_COLUMN_LANDLINE = "landline";
    public static final String CONTACTS_COLUMN_EMAIL = "email";

    private HashMap hp;
    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table contacts " +
                        "(id integer primary key, state text,city text,survey_no text, firm_name text,address text, address text, landmark text, pincode text, name_of_person text, mobile text," +
                        "landline text, email text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }
    public boolean insertContacts(String state, String city, String survey, String firmname,String address, String landmark,
                                  String pincode, String contactPerson, String mobile, String landline, String email)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("state", state);
        contentValues.put("city", city);
        contentValues.put("survey_no", survey);
        contentValues.put("firm_name", firmname);
        contentValues.put("address", address);
        contentValues.put("landmark", landmark);
        contentValues.put("pincode", pincode);
        contentValues.put("name_of_person", contactPerson);
        contentValues.put("mobile", mobile);
        contentValues.put("landline",landline);
        contentValues.put("email", email);

        db.insert("address_book", null, contentValues);

        return true;
    }
    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from address_book where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }
    public boolean updateContact(Integer id,String state, String city, String survey, String firmname,String address, String landmark,
                                  String pincode, String contactPerson, String mobile, String landline, String email)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("state", state);
        contentValues.put("city", city);
        contentValues.put("survey_no", survey);
        contentValues.put("firm_name", firmname);
        contentValues.put("address", address);
        contentValues.put("landmark", landmark);
        contentValues.put("pincode", pincode);
        contentValues.put("name_of_person", contactPerson);
        contentValues.put("mobile", mobile);
        contentValues.put("landline",landline);
        contentValues.put("email", email);

        db.update("address_book", contentValues, "id = ? ", new String[] { Integer.toString(id) } );

        return true;
    }
    public Integer deleteContact (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("address_book",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllCotacts()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from address_book", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_CONTACTPERSON+CONTACTS_COLUMN_ADDRESS)));
            res.moveToNext();
        }
        return array_list;
    }

}
