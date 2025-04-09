package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class firstLab {
    private WebDriver chromeDriver;
    private static final String baseUrl = "https://www.nmu.org.ua/ua/";
    @BeforeClass(alwaysRun = true)
    public void setUp(){
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--start-fullscreen");
        chromeOptions.setImplicitWaitTimeout(Duration.ofSeconds(15));
        this.chromeDriver = new ChromeDriver(chromeOptions);

    }
    @BeforeMethod
    public void preconditions()
    {
        chromeDriver.get(baseUrl);
    }
    @AfterClass(alwaysRun = true)
    public void tearDown(){chromeDriver.quit();}
  @Test
    public void testHeaderExists()
  {
      WebElement header = chromeDriver.findElement(By.id("header"));
      Assert.assertNotNull(header);
  }
@Test
    public void testClickOn()
{
    WebElement clickOn = chromeDriver.findElement(By.xpath("/html/body/div[1]/footer/div[1]/div/div/div[1]/div/div/nav/div/ul/li[1]/a"));
Assert.assertNotNull(clickOn);
clickOn.click();
Assert.assertNotEquals(baseUrl, chromeDriver.getCurrentUrl());
}
@Test
    public void testSearchFileOnForStudentPage()
    {
        String Pageurl = "https://old.nmu.org.ua/ua/content/students/";
        chromeDriver.get(baseUrl + Pageurl);
        WebElement searchField = chromeDriver.findElement(By.tagName("input"));
   Assert.assertNotNull(searchField);
   System.out.println(String.format("Name attribute: %s", searchField.getAttribute("name"))+
           String.format("\nId attribute: %s", searchField.getAttribute("id"))+
           String.format("\nType attribute: %s", searchField.getAttribute("type"))+
           String.format("\nValue attribute: %s", searchField.getAttribute("value"))+
                   String.format("\nPosition : (%d;%d)", searchField.getLocation().x,searchField.getLocation().y)+
                           String.format("\nSize attribute: %d%d", searchField.getSize().height,searchField.getSize().width)
           );
    }
    @Test
        public void testSlider()
        {
            WebElement nextButton = chromeDriver.findElement(By.className("swiper-button-next"));
        WebElement nextButtonByCss = chromeDriver.findElement(By.cssSelector("div.swiper-button-next"));
            Assert.assertEquals(nextButton, nextButtonByCss);
            WebElement previousButton = chromeDriver.findElement(By.className("swiper-button-prev"));
            for(int i=0;i<20;i++)
            {
                if(nextButton.getAttribute("class").contains("disabled"))
                {
                    previousButton.click();
                    Assert.assertTrue(previousButton.getAttribute("class").contains("disabled"));
                    Assert.assertFalse(nextButton.getAttribute("class").contains("disabled"));
                }
                else
                {
                    nextButton.click();
                    Assert.assertTrue(nextButton.getAttribute("class").contains("disabled"));
                    Assert.assertFalse(previousButton.getAttribute("class").contains("disabled"));
                }
            }
        }
}
