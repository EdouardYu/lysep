package software.engineering.lysep.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SeleniumTest {
    public static void main(String[] args) throws InterruptedException {
        WebDriver driver = getWebDriver();
        driver.get("http://localhost:5173/authentication");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Login with Student:
        Thread.sleep(6000);
        WebElement connexionButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='homepage-button login']")));
        connexionButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='email']")));

        Thread.sleep(6000);
        WebElement emailField = driver.findElement(By.xpath("//input[@name='email']"));
        emailField.sendKeys("edouard.yu@eleve.isep.fr");

        Thread.sleep(1000);
        WebElement passwordField = driver.findElement(By.xpath("//input[@name='password']"));
        passwordField.sendKeys("Azerty1234!");

        Thread.sleep(1000);
        WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));
        loginButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[normalize-space()='Hey dear user!']")));
        Thread.sleep(6000);

        WebElement profilLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"root\"]/div/a[1]")));
        profilLink.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[normalize-space()='Mon profil']")));
        Thread.sleep(6000);

        WebElement editButton = driver.findElement(By.xpath("//button[normalize-space()='Edit']"));
        editButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='username']")));
        Thread.sleep(1000);


        WebElement usernameField = driver.findElement(By.xpath("//input[@name='username']"));
        usernameField.clear();
        usernameField.sendKeys("LysEdo");
        Thread.sleep(1000);

        WebElement saveButton = driver.findElement(By.xpath("//button[normalize-space()='Save']"));
        saveButton.click();
        Thread.sleep(1000);

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Edit']")));
        editButton.click();
        Thread.sleep(1000);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='username']")));
        usernameField = driver.findElement(By.xpath("//input[@name='username']"));
        usernameField.clear();
        usernameField.sendKeys("EdouardY");
        Thread.sleep(1000);

        saveButton.click();
        Thread.sleep(1000);

        WebElement modifyPasswordButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Modify Password']")));
        modifyPasswordButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[normalize-space()='Change Password']")));
        Thread.sleep(1000);

        WebElement currentPasswordField = driver.findElement(By.xpath("//div[@class='modal-content']//div[1]//input[1]"));
        currentPasswordField.sendKeys("Azerty1234!");
        Thread.sleep(1000);

        WebElement newPasswordField = driver.findElement(By.xpath("//div[@class='modal-backdrop']//div[2]//input[1]"));
        newPasswordField.sendKeys("Azerty1234!");
        Thread.sleep(1000);

        WebElement savePasswordButton = driver.findElement(By.xpath("//button[@class='modal-button modal-button-save']"));
        savePasswordButton.click();
        Thread.sleep(1000);

        WebElement calendarLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"root\"]/div/a[2]")));
        calendarLink.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[normalize-space()='Agenda']\n")));
        Thread.sleep(5000);

        WebElement weekButton = driver.findElement(By.xpath("//button[normalize-space()='Week']"));
        weekButton.click();
        Thread.sleep(5000);

        WebElement agendaButton = driver.findElement(By.xpath("//button[normalize-space()='Agenda']"));
        agendaButton.click();
        Thread.sleep(5000);

        WebElement monthButton = driver.findElement(By.xpath("//button[normalize-space()='Month']"));
        monthButton.click();
        Thread.sleep(2000);

        WebElement event = driver.findElement(By.xpath("//div[@title='Électronique/Signal Project (APP) - Projet']"));
        event.click();
        Thread.sleep(5000);

        WebElement closeButton = driver.findElement(By.xpath("//button[@class='modal-button modal-button-cancel']"));
        closeButton.click();
        Thread.sleep(1000);

        WebElement notificationsLink = driver.findElement(By.xpath("//*[@id=\"root\"]/div/a[3]"));
        notificationsLink.click();
        Thread.sleep(5000);

        WebElement alertsLink = driver.findElement(By.xpath("//*[@id=\"root\"]/div/a[4]"));
        alertsLink.click();
        Thread.sleep(5000);

        WebElement logoutLink = driver.findElement(By.xpath("//span[contains(text(),'Déconnexion')]"));
        logoutLink.click();
        Thread.sleep(1000);

        // Login with professor
        connexionButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='homepage-button login']")));
        connexionButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='email']")));

        Thread.sleep(1000);
        emailField = driver.findElement(By.xpath("//input[@name='email']"));
        emailField.sendKeys("admin@lysep.fr");

        Thread.sleep(1000);
        passwordField = driver.findElement(By.xpath("//input[@name='password']"));
        passwordField.sendKeys("Admin123!");

        Thread.sleep(1000);
        loginButton = driver.findElement(By.xpath("//button[@type='submit']"));
        loginButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[normalize-space()='Agenda']\n")));
        Thread.sleep(1000);

        WebElement newEvent = driver.findElement(By.xpath("//div[@title='Électronique des objets - Examen']"));
        newEvent.click();
        Thread.sleep(10000);

        closeButton = driver.findElement(By.xpath("//button[@class='modal-button modal-button-cancel']"));
        closeButton.click();
        Thread.sleep(1000);

        event = driver.findElement(By.xpath("//div[@title='Électronique/Signal Project (APP) - Projet']"));
        event.click();
        Thread.sleep(6000);

        WebElement editEventButton = driver.findElement(By.xpath("//button[@class='modal-button modal-button-save']"));
        editEventButton.click();
        Thread.sleep(6000);

        WebElement eventDescriptionField = driver.findElement(By.xpath("//input[@name='description']"));
        eventDescriptionField.clear();
        eventDescriptionField.sendKeys("Séance n°2");
        Thread.sleep(6000);

        WebElement saveEventButton = driver.findElement(By.xpath("//button[@class='modal-button modal-button-save']"));
        saveEventButton.click();
        Thread.sleep(1000);

        closeButton = driver.findElement(By.xpath("//button[@class='modal-button modal-button-cancel']"));
        closeButton.click();
        Thread.sleep(1000);

        driver.quit();
    }

    private static WebDriver getWebDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        return new ChromeDriver(options);
    }
}
