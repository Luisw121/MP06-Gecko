package net.xeill.elpuig;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class Main {

    public static void main(String[] args) {
        System.out.println(System.getenv("PATH"));
        System.out.println(System.getenv("HOME"));

        System.setProperty("webdriver.gecko.driver", "src/main/resources/geckodriver");
        FirefoxOptions options = new FirefoxOptions();

        WebDriver driver = new FirefoxDriver(options);
        driver.get("https://counterstrike.fandom.com/wiki/Counter-Strike:_Global_Offensive#Hostage_Rescue");

        WebElement element = driver.findElement(By.cssSelector("wikia-gallery-item"));

        String texto = element.getText();

        System.out.println("Weapon: " + texto);




        driver.quit();
    }
}