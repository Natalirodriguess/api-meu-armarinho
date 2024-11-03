package com.meuarmarinho.helper.teste_verifica;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.BinaryBitmap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class CodigoDeBarraReader {

    public static void main(String[] args) {
        try {
            // Carrega a imagem com o código de barras
            BufferedImage bufferedImage = ImageIO.read(new File("C:/Users/natal/OneDrive/Área de Trabalho/Estudos/Projeto-meu-armarinho/meuarmarinho/src/main/java/com/meuarmarinho/helper/codigo_barra-sem-numero.jpeg"));

            // Cria um objeto de bitmap binário a partir da imagem
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            // Inicializa o decodificador ZXing
            Result result = new MultiFormatReader().decode(bitmap);

            // Exibe o resultado
            System.out.println("Código de Barras: " + result.getText());

        } catch (IOException e) {
            System.err.println("Erro ao ler a imagem: " + e.getMessage());
        } catch (NotFoundException e) {
            System.err.println("Código de barras não encontrado na imagem.");
        }
    }
}
