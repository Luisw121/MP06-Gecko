package net.xeill.elpuig;

import com.opencsv.CSVWriter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class Scrapper {

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
        //Creamos un archivo csv
        String archivocsv1 = "datos_armas.csv";

        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(archivocsv1))) {
            //Creamos array que luego se escribira en el csv
            String[] hello = {"Nombre del arma", "Damage LMB", "Damage RMB", "Kill Award", "Running Speed", "Side"};
            csvWriter.writeNext(hello);

            //ahora creamos bucle para recorrer todos los enlaces y obtnerer las estadisticas
            for (String enlace : listaEnlaces) {
                driver.get(enlace);
                List<WebElement> stats = driver.findElements(By.className("oenizlvgmxdjluppuqjqdwtwng"));
                WebElement nombre = driver.findElement(By.className("rdmwocwwwyeqwxiiwtdwuwgwkh"));
                String nombreArma = nombre.getText();

                //en este string guardaremos todas las estadisticas
                //StringBuilder estadisticas = new StringBuilder();
                String[] datosArmaArray = new String[6];

                datosArmaArray[0] = nombreArma;

                int i = 1;
                for (WebElement stat : stats) {
                    WebElement estats = stat.findElement(By.className("coybuydtexahpqmeiusrucdvqy"));
                    WebElement numeros = stat.findElement(By.className("jykqpwpklhfwmblgijelpcvbzy"));
                    //estadisticas.append("Estadisticas: " + estats.getText()).append(" ").append(numeros.getText()).append(", ");

                    // if estats == "DAMAGE LBR" --> lo meto en la posicion X

                    datosArmaArray[i] = numeros.getText();
                    i++;
                }
                //ahora escribimos en el archivo CSV
                csvWriter.writeNext(datosArmaArray);

                System.out.println("Imprimiendo en datos_armas.csv");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    public void sacarllaves() throws InterruptedException {
        FirefoxOptions options = new FirefoxOptions();
        WebDriver driver = new FirefoxDriver(options);

        driver.get("https://wiki.cs.money/keys");

        List<WebElement> filaDeLlaves = driver.findElements(By.className("nbilqzbwqcjqxplbkrncabwrdm"));

        ArrayList<String> enlaces = new ArrayList<>();

        //Con este bucle recorremos todas las llaves
        for (WebElement fila : filaDeLlaves) {

            List<WebElement> llaves = fila.findElements(By.className("kxmatkcipwonxvwweiqqdoumxg"));
            for (WebElement llave : llaves) {
                //Aqui guardamos los enlaces en un ArrayList<String>
                enlaces.add(llave.findElement(By.tagName("a")).getAttribute("href"));
            }
        }
        String archivocsv2 = "datos_llaves.csv";
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(archivocsv2))) {
            //array
            String[] datos = {"Nombre de las llaves ", "Precio de la llave", "Cajas que puedo abrir"};
            csvWriter.writeNext(datos);

            //Lista de enlaces entera
            for (String enlace : enlaces) {

                //Aqui hago el get de la pagina y cojo las lalves
                driver.get(enlace);

                //Aqui pillo las cajas que puedo abrir
                List<WebElement> open = driver.findElements(By.className("uahaobodgycugfmkhrqpjvevkh"));

                //Aqui pillo el nombre de las cajas
                WebElement nombre = driver.findElement(By.className("rdmwocwwwyeqwxiiwtdwuwgwkh"));

                //Aqui pillo el precio de la llave
                WebElement precio = driver.findElement(By.className("bthixlgmwxbzrkuwifzzyvnpey"));

                //Aqui imprimimos el nombre de las llaves
                String nombrellave = nombre.getText();

                //Aqui imprimimos el precio de las llaves
                String preciollave = precio.getText();

                //String para guardar todas las estadisticas
                StringBuilder estadisticas = new StringBuilder();

                String[] array = new String[3];

                array[0] = nombrellave;
                array[1] = preciollave;

                int i = 2;
                //Bucle para cojer las cajas que se pueden abrir con las llaves
                for (WebElement key : open) {

                    WebElement cajaQuePuedoAbrir = key.findElement(By.className("jbpjkjachbxfigkusfwkkdcznu"));


                    array[i] = cajaQuePuedoAbrir.getText();
                    i++;
                }
                csvWriter.writeNext(array);

                System.out.println("Imprimiendo en datos_llaves.csv");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    public void sacarCajas() throws InterruptedException {
        FirefoxOptions options = new FirefoxOptions();
        WebDriver driver = new FirefoxDriver(options);

        driver.get("https://wiki.cs.money/cases");

        List<WebElement> filasCajas = driver.findElements(By.className("gasovxczmdwrpzliptyovkjrjp"));

        ArrayList<String> listaDeEnlaces = new ArrayList<String>();

        for (WebElement fila : filasCajas) {

            List<WebElement> cajas = fila.findElements(By.className("kxmatkcipwonxvwweiqqdoumxg"));

            for (WebElement caja : cajas) {
                listaDeEnlaces.add(caja.findElement(By.tagName("a")).getAttribute("href"));
            }
        }

        //String del archivo CSV
        String archivocsv3 = "nombre_cajas.csv";
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(archivocsv3))) {
            //array
            String[] array = {"Nombre de la caja"};
            csvWriter.writeNext(array);

            for (String enlace : listaDeEnlaces) {

                driver.get(enlace);
                //gasovxczmdwrpzliptyovkjrjp
                List<WebElement> nom_caja = driver.findElements(By.className("iwxsbgrvudiuruwvxmapevbvcl"));
                //String para guardar todos los nombres de las cajas

                //array par aguardar los nombres de las cajas
                String[] array3 = new String[2];

                int i = 0;
                for (WebElement rarity : nom_caja) {
                    //aqui se encuentra el nombre de la caja y la metemos en la array
                    WebElement nombre_caja = rarity.findElement(By.className("rdmwocwwwyeqwxiiwtdwuwgwkh"));
                    array3[i] = nombre_caja.getText();
                    i++;
                }
                //imprimimos las cajas
                csvWriter.writeNext(array3);

                System.out.println("Imprimiendo en nombre_cajas.csv");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    public void scarSkins() throws InterruptedException {
        FirefoxOptions options = new FirefoxOptions();
        WebDriver driver = new FirefoxDriver(options);

        driver.get("https://wiki.cs.money/cases");

        List<WebElement> filasCajas = driver.findElements(By.className("gasovxczmdwrpzliptyovkjrjp"));

        ArrayList<String> listaDeEnlaces = new ArrayList<String>();

        for (WebElement fila : filasCajas) {

            List<WebElement> cajas = fila.findElements(By.className("kxmatkcipwonxvwweiqqdoumxg"));

            for (WebElement caja : cajas) {
                listaDeEnlaces.add(caja.findElement(By.tagName("a")).getAttribute("href"));
            }
        }

        //String del archivo CSV
        String archivocsv3 = "datos_skins.csv";
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(archivocsv3))) {
            //array
            String[] array = {"Nombre de la caja", " Nombre de la skin"};
            csvWriter.writeNext(array);

            //ArrayList<ArrayList<String>> datosSkins = new ArrayList<ArrayList<String>>();

            ArrayList<String[]> datosSkins = new ArrayList<String[]>();

            for (String enlace : listaDeEnlaces) {

                driver.get(enlace);

                //para coger el nombre de las cajas
                List<WebElement> nom_caja = driver.findElements(By.className("iwxsbgrvudiuruwvxmapevbvcl"));

                //para coger los nombres de la skins
                List<WebElement> skinss = driver.findElements(By.className("gasovxczmdwrpzliptyovkjrjp"));

                //nombre de las cajas
                WebElement nombre_caja = driver.findElement(By.className("rdmwocwwwyeqwxiiwtdwuwgwkh"));
                String nombre_de_caja = nombre_caja.getText();


                for (WebElement rarity : skinss) {
                    //aqui esta el nombre de la skin
                    List<WebElement> nombresSkins = rarity.findElements(By.className("szvsuisjrrqalciyqqzoxoaubw"));

                    for (WebElement nombreSkin : nombresSkins) {
                        //array par aguardar
                        String[] datoSkin = new String[2];
                        datoSkin[0] = nombre_de_caja;
                        datoSkin[1] = nombreSkin.getText();

                        datosSkins.add(datoSkin);

                    }

                }

            }

            // Lista de datos completada, escribir línea a línea
            System.out.println("Imprimiendo en nombre_cajas.csv");

            for (String[] dato : datosSkins) {
                csvWriter.writeNext(dato);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
