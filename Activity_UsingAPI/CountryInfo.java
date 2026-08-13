import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CountryInfo {

    public static void main(String[] args) {

        // Scanner allows the program to receive input from the user
        Scanner scanner = new Scanner(System.in);

        // API key used to authenticate the request
        String API_KEY =
                "rc_live_865a7c27860c4fb398455be2af9a66f1";

        // Display program title
        System.out.println("=================================");
        System.out.println("     COUNTRY INFORMATION API");
        System.out.println("=================================");

        // Ask the user to enter a country
        System.out.print("Enter a country name: ");

        // Read the country entered by the user and remove extra spaces
        String country = scanner.nextLine().trim();

        // Check if the user entered nothing
        if (country.isEmpty()) {
            System.out.println("Error: Country name cannot be empty.");
            scanner.close();
            return;
        }

        try {

            // Build the API URL using the country entered by the user
            // %20 replaces spaces in the country name for URL formatting
            String apiUrl =
                    "https://api.restcountries.com/countries/v5/names.common/"
                    + country.replace(" ", "%20")
                    + "?response_fields=names.common,capitals,region,population";

            // Convert the URL String into a Java URL object
            URL url = new URL(apiUrl);

            // Create an HTTP connection to the API
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            // Tell the API that we are requesting information
            connection.setRequestMethod("GET");

            // Send the API key through the Authorization header
            connection.setRequestProperty(
                    "Authorization",
                    "Bearer " + API_KEY
            );

            // Send the request and get the HTTP response code
            // 200 means the request was successful
            int responseCode = connection.getResponseCode();

            // Check if the API request was unsuccessful
            if (responseCode != 200) {

                System.out.println();
                System.out.println("Error: Country not found.");
                System.out.println(
                        "HTTP Response Code: " + responseCode
                );

                // Close the connection
                connection.disconnect();

                // Close the Scanner
                scanner.close();

                // Stop the program
                return;
            }

            // Get the JSON response from the API
            // InputStreamReader converts the response into characters
            // BufferedReader allows us to read the response
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream()
                    )
            );

            // StringBuilder stores the complete API response
            StringBuilder response = new StringBuilder();

            String line;

            // Read the JSON response line by line
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            // We are finished reading the response
            reader.close();

            // Close the HTTP connection
            connection.disconnect();

            // Convert the response into a normal String
            // This String contains the complete JSON response
            String body = response.toString();

            // Display the raw JSON received from the API
            System.out.println();
            System.out.println("=================================");
            System.out.println("RAW JSON RESPONSE");
            System.out.println("=================================");
            System.out.println(body);

            // Extract the country name from the JSON
            String countryName =
                    extractString(body, "\"common\":\"");

            // Extract the capital from the JSON
            String capital =
                    extractCapital(body);

            // Extract the region from the JSON
            String region =
                    extractString(body, "\"region\":\"");

            // Extract the population from the JSON
            String population =
                    extractNumber(body, "\"population\":");

            // Display the extracted information
            System.out.println();
            System.out.println("=================================");
            System.out.println("EXTRACTED INFORMATION");
            System.out.println("=================================");

            System.out.println("Country Name : " + countryName);
            System.out.println("Capital      : " + capital);
            System.out.println("Region       : " + region);
            System.out.println("Population   : " + population);

        } catch (Exception e) {

            // Handles connection or other errors
            System.out.println();
            System.out.println("Error connecting to the API.");
            System.out.println("Please check your internet connection.");

        }

        // Close Scanner when the program is finished
        scanner.close();
    }


    // Extract text values from the JSON
    public static String extractString(String json, String key) {

        // Find the position of the requested JSON key
        int start = json.indexOf(key);

        // If the key does not exist, return "Not found"
        if (start == -1) {
            return "Not found";
        }

        // Move the starting position after the key
        start += key.length();

        // Find the quotation mark where the value ends
        int end = json.indexOf("\"", start);

        // Extract and return the value between start and end
        return json.substring(start, end);
    }


    // Extract the capital from the capitals section
    public static String extractCapital(String json) {

        // Find the "capitals" section in the JSON
        int start = json.indexOf("\"capitals\"");

        // If capitals cannot be found
        if (start == -1) {
            return "Not found";
        }

        // Find the "name" field after the capitals section
        start = json.indexOf("\"name\":\"", start);

        // If the name cannot be found
        if (start == -1) {
            return "Not found";
        }

        // Move past "name":" to the actual capital name
        start += "\"name\":\"".length();

        // Find the quotation mark where the capital name ends
        int end = json.indexOf("\"", start);

        // Extract and return the capital
        return json.substring(start, end);
    }


    // Extract number values from the JSON
    public static String extractNumber(String json, String key) {

        // Find the position of the requested number field
        int start = json.indexOf(key);

        // If the field does not exist
        if (start == -1) {
            return "Not found";
        }

        // Move the starting position after the key
        start += key.length();

        // Start checking characters from the number
        int end = start;

        // Continue while the characters are digits
        while (end < json.length()
                && Character.isDigit(json.charAt(end))) {

            end++;
        }

        // Extract and return the number
        return json.substring(start, end);
    }
}