package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class AppTest {

    //  CODE SMELL: Hardcoded credentials
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "password123";

    // BUG: Unused variable
    private int unusedValue = 10;

    //  CODE SMELL: Public field (should be private)
    public String name = "TestName";

    //  VULNERABILITY: SQL Injection
    public void vulnerableSql(String userInput) {
        try {
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/testdb", DB_USER, DB_PASSWORD);
            Statement stmt = con.createStatement();
            // ❌ Directly concatenating user input
            String query = "SELECT * FROM users WHERE username = '" + userInput + "'";
            stmt.executeQuery(query);
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //  CODE SMELL: Empty catch block
    public void emptyCatchExample() {
        try {
            int x = 5 / 0;
        } catch (Exception e) {
            // do nothing
        }
    }

    //  BUG: Assertion always true
    @Test
    public void testAlwaysPasses() {
        assertTrue(true);
    }

    //  CODE SMELL: Unnecessary if-statement
    @Test
    public void testAddition() {
        int sum = 2 + 2;
        if (sum == 4) { // unnecessary conditional
            System.out.println("Sum is correct!");
        }
        assertEquals(4, sum);
    }

    //  CODE SMELL: Printing stack trace instead of logging
    @Test
    public void testWithException() {
        try {
            String s = null;
            s.length(); // NullPointerException
        } catch (Exception e) {
            e.printStackTrace(); // should use a logger
        }
    }

    //  CODE SMELL: Too many responsibilities in one method
    @Test
    public void testMultipleThings() {
        int a = 5 + 5;
        assertEquals(10, a);
        String text = "abc".toUpperCase();
        assertEquals("ABC", text);
        // Doing two unrelated things — bad practice
    }

    //  BUG: Potential resource leak
    public void fileLeakExample() {
        try {
            FileReader fr = new FileReader("test.txt");
            // FileReader not closed
        } catch (IOException e) {
            System.out.println("Error reading file");
        }
    }
}
