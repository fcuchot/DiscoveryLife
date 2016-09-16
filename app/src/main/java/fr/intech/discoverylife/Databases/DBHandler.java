package fr.intech.discoverylife.Databases;

/**
 * Created by famille on 15/09/2016.
 */
    import android.content.ContentValues;
    import android.content.Context;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.database.sqlite.SQLiteOpenHelper;

    import java.util.ArrayList;
    import java.util.List;

    import fr.intech.discoverylife.Classes.Landmark;

public class DBHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "DiscoveryLife";
    // Contacts table name
    private static final String TABlE_LANDMARK = "Landmark";
    //  Table Columns names
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_Description = "description";
    private static final String COLUMN_IDUSER = "idUser";
    private static final String COLUMN_LONGITUDE = "longitude";
    private static final String COLUMN_LATITUDE = "latitude";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABlE_LANDMARK + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_TITLE + " TEXT NOT NULL, "
                + COLUMN_Description + " TEXT, " + COLUMN_IDUSER + " INTEGER NOT NULL, " + COLUMN_LATITUDE + " FLOAT( 10, 6 ) NOT NULL, "
                + COLUMN_LONGITUDE + " FLOAT( 10, 6 ) NOT NULL" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABlE_LANDMARK);
// Creating tables again
        onCreate(db);
    }

    // Adding new Landmark
    public void addLandmark(Landmark landmark) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, landmark.getTitle()); // Landmark Title
        values.put(COLUMN_Description, landmark.getDescription()); // Landmark Description
        values.put(COLUMN_IDUSER, landmark.getIdUser()); // Landmark idUser
        values.put(COLUMN_LATITUDE, landmark.getLatitude()); // Landmark Latitude
        values.put(COLUMN_LONGITUDE, landmark.getLongitude()); // Landmark Longitude
        // Inserting Row
        db.insert(TABlE_LANDMARK, null, values);
        db.close(); // Closing database connection
    }

    // Getting All Landmarks for specific user
    public List<Landmark> getAllLandmarks(int idUser) {
        List<Landmark> landmarkList = new ArrayList<Landmark>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + TABlE_LANDMARK + " WHERE " + COLUMN_IDUSER + " == " + idUser;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Landmark landmark = new Landmark();
                landmark.setId(Integer.parseInt(cursor.getString(0)));
                landmark.setTitle(cursor.getString(1));
                landmark.setDescription(cursor.getString(2));
                landmark.setIdUser(cursor.getInt(3));
                landmark.setLatitude(cursor.getFloat(4));
                landmark.setLongitude(cursor.getFloat(5));

                // Adding Landmark to list
                landmarkList.add(landmark);
            } while (cursor.moveToNext());
        }
        // return Landmarks list
        return landmarkList;
    }

    // Updating a Landmark
    public int updateLandmark(Landmark landmark) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, landmark.getTitle());
        values.put(COLUMN_Description, landmark.getDescription());
    // updating row
        return db.update(TABlE_LANDMARK, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(landmark.getId())});
    }

    // Deleting a Landmark
    public void deleteLandmark(Landmark landmark) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABlE_LANDMARK, COLUMN_ID + " = ?",
                new String[] { String.valueOf(landmark.getId()) });
        db.close();
    }
}