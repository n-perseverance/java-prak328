package msu.cmc.webprak;

import org.openqa.selenium.chrome.ChromeDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WebTest {

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    private WebDriver driver;
    private WebDriverWait wait;
    private String baseUrl = "http://localhost:8080";
    @BeforeEach
    void setup() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    void shutdown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private WebElement findElement(By path) {
        return wait.until(ExpectedConditions.presenceOfElementLocated
                (path));
    }
    private void click(By path) {
        wait.until(ExpectedConditions.elementToBeClickable(
                path)).click();
    }
    private void clickInElement(By path,WebElement webElement) {
        wait.until(ExpectedConditions.elementToBeClickable(
                webElement.findElement(path))).click();
    }
    private void clickWaitUrl(By path,String url) {
        wait.until(ExpectedConditions.elementToBeClickable(
                path)).click();
        wait.until(ExpectedConditions.urlContains(url));
    }

    @Test
    void example1Test() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        driver.get(baseUrl);
        wait.until(ExpectedConditions.titleIs("Центральная городская библиотека имени А. П. Чехова"));

        click(By.linkText("Читатели"));
        wait.until(ExpectedConditions.titleIs("Читатели"));

        driver.findElement(By.id("searchName")).sendKeys("Алина");
        click(By.xpath("//button[text()='Поиск']"));

        wait.until(d -> {
            List<WebElement> rows = driver.findElements(By.xpath("//table[@class='table table-striped']//tbody/tr"));
            return rows.size() == 1;
        });

        click(By.linkText("Сбросить"));

        wait.until(d -> {
            List<WebElement> rows = driver.findElements(By.xpath("//table[@class='table table-striped']//tbody/tr"));
            return rows.size() != 1; // Или другое условие
        });

        driver.findElement(By.id("searchPhone")).sendKeys("8915");
        click(By.xpath("//button[text()='Поиск']"));

        wait.until(d -> {
            List<WebElement> rows = driver.findElements(By.xpath("//table[@class='table table-striped']//tbody/tr"));
            return rows.size() == 1;
        });
    }
    @Test
    void example1Test2() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        driver.get(baseUrl);
        wait.until(ExpectedConditions.titleIs("Центральная городская библиотека имени А. П. Чехова"));
        click(By.linkText("Главная"));
        wait.until(ExpectedConditions.titleIs("Центральная городская библиотека имени А. П. Чехова"));

        click(By.linkText("Книги"));
        wait.until(ExpectedConditions.titleIs("Книги"));

        findElement(By.id("searchName")).sendKeys("1984");
        click(By.xpath("//button[text()='Поиск']"));

        wait.until(d -> {
            List<WebElement> rows = driver.findElements(By.xpath("//table[@class='table table-striped']//tbody/tr"));
            return rows.size() == 1;
        });

        click(By.xpath("//a[contains(@class, 'btn-secondary') and text()='Сбросить']"));

        wait.until(d -> {
            List<WebElement> rows = driver.findElements(By.xpath("//table[@class='table table-striped']//tbody/tr"));
            return rows.size() != 1;
        });

        findElement(By.id("searchPublisher")).sendKeys("Эксмо");
        click(By.xpath("//button[text()='Поиск']"));

        wait.until(d -> {
            List<WebElement> rows = driver.findElements(By.xpath("//table[@class='table table-striped']//tbody/tr"));
            return rows.size() == 2;
        });

        click(By.xpath("//a[contains(@class, 'btn-secondary') and text()='Сбросить']"));

        wait.until(d -> {
            List<WebElement> rows = driver.findElements(By.xpath("//table[@class='table table-striped']//tbody/tr"));
            return rows.size() != 2;
        });

        findElement(By.id("searchAuthor")).sendKeys("Ору");
        click(By.xpath("//button[text()='Поиск']"));

        wait.until(d -> {
            List<WebElement> rows = driver.findElements(By.xpath("//table[@class='table table-striped']//tbody/tr"));
            return rows.size() == 1;
        });

        click(By.xpath("//a[contains(@class, 'btn-secondary') and text()='Сбросить']"));

        wait.until(d -> {
            List<WebElement> rows = driver.findElements(By.xpath("//table[@class='table table-striped']//tbody/tr"));
            return rows.size() != 1;
        });

        findElement(By.id("searchIsbn")).sendKeys("978-5-6046264-0-8");
        click(By.xpath("//button[text()='Поиск']"));

        wait.until(d -> {
            List<WebElement> rows = driver.findElements(By.xpath("//table[@class='table table-striped']//tbody/tr"));
            return rows.size() == 1;
        });
    }

    @Test
    void use_case1() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        driver.get(baseUrl);
        wait.until(ExpectedConditions.titleIs("Центральная городская библиотека имени А. П. Чехова"));

        click(By.linkText("Читатели"));
        wait.until(ExpectedConditions.titleIs("Читатели"));

        int initialReadersCount = driver.findElements(By.xpath("//table[@class='table table-striped']//tbody/tr")).size();

        click(By.xpath("//a[contains(@class, 'btn-success') and text()='Добавить читателя']"));

        wait.until(ExpectedConditions.titleIs("Центральная городская библиотека имени А. П. Чехова"));

        findElement(By.id("fullName")).sendKeys("Иванов Иван Иванович");
        findElement(By.id("phone")).sendKeys("89991234567");
        findElement(By.id("address")).sendKeys("ул. Пушкина, д.10, кв.5");

        click(By.xpath("//button[contains(text(), 'Сохранить')]"));

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("addReaderModal")));

        wait.until(d -> {
            int currentReadersCount = driver.findElements(By.xpath("//table[@class='table table-striped']//tbody/tr")).size();
            return currentReadersCount == initialReadersCount + 1;
        });

        driver.findElement(By.id("searchPhone")).sendKeys("89991234567");
        click(By.xpath("//button[text()='Поиск']"));

        wait.until(d -> {
            List<WebElement> rows = driver.findElements(By.xpath("//table[@class='table table-striped']//tbody/tr"));
            return rows.size() == 1;
        });

        click(By.xpath("//a[contains(@class, 'btn-primary') and text()='Просмотр']"));

        findElement(By.id("name")).clear();
        findElement(By.id("name")).sendKeys("Test");

        driver.findElement(By.xpath("//button[text()='Сохранить изменения']")).click();

        wait.until(ExpectedConditions.titleIs("Читатели"));

        driver.findElement(By.id("searchName")).sendKeys("Test");
        click(By.xpath("//button[text()='Поиск']"));

        wait.until(d -> {
            List<WebElement> rows = driver.findElements(By.xpath("//table[@class='table table-striped']//tbody/tr"));
            return rows.size() == 1;
        });

        click(By.xpath("//a[contains(@class, 'btn-primary') and text()='Просмотр']"));

        click(By.xpath("//a[contains(@class, 'btn-danger') and text()='Удалить читателя']"));

        Alert alert = wait.until(ExpectedConditions.alertIsPresent());

        assertEquals("Вы уверены, что хотите удалить этого читателя?", alert.getText());

        alert.accept();

        wait.until(d -> {
            List<WebElement> rows = driver.findElements(By.xpath("//table[@class='table table-striped']//tbody/tr"));
            return rows.size() == 10;
        });

    }

    @Test
    void use_case2() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        driver.get(baseUrl);
        wait.until(ExpectedConditions.titleIs("Центральная городская библиотека имени А. П. Чехова"));

        click(By.linkText("Читатели"));
        wait.until(ExpectedConditions.titleIs("Читатели"));

        driver.findElement(By.id("searchId")).sendKeys("17");
        click(By.xpath("//button[text()='Поиск']"));

        wait.until(d -> {
            List<WebElement> rows = driver.findElements(By.xpath("//table[@class='table table-striped']//tbody/tr"));
            return rows.size() == 1;
        });

        click(By.xpath("//a[contains(@class, 'btn-primary') and text()='Просмотр']"));

        WebElement addHistoryButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@href,'addHistory') and contains(text(),'Добавить Запись в Историю')]")
        ));
        addHistoryButton.click();

        wait.until(ExpectedConditions.titleIs("Добавить запись в историю"));

        Select bookSelect = new Select(driver.findElement(By.name("isbn")));
        bookSelect.selectByIndex(1);

        driver.findElement(By.name("copyId")).sendKeys("1");

        LocalDate today = LocalDate.now();
        LocalDate returnDate = today.plusDays(7);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedIssueDate = today.format(formatter);
        String formattedReturnDate = returnDate.format(formatter);

        WebElement issueDateField = driver.findElement(By.name("issueDate"));
        issueDateField.clear();
        issueDateField.sendKeys(formattedIssueDate);

        WebElement returnDateField = driver.findElement(By.name("returnDate"));
        returnDateField.clear();
        returnDateField.sendKeys(formattedReturnDate);

        driver.findElement(By.xpath("//button[text()='Добавить']")).click();

        wait.until(ExpectedConditions.titleIs("Читатель"));

        wait.until(d -> {
            List<WebElement> currentHistory = driver.findElements(
                    By.xpath("//h1[contains(text(),'История')]/following-sibling::table//tbody/tr")
            );
            return currentHistory.size() == 1;
        });
    }

    @Test
    void use_case3() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        driver.get(baseUrl);
        wait.until(ExpectedConditions.titleIs("Центральная городская библиотека имени А. П. Чехова"));
        click(By.linkText("Главная"));
        wait.until(ExpectedConditions.titleIs("Центральная городская библиотека имени А. П. Чехова"));

        click(By.linkText("Книги"));
        wait.until(ExpectedConditions.titleIs("Книги"));

        findElement(By.id("searchName")).sendKeys("1984");
        click(By.xpath("//button[text()='Поиск']"));

        wait.until(d -> {
            List<WebElement> rows = driver.findElements(By.xpath("//table[@class='table table-striped']//tbody/tr"));
            return rows.size() == 1;
        });

        click(By.xpath("//a[contains(@class, 'btn-primary') and text()='Просмотр']"));

        wait.until(d -> {
            List<WebElement> rows = driver.findElements(By.xpath("//table[@class='table table-striped']//tbody/tr"));
            return rows.size() == 2;
        });

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//form[contains(@action, 'addCopy')]//button[contains(., '+ Добавить новый экземпляр')]")
        )).click();

        wait.until(d -> {
            List<WebElement> rows = driver.findElements(By.xpath("//table[@class='table table-striped']//tbody/tr"));
            return rows.size() == 3;
        });
    }

    @Test
    void use_case4() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        driver.get(baseUrl);
        wait.until(ExpectedConditions.titleIs("Центральная городская библиотека имени А. П. Чехова"));
        click(By.linkText("Главная"));
        wait.until(ExpectedConditions.titleIs("Центральная городская библиотека имени А. П. Чехова"));

        click(By.linkText("Книги"));
        wait.until(ExpectedConditions.titleIs("Книги"));

        findElement(By.id("searchName")).sendKeys("Война и мир");
        click(By.xpath("//button[text()='Поиск']"));

        wait.until(d -> {
            List<WebElement> rows = driver.findElements(By.xpath("//table[@class='table table-striped']//tbody/tr"));
            return rows.size() == 1;
        });

        click(By.xpath("//a[contains(@class, 'btn-primary') and text()='Просмотр']"));

        click(By.xpath("//a[contains(@class, 'btn-primary') and text()='Просмотр']"));

        click(By.xpath("//a[contains(@class, 'btn-primary') and text()='Переход на страницу читателя']"));

    }

    }