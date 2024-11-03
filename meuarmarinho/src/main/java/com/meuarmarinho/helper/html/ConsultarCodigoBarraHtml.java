package com.meuarmarinho.helper.html;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;


public class ConsultarCodigoBarraHtml {

        // Inicializa o WebDriver uma vez para ser reutilizado
        private WebDriver driver;

    public ConsultarCodigoBarraHtml() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Configura o navegador para rodar em modo headless
        options.addArguments("--disable-gpu"); // Otimiza o headless em algumas plataformas
        options.addArguments("--window-size=1920,1080"); // Define o tamanho da janela (opcional, mas útil)

        this.driver = new ChromeDriver(options); // Passa as opções para o ChromeDriver
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

        public void acessarPagina() {
            driver.get("https://www.gs1.org/services/verified-by-gs1/results");
        }

        public void buscarProdutoPorGTIN(String gtin) {
            WebElement acceptPrivacy = driver.findElement(By.id("onetrust-accept-btn-handler"));
            acceptPrivacy.click();
            WebElement searchInput = driver.findElement(By.cssSelector("input[placeholder='Enter a barcode number/GTIN']"));
            searchInput.sendKeys(gtin);
            WebElement searchButton = driver.findElement(By.cssSelector("button[type='submit']"));
            searchButton.click();

            try{
                WebElement acceptTermsUser = driver.findElement(By.className("btn-accept"));
                if( acceptTermsUser.isDisplayed() ) {
                    acceptTermsUser.click();
                    System.out.println("Botão clicado.");
                }

            } catch (org.openqa.selenium.NoSuchElementException e){
                System.out.println("O botão não está presente.");
            }

        }

        public String capturarNomeProduto() {
//            WebElement productName = driver.findElement(By.tagName("h3"));
            WebElement nomeProduto = driver.findElement(By.xpath("//div[@class='col-md-12']/h3"));
            return nomeProduto.getText();
        }


        public String capturarImagemProduto() {
            String link = null;

            try{
                WebElement productImageLink = driver.findElement(By.xpath("//td/a"));
                link = productImageLink.getAttribute("href");
//                downloadImage(link, "imagem_baixada.jpg");
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(!link.isBlank()){
                return link;
            } else {
                return null;
            }
        }

        public void fecharNavegador() {
            driver.quit();
        }

        public static void main(String[] args) {
            ConsultarCodigoBarraHtml teste = new ConsultarCodigoBarraHtml();

            try {
                // Executa as ações do teste
                teste.acessarPagina();
                teste.buscarProdutoPorGTIN("7898688540627");

                // Captura e imprime nome e descrição do produto
                String nomeProduto = teste.capturarNomeProduto();
//                String imagemProduto = teste.capturarImagemProduto();
                System.out.println("Nome do Produto: " + nomeProduto);
//                System.out.println("Descrição do Produto: " + imagemProduto);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // Fecha o navegador
                teste.fecharNavegador();
            }
        }

    public static String buscaProdutoPorCodigoBarra(String codigoBarra) {
        ConsultarCodigoBarraHtml teste = new ConsultarCodigoBarraHtml();
        String nomeProduto = null;
        try {
            // Executa as ações do teste
            teste.acessarPagina();
            teste.buscarProdutoPorGTIN(codigoBarra);

            // Captura e imprime nome e descrição do produto
            nomeProduto = teste.capturarNomeProduto();
//                String imagemProduto = teste.capturarImagemProduto();
            System.out.println("Nome do Produto: " + nomeProduto);

//                System.out.println("Descrição do Produto: " + imagemProduto);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Fecha o navegador
            teste.fecharNavegador();
        }
        return nomeProduto;
    }

    public static void downloadImage(String url, String destinationFile) throws IOException {
        URL imageUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        // Verifica se a resposta foi bem-sucedida
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
                 FileOutputStream out = new FileOutputStream(destinationFile)) {

                byte[] dataBuffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    out.write(dataBuffer, 0, bytesRead);
                }

                System.out.println("Imagem baixada com sucesso: " + destinationFile);
            }
        } else {
            System.out.println("Falha ao conectar à URL da imagem.");
        }
        connection.disconnect();
    }


}
