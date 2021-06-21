package com.dummy.base.report;

import com.relevantcodes.extentreports.ExtentReports;
import org.testng.ITestContext;
import org.testng.Reporter;

import java.io.File;

public class ExtentManager {
    private static ExtentReports extent;
    private static ITestContext context;

    public synchronized static ExtentReports getInstance() {
        if (extent == null) {
            File outputDirectory = new File(context.getOutputDirectory());
            File resultDirectory = new File(outputDirectory.getParentFile(), "html");
            extent = new ExtentReports(System.getProperty("user.dir") + "/Extent-Report/AutomationReport.html", true);
            Reporter.log("Extent Report Directory" + resultDirectory, true);
            extent.addSystemInfo("Host Name", "PeopleNtech").addSystemInfo("Environment",
                    "Quality").addSystemInfo("User Name", "menad").addSystemInfo("URL", "dummy.restapiexample.com");
            extent.loadConfig(new File("../APIFrameWork/src/main/resources/report-config.xml"));
        }
        return extent;
    }

    public static void setOutputDirectory(ITestContext context) {
        ExtentManager.context = context;
    }
}//"json:target/cucumber-reports/CucumberTestReport.json"