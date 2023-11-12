package net.xeill.elpuig;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.ArrayList;
import java.util.List;

public class Scrapper{

    public Scrapper() throws InterruptedException {
        System.out.println(System.getenv("PATH"));
        System.out.println(System.getenv("HOME"));

        System.setProperty("webdriver.gecko.driver", "src/main/resources/geckodriver");
    }

    public void sacarStatsArmas() throws InterruptedException {
        FirefoxOptions options = new FirefoxOptions();
        WebDriver driver = new FirefoxDriver(options);

        driver.get("https://wiki.cs.money/");

        List<WebElement> filasDeArmas = driver.findElements(By.className("cjhhjgtyxaptnnnlzzbcznjjpt"));

        ArrayList<String> listaEnlaces = new ArrayList<String>();

        //bucle para recorrer todas las armas
        for (WebElement fila : filasDeArmas) {

            List<WebElement> armas = fila.findElements(By.className("brzpbogxsgrlikcnlwpafrzdyt"));

            for (WebElement arma : armas) {
                // Guardar los enlaces em un ArrayList<String>
                listaEnlaces.add(arma.findElement(By.tagName("a")).getAttribute("href"));
            }


        }

        // Lista de enlaces completa

        for (String enlace : listaEnlaces) {

            // Hago el get de la página y cojo las estadísticas
            driver.get(enlace);
            //Aqui pillo las estats de las armas
            List<WebElement> stats = driver.findElements(By.className("oenizlvgmxdjluppuqjqdwtwng"));
            WebElement nombre = driver.findElement(By.className("rdmwocwwwyeqwxiiwtdwuwgwkh"));

            //Aqui pillo el nombre de las armas y las imprimo
            String nombreArma = nombre.getText();
            System.out.println(nombreArma);

            //Bucle para pillar todas el nombre de las estats con los numeros(valores) y lo imprimo
            for (WebElement stat : stats) {
                //WebElement sts = driver.findElement(By.className("hxtzmvitxocobaxesbucdwkqrv"));
                WebElement estats = stat.findElement(By.className("coybuydtexahpqmeiusrucdvqy"));
                WebElement numeros = stat.findElement(By.className("jykqpwpklhfwmblgijelpcvbzy"));
                System.out.println(estats.getText() + " " + numeros.getText());
            }
            System.out.println("______________________");
        }
        driver.quit();
    }
    public void sacarllaves() throws InterruptedException {

    }
}

/*
List<WebElement> keys = driver.findElements(By.className("nmkpyzbxbnvwcxlxzdoiftdmpj"));

        ArrayList<String> todoslosenlaces = new ArrayList<>();

        for (WebElement llave : keys) {
            //
            List<WebElement> llaves = driver.findElements(By.className("dshqqaonthvvsarqtrbbrgjvvb"));

            for (WebElement klk : llaves) {
                todoslosenlaces.add(klk.findElement(By.tagName("a")).getAttribute("href"));
            }

        }

        for (String todos : todoslosenlaces) {

            driver.get(todos);

            List<WebElement> llve = driver.findElements(By.className("wiigrmhqvyvyncniatqresceen"));
            for (WebElement hola : llve) {
                WebElement tutia = hola.findElement(By.className("kxmatkcipwonxvwweiqqdoumxg"));
                System.out.println(tutia.getText());
            }


        }
 */