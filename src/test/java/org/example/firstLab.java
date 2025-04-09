package org.example;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class FirstLab {
    private WebDriver chromeDriver;
    private static final String baseUrl = "https://www.nmu.org.ua/ua/";
@BeforeClass(alwaysRun = true)
    public void setUp(){
    WebDEriverManager.chromedriver().setup();
    ChromeOptions chromeOptions = new ChromeOptions();
    chromeOptions.addArguments("--start-fullscreen");
    chromeOptions.setImplicitWaitTimeout(Duration.ofSeconds(15));
    this.chromeDriver = new ChromeDriver(chromeOptions);
}
}