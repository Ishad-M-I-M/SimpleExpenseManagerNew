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

import android.app.Application;
import android.test.ApplicationTestCase;

import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    private PersistentAccountDAO accountDAO;
    private PersistentTransactionDAO transactionDAO;
    private final String DB_NAME = "test_expense";

    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        accountDAO = new PersistentAccountDAO(getContext(), DB_NAME);
        transactionDAO = new PersistentTransactionDAO(getContext(), DB_NAME);
    }

    @Override
    public void tearDown() throws Exception {
        accountDAO.close();
        transactionDAO.close();
        getContext().deleteDatabase(DB_NAME);
        super.tearDown();
    }

    public void testInsertAccount() throws InvalidAccountException {
        //Insert An account
        Account account = new Account("A0001","ABC Bank", "Ishad-M-I-M", 1000.00);
        accountDAO.addAccount(account);

        //get the account back and check
        Account checkAccount = accountDAO.getAccount("A0001");
        assertEquals("ABC Bank", checkAccount.getBankName());
        assertEquals("Ishad-M-I-M", checkAccount.getAccountHolderName());
        assertEquals(1000.00, checkAccount.getBalance());
    }

    public void testInsertTransaction() {
        //Insert an transaction
        Date today = new Date();
        transactionDAO.logTransaction(today, "A0001", ExpenseType.EXPENSE, 1000.00);

        //check transaction
        List<Transaction> transactions = transactionDAO.getAllTransactionLogs();
        Transaction checkTransaction = transactions.get(0);
        assertEquals("A0001", checkTransaction.getAccountNo());
        assertEquals(ExpenseType.EXPENSE, checkTransaction.getExpenseType());
        assertEquals(1000.00, checkTransaction.getAmount());
    }
}