package seleniumGrid;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class SeleniumGridTest {
    WebDriver driver;

    @Parameters({"browserName"})
    @BeforeClass
    public void setUp(String browserName) throws MalformedURLException {
        // Set the address of the hub
        String node = "http://localhost:4444";
        // Provide capabilities for choosing browser
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setBrowserName(browserName);
        // Initialize driver
        driver = new RemoteWebDriver(new URL(node), caps);

        // Open page
        driver.get("https://training-support.net");
    }

    @Test
    public void firefoxNodeTest() {
        // Print page title
        System.out.println("Page title: " + driver.getTitle());
    }

    @AfterClass
    public void tearDown() {
        // Close browser
        driver.quit();
    }

}
