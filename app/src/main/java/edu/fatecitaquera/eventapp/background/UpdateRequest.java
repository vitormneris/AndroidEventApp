package edu.fatecitaquera.eventapp.background;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import edu.fatecitaquera.eventapp.util.ConnectionFactory;

public class UpdateRequest extends AsyncTask<String, Void, Boolean> {

    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            URL update = new URL("http://" + ConnectionFactory.serverIP + ":8080/eventos/" + strings[0] + "/atualizar");
            HttpURLConnection connection = (HttpURLConnection) update.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");
            connection.setDoOutput(true);
            PrintStream printStream = new PrintStream(connection.getOutputStream());
            printStream.println(strings[1]);
            connection.connect();

            if (connection.getResponseCode() == 200) return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
