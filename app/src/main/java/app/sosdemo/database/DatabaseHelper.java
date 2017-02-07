package app.sosdemo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import app.sosdemo.model.FileModel;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private Context context;
    private SQLiteDatabase database;

    //
//    /**
//     * Constructor
//     * *
//     */
    public DatabaseHelper(Context context) {
        super(context, DBUtils.DATABASE_NAME, null, DBUtils.DATABASE_VERSION);
        this.context = context;
    }

    //
//    /**
//     * Create database tables if it does not exists.
//     * *
//     */
//    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DBUtils.DB_CREATE_FILE_TABLE);
        this.database = db;
    }

    //
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //
//    /**
//     * Open Databases
//     * *
//     */
    public void openDataBase() throws SQLException {
        database = this.getWritableDatabase();
    }

    //
    @Override
    public synchronized void close() {
        // if (database != null && database.isOpen())
        // database.close();
        // super.close();
    }

    //--------------------------------SMS START------------------------------------

    /**
     * Insert SMS
     *
     * @param
     */

    public long insertSMS(FileModel fileModel) {
        if (!database.isOpen()) {
            openDataBase();
        }
        try {
            // database.beginTransaction();
            ContentValues values = new ContentValues();
            values.put(DBUtils.COLUMN_FILE_FILEPATH, fileModel.getFilepath());
            values.put(DBUtils.COLUMN_FILE_AWCODE, fileModel.getAwcode());
            values.put(DBUtils.COLUMN_FILE_DATETIME, fileModel.getDatetime());
            return database.insert(DBUtils.FILE_TABLE, null, values);

        } catch (Exception e) {
            e.printStackTrace();
            return -1;

        } finally {
            close();
            SQLiteDatabase.releaseMemory();
        }
    }


    /**
     * get SMS All SMSList
     *
     * @param
     * @return
     */
    public ArrayList<FileModel> getFileList() {
        final ArrayList<FileModel> fileList = new ArrayList<FileModel>();
        if (!database.isOpen()) {
            openDataBase();
        }
        Cursor cursor = null;
        try {
            String query = "Select * from " + DBUtils.FILE_TABLE;
            cursor = database.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                FileModel model = null;
                for (int i = 0; i < cursor.getCount(); i++) {
                    model = new FileModel();
                    model.setFilepath(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_FILE_FILEPATH)));
                    model.setAwcode(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_FILE_AWCODE)));
                    model.setDatetime(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_FILE_DATETIME)));
                    fileList.add(model);
                    cursor.moveToNext();
                }
                fileList.trimToSize();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            close();
            if (cursor != null) {
                cursor.close();
                SQLiteDatabase.releaseMemory();
            }
        }
        return fileList;
    }


    /**
     * Delete SMS
     */


    /**
     * Delete SMS by id
     */

    public void deleteSMS(String awcode) {
        if (!database.isOpen()) {
            openDataBase();
        }
        try {
            database.delete(DBUtils.FILE_TABLE, DBUtils.COLUMN_FILE_AWCODE + "=" + awcode, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    //--------------------------------SMS END------------------------------------

}