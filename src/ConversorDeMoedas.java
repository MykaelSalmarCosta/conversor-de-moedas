import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.http.*;
import java.net.URI;
import java.util.Scanner;

public class ConversorDeMoedas {
    private static final String API_KEY = "c75245eac08a93118939a3fe";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("""
                        Seja bem-vindo/a ao Conversor de Moedas =]
                        
                        Opções de moedas disponíveis:
                        
                        ARS - Peso Argentino
                        BOB - Boliviano da Bolivia
                        BRL - Real Brasileiro
                        CLP - Peso Chileno
                        COP - Peso Colombiano
                        USD - Dólar Americano
                        """
                );
        System.out.println("Informe a moeda base: ");
        String base = sc.next().toUpperCase();

        System.out.println("Informe a moeda destino: ");
        String target = sc.next().toUpperCase();

        System.out.println("Informe o valor: ");
        double valor = sc.nextDouble();

        try {
            double taxa = obterTaxa(base, target);
            double convertido = converterMoeda(valor, taxa);

            System.out.printf("\n%.2f %s = %.2f %s\n", valor, base, convertido, target);

        } catch (Exception e) {
            System.out.println("Erro ao realizar a conversão: " + e.getMessage());
        }

        sc.close();
    }

    public static double obterTaxa(String base, String target) throws Exception {
        String url = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/pair/" + base + "/" + target;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> resposta = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonElement elemento = JsonParser.parseString(resposta.body());
        JsonObject objectRoot = elemento.getAsJsonObject();

        if (!objectRoot.get("result").getAsString().equals("success")) {
            throw new RuntimeException("Falha ao consultar a taxa de cambio.");
        }
        return objectRoot.get("conversion_rate").getAsDouble();
    }

    public static double converterMoeda(double valor, double taxa) {
        return valor * taxa;
    }
}




