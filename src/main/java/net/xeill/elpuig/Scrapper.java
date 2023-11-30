package net.xeill.elpuig;

import com.opencsv.CSVWriter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
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

        List<WebElement> filasDeArmas = driver.findElements(By.className("yubwxbvslulbauytxdhyofbyjh"));

        ArrayList<String> listaEnlaces = new ArrayList<String>();

        for (WebElement fila : filasDeArmas) {
            List<WebElement> armas = fila.findElements(By.className("brzpbogxsgrlikcnlwpafrzdyt"));

            for (WebElement arma : armas) {
                listaEnlaces.add(arma.findElement(By.tagName("a")).getAttribute("href"));
            }
        }

        String archivocsv1 = "datos_armas.csv";

        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(archivocsv1))) {
            String[] header = {"Nombre del arma", "Damage LMB", "Damage RMB", "Kill Award", "Running Speed", "Side"};
            csvWriter.writeNext(header);

            for (String enlace : listaEnlaces) {
                driver.get(enlace);

                WebElement nombre = driver.findElement(By.className("rdmwocwwwyeqwxiiwtdwuwgwkh"));
                String nombreArma = nombre.getText();

                List<WebElement> stats = driver.findElements(By.className("oenizlvgmxdjluppuqjqdwtwng"));

                String[] datosArmaList = new String[6];
                datosArmaList[0] = nombreArma;

                int i = 1;
                for (WebElement stat : stats) {
                    WebElement numeros = stat.findElement(By.className("jykqpwpklhfwmblgijelpcvbzy"));
                    datosArmaList[i] = numeros.getText();
                    i++;
                    if (i >= 6) {
                        break; // Salir del bucle si ya se recogieron todas las estadísticas necesarias
                    }
                }

                csvWriter.writeNext(datosArmaList);
                System.out.println("Imprimiendo en datos_armas.csv");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            generarArchivoXML(listaEnlaces, driver);
            driver.quit();
        }
    }
    public void generarArchivoXML(ArrayList<String> listaEnlaces, WebDriver driver) {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;

        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element armasElement = document.createElement("armas");
            document.appendChild(armasElement);

            for (String enlaace : listaEnlaces) {
                driver.get(enlaace);
                List<WebElement> stats = driver.findElements(By.className("oenizlvgmxdjluppuqjqdwtwng"));
                WebElement nombre = driver.findElement(By.className("rdmwocwwwyeqwxiiwtdwuwgwkh"));
                String nombreArma = nombre.getText();

                Element armaElement  =document.createElement("arma");
                armasElement.appendChild(armaElement);

                Element nombreElement = document.createElement("nombre");
                nombreElement.appendChild(document.createTextNode(nombreArma));
                armaElement.appendChild(nombreElement);

                int i = 1;
                for (WebElement stat : stats) {
                    WebElement numeros = stat.findElement(By.className("jykqpwpklhfwmblgijelpcvbzy"));
                    Element statElement = document.createElement("stat" + i);
                    statElement.appendChild(document.createTextNode(numeros.getText()));
                    armaElement.appendChild(statElement);
                    i++;
                }
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes"); // Habilitar el formato
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2"); // Establecer la cantidad de espacios

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File("armas.xml"));
            transformer.transform(source, result);

            System.out.println("Archivo XML generado correctamente.");

        }catch (Exception e) {
            e.printStackTrace();
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

                //Aqui pillo el nombre de las llaves
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
            generarArchivoXML2(enlaces, driver);
            driver.quit();
        }
    }
    public void generarArchivoXML2(ArrayList<String> enlaces, WebDriver driver) {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;

            try {
                documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.newDocument();

                Element rootElement = document.createElement("llaves");
                document.appendChild(rootElement);

                for (String enlace : enlaces) {
                    driver.get(enlace);

                    List<WebElement> open = driver.findElements(By.className("uahaobodgycugfmkhrqpjvevkh"));
                    WebElement nombre = driver.findElement(By.className("rdmwocwwwyeqwxiiwtdwuwgwkh"));
                    WebElement precio = driver.findElement(By.className("bthixlgmwxbzrkuwifzzyvnpey"));

                    String nombrellave = nombre.getText();
                    String preciollave = precio.getText();

                    Element llaveElement = document.createElement("llave");
                    rootElement.appendChild(llaveElement);

                    Element nombreElement = document.createElement("nombre");
                    nombreElement.appendChild(document.createTextNode(nombrellave));
                    llaveElement.appendChild(nombreElement);

                    Element precioElement = document.createElement("precio");
                    precioElement.appendChild(document.createTextNode(preciollave));
                    llaveElement.appendChild(precioElement);

                    for (WebElement key : open) {
                        WebElement cajaQuePuedoAbrir = key.findElement(By.className("jbpjkjachbxfigkusfwkkdcznu"));
                        Element cajaElement = document.createElement("caja");
                        cajaElement.appendChild(document.createTextNode(cajaQuePuedoAbrir.getText()));
                        llaveElement.appendChild(cajaElement);
                    }

                    System.out.println("Generando datos_llaves.xml");
                }

                // exribimos en el documneto
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

                DOMSource source = new DOMSource(document);
                StreamResult result = new StreamResult(new FileWriter("llave.xml"));
                transformer.transform(source, result);

                System.out.println("Archivo XML generado correctamente.");

            } catch (ParserConfigurationException | IOException | TransformerException e) {
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
                //todas las cajas
                List<WebElement> nom_caja = driver.findElements(By.className("iwxsbgrvudiuruwvxmapevbvcl"));

                for (WebElement rarity : nom_caja) {
                    //aqui se encuentra el nombre de la caja y la metemos en la array
                    WebElement nombre_caja = rarity.findElement(By.className("rdmwocwwwyeqwxiiwtdwuwgwkh"));
                   String[] array3 = {nombre_caja.getText()};
                   //imprimimos las cajas
                    csvWriter.writeNext(array3);
                }
                System.out.println("Imprimiendo en nombre_cajas.csv");
            }
            generarArchivoXML3(listaDeEnlaces, driver);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            driver.quit();
        }
    }

    public void generarArchivoXML3(ArrayList<String> listaDeEnlaces, WebDriver driver) {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;

        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element cajaElement = document.createElement("cajas");
            document.appendChild(cajaElement);

            for (String enlace : listaDeEnlaces) {
                driver.get(enlace);
                List<WebElement> nom_caja = driver.findElements(By.className("iwxsbgrvudiuruwvxmapevbvcl"));
                Element cajasElement = document.createElement("caja");
                cajaElement.appendChild(cajasElement);

                int i = 1;
                for (WebElement cajita : nom_caja) {
                    WebElement nombre_caja = cajita.findElement(By.className("rdmwocwwwyeqwxiiwtdwuwgwkh"));
                    Element nomElement = document.createElement("stat" + i);
                    nomElement.appendChild(document.createTextNode(nombre_caja.getText()));
                    cajasElement.appendChild(nomElement);
                    i++;
                }
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File("cajas.xml"));
            transformer.transform(source, result);
        }catch (Exception e) {
            e.printStackTrace();
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
            generarArchivoXMLSkins(datosSkins);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    public void generarArchivoXMLSkins(ArrayList<String[]> datosSkins) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element skinsElement = document.createElement("skins");
            document.appendChild(skinsElement);

            for (String[] datoSkin : datosSkins) {
                String nombreCaja = datoSkin[0];
                String nombreSkin = datoSkin[1];

                // Crear elemento XML de la skin
                Element skinElement = document.createElement("skin");
                skinsElement.appendChild(skinElement);

                // Crear elemento XML del nombre de la caja
                Element cajaElement = document.createElement("nombre_caja");
                cajaElement.appendChild(document.createTextNode(nombreCaja));
                skinElement.appendChild(cajaElement);

                // Crear elemento XML del nombre de la skin
                Element nombreElement = document.createElement("nombre_skin");
                nombreElement.appendChild(document.createTextNode(nombreSkin));
                skinElement.appendChild(nombreElement);
            }

            // Crear el archivo XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "YES");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File("skins.xml"));
            transformer.transform(source, result);

            System.out.println("Archivo XML generado correctamente");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
