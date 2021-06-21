package com.dummy.restapi;

import com.dummy.base.TestBase;
import com.dummy.base.report.ExtentTestManager;
import io.restassured.response.Response;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import java.util.HashMap;
// YOU CAN RUN YOUR TEST CASE FROM JENKINS IF YOU WANT

public class TestRest extends TestBase {
    private final Logger logger = Logger.getLogger(TestRest.class);

    @Test(priority = 0)
    public void validateGetingAllEmployees() {
        Response response = select("employees");
        ExtentTestManager.log("Here all data we get from data base", logger);
        ExtentTestManager.log(response.asString(), logger);
        ExtentTestManager.log("Response code is " + response.getStatusCode(), logger);
        String jsonString = response.asString();
        validationMessage("Successfully! All records has been fetched.", jsonString);
    }

    @Test(priority = 1)
    public void validateGettingIndivdualEmployee() {
        Response response = select("employee/5");
        ExtentTestManager.log("Here all data we get for employee with id 5", logger);
        ExtentTestManager.log(response.asString(), logger);
        ExtentTestManager.log("Response code is " + response.getStatusCode(), logger);
        String jsonString = response.asString();
        validationMessage("Successfully! Record has been fetched.", jsonString);
    }

    @Test(priority = 4)
    public void validateAddingEmployee() {
        HashMap data = new HashMap();
        data.put("name", "pnt");
        data.put("salary", 10000);
        data.put("age", 24);
        Response response = add("create", data);
        ExtentTestManager.log("The data we send assigned to ID that is down below", logger);
        ExtentTestManager.log(response.asString(), logger);
        ExtentTestManager.log("Response code is " + response.getStatusCode(), logger);
        String jsonString = response.asString();
        validationMessage("Successfully! Record has been added.", jsonString);
    }

    @Test(priority = 3)
    public void validateUpdatingEmployee() {
        HashMap data = new HashMap();
        data.put("salary", 10000);
        Response response = update("update/5", data);
        ExtentTestManager.log("The salary of employee with id 5 is updated, the result down below", logger);
        ExtentTestManager.log(response.asString(), logger);
        ExtentTestManager.log("Response code is " + response.getStatusCode(), logger);
        String jsonString = response.asString();
        validationMessage("Successfully! Record has been updated.", jsonString);
    }

    @Test(priority = 2)
    public void validateDeltingEmployee() {
        Response response = delet("delete/6");
        ExtentTestManager.log("The employee with id 6 is delted the result down below", logger);
        ExtentTestManager.log(response.asString(), logger);
        ExtentTestManager.log("Response code is " + response.getStatusCode(), logger);
        String jsonString = response.asString();
        validationMessage("Successfully! Record has been deleted", jsonString);
    }
}
