package com.amplearch.circleonet.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.amplearch.circleonet.Model.NFCModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper
{
	// Logcat tag
	private static final String LOG = "DatabaseHelper";
	// Database Version
	private static final int DATABASE_VERSION = 1;
	// Database Name
	private static final String DATABASE_NAME = "CircleOneDatabase";
	// Table Names
	private static final String TABLE_NFC = "nfc_data";

	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_COMPANY = "company";
	private static final String KEY_DESIGNATION = "designation";
	private static final String KEY_MOB = "mob_no";
	private static final String KEY_WORK = "work_no";
	private static final String KEY_PH = "ph_no";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_WEBSITE = "website";
	private static final String KEY_ADDRESS = "address";
	private static final String KEY_LAT = "lat";
	private static final String KEY_LNG = "lng";
	private static final String KEY_REMARK = "remark";
    private static final String KEY_FACEBOOK_ID = "fb_id";
    private static final String KEY_LINKEDIN_ID = "linkedin_id";
    private static final String KEY_GOOGLE_ID = "google_id";
    private static final String KEY_TWITTER_ID = "twitter_id";
    private static final String KEY_YOUTUBE_ID = "youtube_id";
    private static final String KEY_CARD_FRONT = "card_front";
	private static final String KEY_CARD_BACK = "card_back";

	private static final String CREATE_TABLE_NFC = "CREATE TABLE "
			+ TABLE_NFC
			+ "("
			+ KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT,"
			+ KEY_COMPANY + " TEXT,"
			+ KEY_DESIGNATION + " TEXT,"
			+ KEY_MOB + " TEXT,"
            + KEY_WORK + " TEXT,"
			+ KEY_PH + " TEXT,"
			+ KEY_EMAIL + " TEXT,"
			+ KEY_WEBSITE + " TEXT,"
			+ KEY_ADDRESS + " TEXT,"
            + KEY_LAT + " TEXT,"
            + KEY_LNG + " TEXT,"
            + KEY_REMARK + " TEXT,"
            + KEY_FACEBOOK_ID + " TEXT,"
            + KEY_LINKEDIN_ID + " TEXT,"
            + KEY_GOOGLE_ID + " TEXT,"
            + KEY_TWITTER_ID + " TEXT,"
            + KEY_YOUTUBE_ID + " TEXT,"
            + KEY_CARD_FRONT + " BLOB,"
            + KEY_CARD_BACK + " BLOB"
			+ ")";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

    public boolean deleteNFCbyID(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NFC, KEY_ID + "=" + id, null) > 0;
    }

	/*public boolean verification(String id) throws SQLException
	{
		int count = -1;
		Cursor c = null;
		try {
			String query = "SELECT COUNT(*) FROM " + TABLE_FAVOURITES + " WHERE " + KEY_PRODUCTID + " = ?";
			SQLiteDatabase db = this.getWritableDatabase();
			c = db.rawQuery(query, new String[] {product_id});
			if (c.moveToFirst())
			{
				count = c.getInt(0);
			}
			return count > 0;
		}
		finally {
			if (c != null) {
				c.close();
			}
		}
	}*/

	public void deleteNFC(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NFC, null, null);
    }

	@Override
	public void onCreate(SQLiteDatabase db)
	{

		// creating required tables
		db.execSQL(CREATE_TABLE_NFC);

		ContentValues values = new ContentValues();
        values.put(KEY_NAME, "Kajol");
        values.put(KEY_COMPANY, "Google");
        values.put(KEY_DESIGNATION, "Android Developer");
        values.put(KEY_MOB, "9874561230");
        values.put(KEY_WORK, "0791234567");
        values.put(KEY_PH, "0792456789");
        values.put(KEY_EMAIL, "kajal.patadia@ample-arch.com");
        values.put(KEY_WEBSITE, "www.google.com");
        values.put(KEY_ADDRESS, "1600 Amphitheatre Parkway, Mountain View, CA");
        values.put(KEY_LAT, "37.4224082");
        values.put(KEY_LNG, "-122.0856086");
        values.put(KEY_REMARK, "Nothing");
        values.put(KEY_FACEBOOK_ID, "www.facebook.com");
        values.put(KEY_LINKEDIN_ID, "www.linkedin.com");
        values.put(KEY_GOOGLE_ID, "www.google.com");
        values.put(KEY_TWITTER_ID, "www.twitter.com");
        values.put(KEY_YOUTUBE_ID, "www.twitter.com");
        values.put(KEY_CARD_FRONT, favourites.getCard_front());
        values.put(KEY_CARD_BACK, favourites.getCard_back());
		db.insert(TABLE_NFC, null, values);

		/*insertFavValues.put("product_id", "6");
		insertFavValues.put("user_id", "22");
		db.insert(TABLE_FAVOURITES, null, insertFavValues);

		insertFavValues.put("product_id", "7");
		insertFavValues.put("user_id", "55");
		db.insert(TABLE_FAVOURITES, null, insertFavValues);

		insertFavValues.put("product_id", "10");
		insertFavValues.put("user_id", "45");
		db.insert(TABLE_FAVOURITES, null, insertFavValues);

		ContentValues insertVoucherValues = new ContentValues();
		insertVoucherValues.put("product_id", "7");
		insertVoucherValues.put("store_name", "Life Style");
		insertVoucherValues.put("lat", "23.057506");
		insertVoucherValues.put("lng", "72.543392");
		insertVoucherValues.put("offer_title", "50% Off");
		insertVoucherValues.put("offer_desc", "Mega Sale. 50% Off on Summer Collection. ");
		insertVoucherValues.put("start_date", "05/04/2017");
		insertVoucherValues.put("end_date", "05/05/2017");
		insertVoucherValues.put("message", "Mega Sale 50% Off on Life Style Clothing");
		insertVoucherValues.put("uuid", "e71c1a56-58e5-7b1f-abbe-9bf13e06f36a");
		insertVoucherValues.put("major", "0");
		insertVoucherValues.put("minor", "0");
		db.insert(TABLE_VOUCHER, null, insertVoucherValues);*/

		/*insertVoucherValues.put("product_id", "5");
		insertVoucherValues.put("store_name", "Levie");
		insertVoucherValues.put("lat", "23.057506");
		insertVoucherValues.put("lng", "72.543392");
		insertVoucherValues.put("offer_title", "50% Off");
		insertVoucherValues.put("offer_desc", "75% Off on HandBags ");
		insertVoucherValues.put("start_date", "05/04/2017");
		insertVoucherValues.put("end_date", "05/05/2017");
		insertVoucherValues.put("message", "Offer For limited peroid");
		insertVoucherValues.put("uuid", "f18aa677-3b40-48c5-a937-9e2c9e9f8");
		insertVoucherValues.put("major", "0");
		insertVoucherValues.put("minor", "0");
		db.insert(TABLE_VOUCHER, null, insertVoucherValues);*/


		/*ContentValues insertValues = new ContentValues();
		insertValues.put("store_name", "Ghatlodia Police Station");
		insertValues.put("lat", "23.057506");
		insertValues.put("lng", "72.543392");
		insertValues.put("offer_title", "Cashbak");
		insertValues.put("offer_desc", "70% Cashback, Hurry up. Offer till 6th April, 2017 only. Men's wear discount 50%, Women's Wear discount 75%.");
		insertValues.put("start_date", "08/03/2017");
		insertValues.put("end_date", "06/04/2017");
		db.insert(TABLE_STORELOCATION, null, insertValues);

		insertValues.put("store_name", "Vikram Appts");
		insertValues.put("lat", "23.012102");
		insertValues.put("lng", "72.522634");
		insertValues.put("offer_title", "Redeem Code");
		insertValues.put("offer_desc", "Hurry up. Offer till 6th April, 2017 only. Men's wear discount 50%, Women's Wear discount 75%");
		insertValues.put("start_date", "08/02/2015");
		insertValues.put("end_date", "14/02/2016");
		db.insert(TABLE_STORELOCATION, null, insertValues);

		insertValues.put("store_name", "Titanium City Center");
		insertValues.put("lat", "23.012102");
		insertValues.put("lng", "72.522634");
		insertValues.put("offer_title", "Whole Sale");
		insertValues.put("offer_desc", "70% Cashback");
		insertValues.put("start_date", "08/02/2015");
		insertValues.put("end_date", "14/02/2016");
		db.insert(TABLE_STORELOCATION, null, insertValues);*/
		//db.execSQL(CREATE_TABLE_TODO_TAG);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NFC);

		// create new tables
		onCreate(db);
	}

	public long createNFC(NFCModel favourites, long[] tag_ids) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, favourites.getId());
		values.put(KEY_NAME, favourites.getName());
		values.put(KEY_COMPANY, favourites.getCompany());
        values.put(KEY_DESIGNATION, favourites.getDesignation());
        values.put(KEY_MOB, favourites.getMob_no());
        values.put(KEY_WORK, favourites.getWork_no());
        values.put(KEY_PH, favourites.getPh_no());
        values.put(KEY_EMAIL, favourites.getEmail());
        values.put(KEY_WEBSITE, favourites.getWebsite());
        values.put(KEY_ADDRESS, favourites.getAddress());
        values.put(KEY_LAT, favourites.getLat());
        values.put(KEY_LNG, favourites.getLng());
        values.put(KEY_REMARK, favourites.getRemark());
        values.put(KEY_FACEBOOK_ID, favourites.getFb_id());
        values.put(KEY_LINKEDIN_ID, favourites.getLinkedin_id());
        values.put(KEY_GOOGLE_ID, favourites.getGoogle_id());
        values.put(KEY_TWITTER_ID, favourites.getTwitter_id());
        values.put(KEY_YOUTUBE_ID, favourites.getYoutube_id());
        values.put(KEY_CARD_FRONT, favourites.getCard_front());
        values.put(KEY_CARD_BACK, favourites.getCard_back());

        // insert row
		long todo_id = db.insert(TABLE_NFC, null, values);


		return todo_id;
	}

	/*public List<NFCModel> getNFCData()
	{
		List<NFCModel> todos = new ArrayList<NFCModel>();

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor c = null ;

		int value = 3 ;

		try{
			String selectQuery = TABLE_NFC + " * " + KEY_ID + " IN " + value ;

			Log.d(LOG, selectQuery);

			String Table = TABLE_FAVOURITES ;
			String[] table_Columns = new String[] { " * " };
			String[] args_Data = new String[] { "1,3,5,7" };
			String selctioonArgs = Arrays.toString(args_Data);

			selctioonArgs = selctioonArgs.replace("[","(");
			selctioonArgs = selctioonArgs.replace("]",")");

			String whereClause = KEY_ID + " IN " + selctioonArgs ;

			c = db.query(Table,table_Columns,whereClause,null,null,null,null);
		}catch (Exception e){
			e.printStackTrace();
		}
		if(c.moveToFirst())
		{
		do
			{
				Favourites fvt = new Favourites();
				fvt.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				fvt.setProduct_id((c.getString(c.getColumnIndex(KEY_PRODUCTID))));
				fvt.setUser_id(c.getString(c.getColumnIndex(KEY_USERID)));
				todos.add(fvt);
			}
			while (c.moveToNext());
		}
		return todos;
	}*/
	/*
	 * get single todo
	 */
	public NFCModel getNFCDatabyID(long todo_id) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_NFC + " WHERE " + KEY_ID + " = " + todo_id;

		Log.e(LOG, selectQuery);

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		NFCModel td = new NFCModel();
		td.setId(c.getInt(c.getColumnIndex(KEY_ID)));
		td.setName((c.getString(c.getColumnIndex(KEY_NAME))));
		td.setCompany(c.getString(c.getColumnIndex(KEY_COMPANY)));
        td.setDesignation(c.getString(c.getColumnIndex(KEY_DESIGNATION)));
        td.setMob_no(c.getString(c.getColumnIndex(KEY_MOB)));
        td.setWork_no(c.getString(c.getColumnIndex(KEY_WORK)));
        td.setPh_no(c.getString(c.getColumnIndex(KEY_PH)));
        td.setEmail(c.getString(c.getColumnIndex(KEY_EMAIL)));
        td.setWebsite(c.getString(c.getColumnIndex(KEY_WEBSITE)));
        td.setAddress(c.getString(c.getColumnIndex(KEY_ADDRESS)));
        td.setLat(c.getString(c.getColumnIndex(KEY_LAT)));
        td.setLng(c.getString(c.getColumnIndex(KEY_LNG)));
        td.setRemark(c.getString(c.getColumnIndex(KEY_REMARK)));
        td.setFb_id(c.getString(c.getColumnIndex(KEY_FACEBOOK_ID)));
        td.setLinkedin_id(c.getString(c.getColumnIndex(KEY_LINKEDIN_ID)));
        td.setGoogle_id(c.getString(c.getColumnIndex(KEY_GOOGLE_ID)));
        td.setTwitter_id(c.getString(c.getColumnIndex(KEY_TWITTER_ID)));
        td.setYoutube_id(c.getString(c.getColumnIndex(KEY_YOUTUBE_ID)));
        td.setCard_front(c.getBlob(c.getColumnIndex(KEY_CARD_FRONT)));
        td.setCard_back(c.getBlob(c.getColumnIndex(KEY_CARD_BACK)));

		return td;
	}

	public String[] getAllNFC() {
		List<String> todos = new ArrayList<String>();
		String selectQuery = "SELECT  * FROM " + TABLE_NFC;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				// adding to todo list
				todos.add((c.getString(c.getColumnIndex(KEY_ID))));
			} while (c.moveToNext());
		}

		String[] FavId = new String[todos.size()];
		FavId = todos.toArray(FavId);//now strings is the resulting array

		return FavId;
	}

	public int getNFCCount() {
		String countQuery = "SELECT  * FROM " + TABLE_NFC;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		int count = cursor.getCount();
		cursor.close();

		// return count
		return count;
	}

	/*
	 * Updating a todo
	 */
	public int updateFavourites(NFCModel favourites) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
        values.put(KEY_ID, favourites.getId());
        values.put(KEY_NAME, favourites.getName());
        values.put(KEY_COMPANY, favourites.getCompany());
        values.put(KEY_DESIGNATION, favourites.getDesignation());
        values.put(KEY_MOB, favourites.getMob_no());
        values.put(KEY_WORK, favourites.getWork_no());
        values.put(KEY_PH, favourites.getPh_no());
        values.put(KEY_EMAIL, favourites.getEmail());
        values.put(KEY_WEBSITE, favourites.getWebsite());
        values.put(KEY_ADDRESS, favourites.getAddress());
        values.put(KEY_LAT, favourites.getLat());
        values.put(KEY_LNG, favourites.getLng());
        values.put(KEY_REMARK, favourites.getRemark());
        values.put(KEY_FACEBOOK_ID, favourites.getFb_id());
        values.put(KEY_LINKEDIN_ID, favourites.getLinkedin_id());
        values.put(KEY_GOOGLE_ID, favourites.getGoogle_id());
        values.put(KEY_TWITTER_ID, favourites.getTwitter_id());
        values.put(KEY_YOUTUBE_ID, favourites.getYoutube_id());
        values.put(KEY_CARD_FRONT, favourites.getCard_front());
        values.put(KEY_CARD_BACK, favourites.getCard_back());

		// updating row
		return db.update(TABLE_NFC, values, KEY_ID + " = ?",
				new String[] { String.valueOf(favourites.getId()) });
	}


	// closing database
	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}

	/**
	 * get datetime
	 * */
	private String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}
}
