package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.DatabaseHelper.ACCOUNT_NO;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.DatabaseHelper.AMOUNT;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.DatabaseHelper.DATE;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.DatabaseHelper.EXPENSE_TYPE;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.DatabaseHelper.TRANSACTIONS;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {

    private DatabaseHelper dbHelper;

    public PersistentTransactionDAO() {
            dbHelper = DatabaseHelper.getInstance();
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String strDate= dateFormat.format(date);

        ContentValues contentValues = new ContentValues();
        contentValues.put(DATE, strDate);
        contentValues.put(ACCOUNT_NO, accountNo);
        contentValues.put(EXPENSE_TYPE, expenseType.toString());
        contentValues.put(AMOUNT, amount);

        db.insert(TRANSACTIONS, null, contentValues);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TRANSACTIONS + ";";
        Cursor cursor = db.rawQuery(query, null);
        return getResultFromCursor(cursor);
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TRANSACTIONS + " LIMIT " + limit + ";";
        Cursor cursor = db.rawQuery(query, null);
        return getResultFromCursor(cursor);
    }

    public List<Transaction> getResultFromCursor(Cursor cursor){
        List<Transaction> transactions = new LinkedList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            String date = cursor.getString(cursor.getColumnIndex(DATE));
            String accountNo= cursor.getString(cursor.getColumnIndex(ACCOUNT_NO));
            String expenseType = cursor.getString(cursor.getColumnIndex(EXPENSE_TYPE));
            double amount = cursor.getDouble(cursor.getColumnIndex(AMOUNT));
            try {
                transactions.add(new Transaction(
                        dateFormat.parse(date),
                        accountNo,
                        ExpenseType.valueOf(expenseType),
                        amount));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return transactions;
    }
}
