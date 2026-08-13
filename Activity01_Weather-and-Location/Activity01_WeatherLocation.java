import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Activity01_WeatherLocation {

    public static void main(String[] args) {

        String apiKey = "46d1e4c3acfd46f49fa2bd2c5e60b83d";

        double latitude;
        double longitude;

        Scanner dataIn = new Scanner(System.in);

        System.out.print("Enter Latitude: ");
        latitude = dataIn.nextDouble();

        System.out.print("Enter Longitude: ");
        longitude = dataIn.nextDouble();

        try {

            // Get data from OpenWeatherMap
            String response = getWeatherData(apiKey, latitude, longitude);

            // Display both Location and Weather
            displayInformation(response, latitude, longitude);

        } catch (IOException e) {

            System.out.println("Error connecting to OpenWeatherMap.");
            System.out.println(e.getMessage());

        } catch (JSONException e) {

            System.out.println("Error reading API data.");
            System.out.println(e.getMessage());
        }

        dataIn.close();
    }

    // Connect to OpenWeatherMap API
    private static String getWeatherData(
            String apiKey,
            double latitude,
            double longitude) throws IOException {

        String apiUrl =
                "https://api.openweathermap.org/data/2.5/weather"
                + "?lat=" + latitude
                + "&lon=" + longitude
                + "&appid=" + apiKey;

        URL url = new URL(apiUrl);

        HttpURLConnection connection =
                (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");

        try (
                InputStream inputStream = connection.getInputStream();
                Scanner scanner = new Scanner(inputStream)
        ) {

            scanner.useDelimiter("\\A");

            return scanner.hasNext()
                    ? scanner.next()
                    : "";

        } finally {

            connection.disconnect();
        }
    }

    // Display Location + Weather
    private static void displayInformation(
            String weatherData,
            double latitude,
            double longitude) throws JSONException {

        JSONObject json = new JSONObject(weatherData);

        // -------------------------
        // LOCATION INFORMATION
        // -------------------------

        String location = json.optString(
                "name",
                "Unknown Location"
        );

        // -------------------------
        // WEATHER INFORMATION
        // -------------------------

        JSONArray weatherArray =
                json.getJSONArray("weather");

        JSONObject weather =
                weatherArray.getJSONObject(0);

        JSONObject main =
                json.getJSONObject("main");

        String description =
                weather.getString("description");

        double temperature =
                main.getDouble("temp") - 273.15;

        double humidity =
                main.getDouble("humidity");

        // -------------------------
        // DISPLAY RESULT
        // -------------------------

        System.out.println();
        System.out.println("======================================");
        System.out.println("       LOCATION AND WEATHER");
        System.out.println("======================================");

        System.out.println();
        System.out.println("LOCATION INFORMATION");
        System.out.println("--------------------------------------");
        System.out.println("Location   : " + location);
        System.out.println("Latitude   : " + latitude);
        System.out.println("Longitude  : " + longitude);

        System.out.println();
        System.out.println("WEATHER INFORMATION");
        System.out.println("--------------------------------------");
        System.out.println("Description: " + description);
        System.out.println(
                "Temperature: "
                + String.format("%.1f", temperature)
                + "°C"
        );
        System.out.println("Humidity   : " + humidity + "%");

        System.out.println();
        System.out.println("======================================");
    }
}