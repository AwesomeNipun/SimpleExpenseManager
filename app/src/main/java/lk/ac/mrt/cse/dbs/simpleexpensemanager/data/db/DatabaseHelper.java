package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.MainActivity;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String ACCOUNTS = "accounts";
    public static final String TRANSACTIONS = "transactions";
    public static final String ACCOUNT_NO = "accountNo";
    public static final String BANK_NAME = "bankName";
    public static final String ACCOUNT_HOLDER_NAME = "accountHolderName";
    public static final String BALANCE = "balance";
    public static final String EXPENSE_TYPE = "expenseType";
    public static final String AMOUNT = "amount";
    public static final String DATE = "date";

    private static DatabaseHelper dbHelper = null;

    public static void makeInstance(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public static DatabaseHelper getInstance(){
        return dbHelper;
    }

    public DatabaseHelper(@Nullable Context context) {
        super(context, "190653L", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + ACCOUNTS + "(" + ACCOUNT_NO + " VARCHAR(25) NOT NULL PRIMARY KEY, " + BANK_NAME + " VARCHAR(255) NOT NULL, " + ACCOUNT_HOLDER_NAME + " VARCHAR(255) NOT NULL, " + BALANCE + " DOUBLE NOT NULL);");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TRANSACTIONS + "( `id` INTEGER PRIMARY KEY AUTOINCREMENT, " + DATE + " DATE NOT NULL, " + ACCOUNT_NO + " VARCHAR(25) NOT NULL, " + EXPENSE_TYPE + " VARCHAR(25) NOT NULL, " + AMOUNT + " DOUBLE NOT NULL, FOREIGN KEY (accountNO) REFERENCES Accounts(accountNo));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ACCOUNTS + ";");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TRANSACTIONS + ";");
        onCreate(sqLiteDatabase);
    }
}
