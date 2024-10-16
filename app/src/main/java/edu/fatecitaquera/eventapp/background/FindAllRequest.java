package edu.fatecitaquera.eventapp.background;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import edu.fatecitaquera.eventapp.util.ConnectionFactory;


public class FindAllRequest extends AsyncTask<Void, Void, String> {

    @Override
    protected String doInBackground(Void... voids) {

        StringBuilder apiResponse = new StringBuilder();
        try {
            URL findAll = new URL("http://" + ConnectionFactory.serverIP + ":8080/eventos/todos");
            HttpURLConnection connection = (HttpURLConnection) findAll.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-type", "application/json");
            connection.setDoOutput(true);
            connection.setConnectTimeout(15000);
            connection.connect();

            Scanner scanner = new Scanner(findAll.openStream());
            while (scanner.hasNext()) {
                apiResponse.append(scanner.next());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return apiResponse.toString();
    }
}
