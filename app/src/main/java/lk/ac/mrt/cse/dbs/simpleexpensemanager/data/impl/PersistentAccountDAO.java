package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.DatabaseHelper.ACCOUNTS;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.DatabaseHelper.ACCOUNT_HOLDER_NAME;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.DatabaseHelper.ACCOUNT_NO;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.DatabaseHelper.BALANCE;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.DatabaseHelper.BANK_NAME;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.DatabaseHelper;

public class PersistentAccountDAO implements AccountDAO {

    private DatabaseHelper dbHelper;

    public PersistentAccountDAO() {
        dbHelper = DatabaseHelper.getInstance();
    }

    @Override
    public List<String> getAccountNumbersList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT " + ACCOUNT_NO + " FROM " + ACCOUNTS + ";";
        Cursor cursor = db.rawQuery(query, null);

        List<String> account_numbers = new LinkedList<>();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            String account_no = cursor.getString(0);
            account_numbers.add(account_no);
        }
        return account_numbers;
    }

    @Override
    public List<Account> getAccountsList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + ACCOUNTS + ";";
        Cursor cursor = db.rawQuery(query, null);

        List<Account> accounts = new LinkedList<>();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            String accountNo = cursor.getString(cursor.getColumnIndex(ACCOUNT_NO));
            String bankName = cursor.getString(cursor.getColumnIndex(BANK_NAME));
            String accountHolderName = cursor.getString(cursor.getColumnIndex(ACCOUNT_HOLDER_NAME));
            double balance = cursor.getDouble(cursor.getColumnIndex(BALANCE));
            Account account = new Account(accountNo, bankName, accountHolderName, balance);
            accounts.add(account);
        }
        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + ACCOUNTS + " WHERE " + ACCOUNT_NO + "=? ;";
        Cursor cursor = db.rawQuery(query, new String[]{accountNo});
        if (cursor.moveToFirst()) {
            String bankName = cursor.getString(1);
            String accountHolderName = cursor.getString(2);
            double balance = cursor.getDouble(3);
            return new Account(accountNo, bankName, accountHolderName, balance);
        }else{
            throw new InvalidAccountException("Invalid Account");
        }
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            getAccount(account.getAccountNo());
        } catch (InvalidAccountException e) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ACCOUNT_NO, account.getAccountNo());
            contentValues.put(BANK_NAME, account.getBankName());
            contentValues.put(ACCOUNT_HOLDER_NAME, account.getAccountHolderName());
            contentValues.put(BALANCE, account.getBalance());

            db.insert(ACCOUNTS, null, contentValues);
        }
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] arg = {accountNo};
        db.delete(ACCOUNTS, ACCOUNT_NO+"=?", arg);

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Account account = getAccount(accountNo);
        double updated_balance = account.getBalance();
        if(expenseType == ExpenseType.EXPENSE){
            updated_balance -= amount;
        }else{
            updated_balance += amount;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(BALANCE, updated_balance);

        String[] arg = {accountNo};
        db.update(ACCOUNTS, contentValues, ACCOUNT_NO+"=?", arg);
    }
}
