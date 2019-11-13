package com.mansubh;

import com.mansubh.model.Transaction;
import com.mansubh.service.MyCsvParser;
import com.mansubh.util.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

@RunWith(JUnitPlatform.class)
public class MyCsvParserTest {

    private MyCsvParser csv;

    @BeforeEach
    public void beforeEachTest() {
        csv = new MyCsvParser();
    }

    @Test
    @DisplayName("Test total amount expense")
    public void testTotalAmountInTransactions() {
        double actualAmount = csv.getTotalAmount();
        System.out.println("actualAmount = " + actualAmount);
        double expectedAmount = 139225.82;
        Assertions.assertEquals(expectedAmount, actualAmount);
    }

    @Test
    @DisplayName("Test transaction for a specific merchant keyword")
    public void testTransactionWithKeyword() {
        String merchant = "woolworths";
        List<Transaction> actualResult = csv.getTransactionsWithKeyword(merchant);

        boolean allHaveWoolworth = actualResult.stream()
                .map(tran -> tran.getDescription().toLowerCase().contains(merchant))
                .reduce((next, agg) -> next && agg)
                .get();

        Assertions.assertTrue(allHaveWoolworth, "All transaction must have " + merchant);

    }

    @Test
    @DisplayName("Test transaction for the specific category")
    public void testTransactionByCategory() {
        String category = "Groceries";
        List<Transaction> actualList = csv.getTransactionByCategory(category);

        boolean allHaveGroceruies = actualList.stream()
                .map(tran -> tran.getCategory().toLowerCase().contains(category.toLowerCase()))
                .reduce((next, agg) -> next && agg)
                .get();

        Assertions.assertTrue(allHaveGroceruies, "All transaction must have category " + category);
    }


    @Test
    @DisplayName("Test transaction for specific amount range ")
    public void testTransactionWithinRange() {
        int min = 10;
        int max = 15;
        List<Transaction> translist = csv.getTransactionWithinRange(min, max);
        boolean isTranInrange = translist.stream()
                .map(tran -> tran.getAmount() >= min && tran.getAmount() <= max)
                .reduce((next, agg) -> next && agg)
                .get();
        Assertions.assertTrue(isTranInrange, "Transaction must be in range");
    }

    @Test
    @DisplayName("Test transaction for specific date")
    public void testTransactionByDate() {
        String queryDate = "2019-09-10";
        Date dateToQuery = DateUtil.parseDate(queryDate);
        boolean allDateAreCorrect = csv.getTransactionByDate(queryDate).stream()
                .map(tran -> tran.getTransactionDate().equals(dateToQuery))
                .reduce((next, agg) -> next && agg)
                .get();
        Assertions.assertTrue(allDateAreCorrect, "All dates must match");
    }

    @Test
    @DisplayName("Test transaction by Mcc")
    public void testTransactionByMcc() {
        String mcc = "0230";
        boolean allHaveMcc = csv.getTransactionByMcc(mcc).stream()
                .map(tran -> tran.getMcc().equals(mcc))
                .reduce((agg, next) -> next && agg)
                .get();
        Assertions.assertTrue(allHaveMcc, "All must have the mcc" + mcc);
    }

    @Test
    @DisplayName("Test filter by predicate condition ")
    public void testTransactionByFilterCondition() {
        Predicate<Transaction> ifContainsColes = transaction -> transaction.getDescription().toLowerCase().contains("coles");
        List<Transaction> transactionList = csv.filterByCondition(ifContainsColes);
        boolean mustHaveColes = transactionList.stream()
                .map(tran -> tran.getDescription().toLowerCase().contains("coles"))
                .reduce((next, agg) -> next && agg)
                .get();
        Assertions.assertTrue(mustHaveColes, "Transactions must have coles");
    }

    @Test
    @DisplayName("Test my Transaction getter")
    public void testTransactionModel(){
        Transaction transaction = new Transaction();
        transaction.setTransactionId("1234");
        transaction.setAccountId("5678");
        transaction.setNotes("testnote");

        Assertions.assertEquals("5678", transaction.getAccountId());
        Assertions.assertEquals("testnote",transaction.getNotes());
        Assertions.assertEquals("1234", transaction.getTransactionId());

    }


}
