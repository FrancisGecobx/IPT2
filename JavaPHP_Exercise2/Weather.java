import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Weather {

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
            String weatherData = getWeatherData(apiKey, latitude, longitude);
            displayWeather(weatherData, latitude, longitude);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static String getWeatherData(String apiKey, double latitude, double longitude) throws IOException {
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=" + apiKey;
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try (InputStream inputStream = connection.getInputStream();
             Scanner scanner = new Scanner(inputStream)) {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        } finally {
            connection.disconnect();
        }
    }

    private static void displayWeather(String weatherData, double latitude, double longitude) throws JSONException {
    JSONObject json = new JSONObject(weatherData);

    String location = json.getString("name");      // Get location name
    JSONObject weather = json.getJSONArray("weather").getJSONObject(0);
    JSONObject main = json.getJSONObject("main");

    String description = weather.getString("description");
    double temperature = main.getDouble("temp") - 273.15;
    double humidity = main.getDouble("humidity");

    System.out.println("\nLocation: " + location);
    System.out.println("Weather at Latitude " + latitude + " and Longitude " + longitude + ":");
    System.out.println("Description: " + description);
    System.out.println("Temperature: " + String.format("%.1f", temperature) + "°C");
    System.out.println("Humidity: " + humidity + "%");
}
}