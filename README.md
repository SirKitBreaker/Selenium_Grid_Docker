- [Distributed Tests with Selenium and Docker](#distributed-tests-with-selenium-and-docker)
  - [Set up Selenium Grid 4 with docker-compose](#set-up-selenium-grid-4-with-docker-compose)
  - [Testing with Selenium](#testing-with-selenium)

# Distributed Tests with Selenium and Docker

This repo is to help setup Selenium Grid and Docker. Also contains a simple test to run with this setup.

## Set up Selenium Grid 4 with docker-compose

-   Create a `docker-compose.yml` file with the following services:

    ```yaml
    # Selenium Hub
    selenium-hub:
        image: selenium/hub
        ports:
            - "4442:4442"
            - "4443:4443"
            - "4444:4444"
    # Firefox Node
    firefox:
        image: selenium/node-firefox
        shm_size: 2gb
        depends_on:
            - selenium-hub
        environment:
            - SE_EVENT_BUS_HOST=selenium-hub
            - SE_EVENT_BUS_PUBLISH_PORT=4442
            - SE_EVENT_BUS_SUBSCRIBE_PORT=4443
            - SE_NODE_MAX_SESSION=5
    # Chrome Node
    chrome:
        image: selenium/node-chrome
        shm_size: 2gb
        depends_on:
            - selenium-hub
        environment:
            - SE_EVENT_BUS_HOST=selenium-hub
            - SE_EVENT_BUS_PUBLISH_PORT=4442
            - SE_EVENT_BUS_SUBSCRIBE_PORT=4443
            - SE_NODE_MAX_SESSION=5
    ```

-   Run the command to start the hub and node continers:

    ```bash
    docker-compose up -d
    ```

-   To scale up or down the number of nodes, run the following command:
    ```bash
    docker-compose up --scale [serviceName]=[number]
    ```
    For example:
    ```bash
    # Scale up firefox nodes by 2
    docker-compose up --scale firefox=2
    ```

## Testing with Selenium

To run Selenium tests on this grid with TestNG:

-   In a setup function, add the following details:

    ```Java
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
    ```

-   Create a `@Test` function to run a test on the required page.
-   Finally add a tearDown function to close browser
    ```Java
    @AfterClass
    public void tearDown() {
        // Close browser
        driver.quit();
    }
    ```
- Given below is a sample testng.xml snippet to run the test with the browserName parameter:
  ```XML
    <?xml version="1.0" encoding="UTF-16" ?>
    <!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd" >
    <suite name="GridSuite" verbose="1" parallel="tests">
        <test name="FirefoxTest">
            <parameter name="browserName" value="firefox" />
            <classes>
                <class name="[packageName.ClassName]" />
            </classes>
        </test>
    </suite>
  ```

  ## Monitoring
  View the status of browsers and the active sessions on [`localhost:4444`](http://localhost:4444)
