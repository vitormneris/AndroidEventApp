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
        StringBuilder apiResponse = null;

        try {
            URL findAll = new URL("http://" + ConnectionFactory.serverIP + ":8080/eventos/todos");
            HttpURLConnection connection = (HttpURLConnection) findAll.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(false);
            connection.setConnectTimeout(15000);
            connection.connect();

            if (connection.getResponseCode() != 200) return null;

            Scanner scanner = new Scanner(findAll.openStream());
            apiResponse = new StringBuilder();
            while (scanner.hasNext()) apiResponse.append(scanner.nextLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (apiResponse == null) ? null : apiResponse.toString();
    }
}
