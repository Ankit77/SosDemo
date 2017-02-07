package app.sosdemo.database;

public class DBUtils {

    /**
     * Integer Constants
     */
    public static final int DATABASE_VERSION = 3;

    /**
     * Database Name
     */
    public static final String DATABASE_NAME = "spy.db";

    /**
     * Tables Constant Names
     */
    public static final String FILE_TABLE = "file";


//


    /**
     * Columns Names
     */
    // SMS
    public static final String COLUMN_FILE_FILEPATH = "filepath";
    public static final String COLUMN_FILE_AWCODE = "awcode";
    public static final String COLUMN_FILE_DATETIME = "datetime";


    //    /**

    //SMS TABLE
    static final String DB_CREATE_FILE_TABLE = "CREATE TABLE IF NOT EXISTS " + FILE_TABLE + " (" + COLUMN_FILE_FILEPATH
            + " TEXT, " + COLUMN_FILE_AWCODE + " TEXT," + COLUMN_FILE_DATETIME + " TEXT)";


}