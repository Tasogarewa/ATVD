package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class AliExpressTest {
    private WebDriver chromeDriver;
    private WebDriverWait wait;
    private static final String baseUrl = "https://www.aliexpress.com/";

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--start-fullscreen");
        chromeOptions.setImplicitWaitTimeout(Duration.ofSeconds(15));
        this.chromeDriver = new ChromeDriver(chromeOptions);
        this.wait = new WebDriverWait(chromeDriver, Duration.ofSeconds(10)); // increased wait time
    }

    @BeforeMethod
    public void preconditions() {
        chromeDriver.get(baseUrl);
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (chromeDriver != null) {
            chromeDriver.quit();
        }
    }

    @Test
    public void searchProductTest() {
        // Перевіряємо наявність пошукового поля
        WebElement searchField = chromeDriver.findElement(By.tagName("input"));
        org.junit.Assert.assertNotNull(searchField);
        System.out.println(String.format("Name attribute: %s", searchField.getAttribute("name")) +
                String.format("\nId attribute: %s", searchField.getAttribute("id")) +
                String.format("\nType attribute: %s", searchField.getAttribute("type")) +
                String.format("\nValue attribute: %s", searchField.getAttribute("value")) +
                String.format("\nPosition : (%d;%d)", searchField.getLocation().x, searchField.getLocation().y) +
                String.format("\nSize attribute: %d%d", searchField.getSize().height, searchField.getSize().width));

        // Перевіряємо, чи поле вводу доступне для введення
        WebElement searchBox = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("search-words")));
        Assert.assertTrue(searchBox.isEnabled(), "Поле пошуку не доступне для введення");

        // Очищаємо поле перед введенням
        searchBox.clear();
        searchBox.sendKeys("Laptop");

        // Перевірка, що значення введено
        String inputValue = searchBox.getAttribute("value");
        Assert.assertEquals(inputValue, "Laptop", "Поле пошуку не містить введене значення");

        // Клік по кнопці пошуку за допомогою точного XPath або перевірки класів
        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"_full_container_header_23_\"]/div[2]/div/div[1]/div/input[2]")));
        searchButton.click();

        // Очікуємо, поки результати пошуку будуть видимі після переходу за новим посиланням
        wait.until(ExpectedConditions.urlContains("wholesale-Laptop"));

        // Перевірка, чи відображаються результати пошуку
        WebElement results = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"card-list\"]/div[1]/div/div/a/div[1]/div[1]/div[1]/div[1]")));
        Assert.assertTrue(results.isDisplayed(), "Результати пошуку не відображаються");

        // Зміна посилання для сортування результатів
        chromeDriver.get("https://www.aliexpress.com/w/wholesale-Laptop.html?g=y&SearchText=Laptop&selectedSwitches=filterCode%3Abigsale");

        // Перевірка, чи результати сортування відображаються
        WebElement sortButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"root\"]/div[1]/div/div[1]/div[1]/div[2]/span/span/span[2]")));
        sortButton.click();

        // Очікуємо, поки нові результати відсортовані
        WebElement sortedResults = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"card-list\"]/div[1]/div/div/a/div[1]/div[1]/div[1]/div[1]")));
        Assert.assertTrue(sortedResults.isDisplayed(), "Результати після сортування не відображаються");
    }
}