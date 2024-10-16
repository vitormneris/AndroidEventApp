package edu.fatecitaquera.eventapp.background;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import edu.fatecitaquera.eventapp.util.ConnectionFactory;

public class UpdateRequest extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... strings) {
        StringBuilder apiResponse = new StringBuilder();
        try {
            URL update = new URL("http://" + ConnectionFactory.serverIP + ":8080/eventos/" + strings[0] + "/atualizar");
            HttpURLConnection connection = (HttpURLConnection) update.openConnection();

            connection.setRequestMethod("PUT");

            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            connection.setDoOutput(true);

            PrintStream printStream = new PrintStream(connection.getOutputStream());
            printStream.println(strings[1]);

            connection.connect();

            String jsonResponse = new Scanner(connection.getInputStream()).next();

            return jsonResponse;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return apiResponse.toString();
    }
}
