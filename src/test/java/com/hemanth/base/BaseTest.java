package com.hemanth.base;

import com.hemanth.core.RequestFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;

public class BaseTest {

    protected final RequestFactory requestFactory = new RequestFactory();

    @AfterMethod(alwaysRun = true)
    public void logFailurePayloads(ITestResult result){
        // Hook for attaching request/response if you integrate a reporter later (Allure, Extent)

    }
}
