package com.dummy.base;

import com.dummy.base.report.ExtentManager;
import com.dummy.base.report.ExtentTestManager;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class TestBase {
    public static WebDriver driver;
    public static ExtentReports extent;
    public static String baseURI = RestAssured.baseURI = "http://dummy.restapiexample.com/api/v1/";
    private static final Logger LOGGER = Logger.getLogger(TestBase.class);

    public static void captureScreenshot(WebDriver driver, String screenshotName) {
        DateFormat dateFormat = new SimpleDateFormat("HH_mm_ss");
        Date date = new Date();
        // --> dateFormat.format(date);
        File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        try {
            FileUtils.copyFile(file, new File(System.getProperty("user.dir") + "/screenshots/" + screenshotName + " " + dateFormat.format(date) + ".jpg"));
            System.out.println("Screenshot captured");
        } catch (Exception e) {
            System.out.println("Exception while taking screenshot " + e.getMessage());
        }
    }

    public Response select(String endPoint) {
        Response response = RestAssured.given().when()
                .get(endPoint).then().assertThat().statusCode(200).extract().response();

        return response;
    }

    public Response add(String endPoint, HashMap hash) {
        Response response = RestAssured.given().when()
                .contentType(ContentType.JSON).body(hash)
                .post(endPoint).then().assertThat()
                .statusCode(200).extract().response();
        return response;
    }

    public Response update(String endpoint, HashMap hash) {
        Response response = RestAssured.given().when()
                .contentType(ContentType.JSON).body(hash)
                .put(endpoint).then().assertThat()
                .statusCode(200).extract().response();

        return response;
    }

    public Response delet(String endpoint) {
        Response response = RestAssured.given().when().delete(endpoint)
                .then().assertThat().statusCode(200).extract().response();

        return response;
    }

    public void validationMessage(String expeted, String json) {
        Assert.assertEquals(json.contains(expeted), true, "the strings are not equal");
        ExtentTestManager.log("The response verfied is down below", LOGGER);
        ExtentTestManager.log(expeted, LOGGER);
    }

    @BeforeSuite
    public void extentSetup(ITestContext context) {
        ExtentManager.setOutputDirectory(context);
        extent = ExtentManager.getInstance();
    }

    @BeforeMethod
    public void startExtent(Method method) {
        String className = method.getDeclaringClass().getSimpleName();
        ExtentTestManager.startTest(method.getName());
        ExtentTestManager.getTest().assignCategory(className);
    }

    protected String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }

    @AfterMethod
    public void afterEachTestMethod(ITestResult result) {
        ExtentTestManager.getTest().getTest().setStartedTime(getTime(result.getStartMillis()));
        ExtentTestManager.getTest().getTest().setEndedTime(getTime(result.getEndMillis()));
        for (String group : result.getMethod().getGroups()) {
            ExtentTestManager.getTest().assignCategory(group);
        }

        if (result.getStatus() == 1) {
            ExtentTestManager.getTest().log(LogStatus.PASS, "Test Passed");
        } else if (result.getStatus() == 2) {
            ExtentTestManager.getTest().log(LogStatus.FAIL, getStackTrace(result.getThrowable()));
        } else if (result.getStatus() == 3) {
            ExtentTestManager.getTest().log(LogStatus.SKIP, "Test Skipped");
        }

        ExtentTestManager.endTest();
        extent.flush();
        if (result.getStatus() == ITestResult.FAILURE) {
            captureScreenshot(driver, result.getName());
        }
    }

    public Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();

    }
    //reporting finish

    @AfterSuite
    public void generateReport() {
        extent.close();
    }


}
