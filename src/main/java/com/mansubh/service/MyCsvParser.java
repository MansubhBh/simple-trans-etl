package com.mansubh.service;

import com.mansubh.exception.MyParseException;
import com.mansubh.model.Transaction;
import com.mansubh.util.DateUtil;
import com.opencsv.CSVParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MyCsvParser {

    private static final String FILE = "mytransactions.csv";
    private List<Transaction> transactions;

    public MyCsvParser() {
        this.transactions = parseTransaction(FILE);
    }

    public MyCsvParser(String filename){
        this.transactions = parseTransaction(filename);
    }

    private List<Transaction> parseTransaction(String filename) {

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filename))) {
            CSVParser parser = new CSVParser();

            return reader.lines().skip(1)
                    .map(line -> {
                        try {
                            return parser.parseLine(line);

                        } catch (IOException e) {
                            throw new MyParseException("problem while parsing ", e);
                        }
                    }).map(this::mapToTransaction)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new MyParseException("Parse problem ", e);
        }
    }

    //description,amount,transactionDate,transactionId,accountId,mcc,notes,category
    private Transaction mapToTransaction(String[] line) {
        Transaction transaction = new Transaction();
        transaction.setDescription(line[0]);
        transaction.setAmount(Double.parseDouble(line[1]));
        transaction.setTransactionDate(DateUtil.parseDate(line[2]));
        transaction.setTransactionId(line[3]);
        transaction.setAccountId(line[4]);
        transaction.setMcc(line[5]);
        transaction.setNotes(line[6]);
        transaction.setCategory(line[7]);
        return transaction;
    }

    public List<Transaction> getTransactionsWithKeyword(String keyword) {
        return transactions.stream().filter(tran -> tran.getDescription()
                .toLowerCase().contains(keyword)).collect(Collectors.toList());
    }

    public List<Transaction> getTransactionByCategory(String category) {
        return transactions.stream()
                .filter(tran -> tran.getCategory().toLowerCase().contains(category.toLowerCase()))
                .collect(Collectors.toList());
    }

    //get all transaction with the price range
    public List<Transaction> getTransactionWithinRange(int min, int max) {
        return transactions.stream().filter(tran -> tran.getAmount() >= min && tran.getAmount() <= max)
                .collect(Collectors.toList());
    }

    public List<Transaction> getTransactionByDate(String queryDate) {
        Date dateToFetch = DateUtil.parseDate(queryDate);
        return transactions.stream()
                .filter(tran -> tran.getTransactionDate().equals(dateToFetch))
                .collect(Collectors.toList());
    }

    public List<Transaction> getTransactionByMcc(String mcc) {
        return transactions.stream()
                .filter(tran -> tran.getMcc().equals(mcc))
                .collect(Collectors.toList());
    }


    public List<Transaction> filterByCondition(Predicate<Transaction> predicate) {
        return transactions.stream()
                .filter(predicate::test)
                .collect(Collectors.toList());
    }

    public double getTotalAmount() {
        double total = transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .reduce((acc, amt) -> acc + amt)
                .getAsDouble();
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.valueOf(df.format(total));
    }

}
