/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.BeforeClass;
import org.junit.Test;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.PersistentExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest{
    private static ExpenseManager expenseManager;

    @BeforeClass
    public static void testAddAccount(){

        Context context = ApplicationProvider.getApplicationContext();
        DatabaseHelper.makeInstance(context);
        expenseManager = new PersistentExpenseManager();
        expenseManager.addAccount("8070", "BOC", "Nipun", 20000);
    }
    @Test
    public void checkAccount(){
        try {
            assertEquals("8070", expenseManager.getAccountsDAO().getAccount("8070").getAccountNo());
        } catch (InvalidAccountException e) {
            fail();
        }
    }
    @Test
    public void checkTransaction(){
        int size_before = expenseManager.getTransactionLogs().size();
        try {
            expenseManager.updateAccountBalance("8070", 3, 12, 2021, ExpenseType.EXPENSE, "1000");
        } catch (InvalidAccountException e) {
            fail();
        }
        int size_after = expenseManager.getTransactionLogs().size();
        assertEquals(1, (size_after - size_before));

    }
}