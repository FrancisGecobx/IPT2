import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CountryInfo {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // REST Countries API key
        String API_KEY =
                "rc_live_865a7c27860c4fb398455be2af9a66f1";

        System.out.println("=================================");
        System.out.println("     COUNTRY INFORMATION API");
        System.out.println("=================================");

        System.out.print("Enter a country name: ");
        String country = scanner.nextLine().trim();

        if (country.isEmpty()) {
            System.out.println("Error: Country name cannot be empty.");
            scanner.close();
            return;
        }

        try {

            // Create the API URL using the country entered by the user
            String apiUrl =
                    "https://api.restcountries.com/countries/v5/names.common/"
                    + country.replace(" ", "%20")
                    + "?response_fields=names.common,capitals,region,population";

            URL url = new URL(apiUrl);

            // Create HTTP connection
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            // Send API key
            connection.setRequestProperty(
                    "Authorization",
                    "Bearer " + API_KEY
            );

            // Check if the request was successful
            int responseCode = connection.getResponseCode();

            if (responseCode != 200) {

                System.out.println();
                System.out.println("Error: Country not found.");
                System.out.println(
                        "HTTP Response Code: " + responseCode
                );

                connection.disconnect();
                scanner.close();
                return;
            }

            // Read the JSON response
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream()
                    )
            );

            StringBuilder response = new StringBuilder();

            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            connection.disconnect();

            String body = response.toString();

            // Display the raw JSON
            System.out.println();
            System.out.println("=================================");
            System.out.println("RAW JSON RESPONSE");
            System.out.println("=================================");
            System.out.println(body);

            // Extract the four required fields
            String countryName =
                    extractString(body, "\"common\":\"");

            String capital =
                    extractCapital(body);

            String region =
                    extractString(body, "\"region\":\"");

            String population =
                    extractNumber(body, "\"population\":");

            // Display extracted information
            System.out.println();
            System.out.println("=================================");
            System.out.println("EXTRACTED INFORMATION");
            System.out.println("=================================");

            System.out.println("Country Name : " + countryName);
            System.out.println("Capital      : " + capital);
            System.out.println("Region       : " + region);
            System.out.println("Population   : " + population);

        } catch (Exception e) {

            System.out.println();
            System.out.println("Error connecting to the API.");
            System.out.println("Please check your internet connection.");

        }

        scanner.close();
    }


    // Extract text values from the JSON
    public static String extractString(String json, String key) {

        int start = json.indexOf(key);

        if (start == -1) {
            return "Not found";
        }

        start += key.length();

        int end = json.indexOf("\"", start);

        return json.substring(start, end);
    }


    // Extract the capital from the capitals array
    public static String extractCapital(String json) {

    // Find the "capitals" section first
    int start = json.indexOf("\"capitals\"");

    if (start == -1) {
        return "Not found";
    }

    // Find "name" after the capitals section
    start = json.indexOf("\"name\":\"", start);

    if (start == -1) {
        return "Not found";
    }

    // Move past "name":" 
    start += "\"name\":\"".length();

    // Find the ending quote
    int end = json.indexOf("\"", start);

    return json.substring(start, end);
}


    // Extract number values from the JSON
    public static String extractNumber(String json, String key) {

        int start = json.indexOf(key);

        if (start == -1) {
            return "Not found";
        }

        start += key.length();

        int end = start;

        while (end < json.length()
                && Character.isDigit(json.charAt(end))) {

            end++;
        }

        return json.substring(start, end);
    }
}

