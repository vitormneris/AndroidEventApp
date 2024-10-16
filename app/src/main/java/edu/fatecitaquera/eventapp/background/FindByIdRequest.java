package edu.fatecitaquera.eventapp.background;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import edu.fatecitaquera.eventapp.util.ConnectionFactory;

public class FindByIdRequest extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... strings) {

        StringBuilder apiResponse = new StringBuilder();
        try {
            URL findById = new URL("http://" + ConnectionFactory.serverIP + ":8080/eventos/" + strings[0] + "/encontrar");
            HttpURLConnection connection = (HttpURLConnection) findById.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-type", "application/json");
            connection.setDoOutput(true);
            connection.setConnectTimeout(15000);
            connection.connect();

            Scanner scanner = new Scanner(findById.openStream());
            while (scanner.hasNext()) {
                apiResponse.append(scanner.next());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return apiResponse.toString();
    }
}
