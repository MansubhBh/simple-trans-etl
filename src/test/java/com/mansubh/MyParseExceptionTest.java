package com.mansubh;

import com.mansubh.exception.MyParseException;
import com.mansubh.service.MyCsvParser;
import com.mansubh.util.DateUtil;
import com.opencsv.CSVParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.shadow.com.univocity.parsers.csv.CsvParser;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
public class MyParseExceptionTest {

    private MyCsvParser myCsvParser;


    @Test
    public void testMyParseException(){
        String incorrectDateFormat = "12/15-2019";
        Assertions.assertThrows(MyParseException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                DateUtil.parseDate(incorrectDateFormat);
            }
        });
    }

    @Test
    public void testMyCsvParserWithRandomFile(){
        String randomfilename = "random.csv";
        Assertions.assertThrows(MyParseException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                myCsvParser = new MyCsvParser(randomfilename);
            }
        });
    }

    @Test
    public void testMyCsvParserByConstructorOverloading(){
        String filename = "mytransactions.csv";
        Assertions.assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                myCsvParser = new MyCsvParser(filename);
            }
        });

    }


}
