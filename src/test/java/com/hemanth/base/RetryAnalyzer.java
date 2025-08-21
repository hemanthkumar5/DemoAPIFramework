package com.hemanth.base;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private int count = 0;
    private final int maxRetry = 2;

    @Override
    public boolean retry(ITestResult iTestResult) {
        return count++<maxRetry;
    }
}
