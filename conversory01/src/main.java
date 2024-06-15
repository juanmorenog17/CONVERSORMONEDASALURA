import java.io.InputStreamReader;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class main {
    private static final String API_KEY = "2fe69033726c68cd70e0d6c2";
    private static JsonObject conversionRates;

    public static void main(String[] args) {
        // Obtener tasas de conversión de la API
        obtenerTasasDeConversion();

        EXTERNA:
        while(true) {
            System.out.println("CONVERSOR DE MONEDAS");
            System.out.println("""
                    1 - Soles peruanos a dolares
                    2 - Pesos mexicanos a dolares
                    3 - Pesos colombianos a dolares
                    4 - Salir
                    """);

            System.out.println("Ingresa una opción");
            Scanner leer = new Scanner(System.in);
            char opcion = leer.next().charAt(0);

            switch (opcion) {
                case '1':
                    convertir("PEN", "Soles Peruanos");
                    break;
                case '2':
                    convertir("MXN", "Pesos Mexicanos");
                    break;
                case '3':
                    convertir("COP", "Pesos Colombianos");
                    break;
                case '4':
                    System.out.println("Cerrando programa");
                    break EXTERNA;
                default:
                    System.out.println("Opción incorrecta");
            }//fin switch

        }//fin while
    }//fin main

    //////////////////////definir funcion//////////////////////
    static void convertir(String codigoMoneda, String pais) {
        Scanner leer = new Scanner(System.in);
        System.out.printf("Ingrese la cantidad de %s : ", pais);
        double cantidadMoneda = leer.nextDouble();

        if (conversionRates != null && conversionRates.has(codigoMoneda)) {
            double tasaCambio = conversionRates.get(codigoMoneda).getAsDouble();
            double dolares = cantidadMoneda / tasaCambio;
            dolares = (double) Math.round(dolares * 100d) / 100;

            System.out.println("--------------------------------");
            System.out.println("|   Tienes $" + dolares + " Dolares   |");
            System.out.println("--------------------------------");
        } else {
            System.out.println("No se pudo obtener la tasa de cambio para " + codigoMoneda);
        }
    }
    ///////////////////fin convertir///////////////////////////

    static void obtenerTasasDeConversion() {
        try {
            String url_str = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/USD";

            URL url = new URL(url_str);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            JsonObject jsonobj = root.getAsJsonObject();

            conversionRates = jsonobj.getAsJsonObject("conversion_rates");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al obtener las tasas de conversión");
        }
    }
}//fin class
