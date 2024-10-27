package edu.fatecitaquera.eventapp.background;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.fatecitaquera.eventapp.util.ConnectionFactory;

public class DeleteRequest extends AsyncTask<String, Void, Boolean> {

    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            URL delete = new URL("http://" + ConnectionFactory.serverIP + ":8080/eventos/" + strings[0] + "/deletar");
            HttpURLConnection connection = (HttpURLConnection) delete.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setDoOutput(false);
            connection.setConnectTimeout(15000);
            connection.connect();

            if (connection.getResponseCode() == 204) return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}