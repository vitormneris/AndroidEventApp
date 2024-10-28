package edu.fatecitaquera.eventapp.background;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.fatecitaquera.eventapp.util.ConnectionFactory;

public class UserAddEventRequest extends AsyncTask<String, Void, Boolean> {

    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            URL userAddEvent = new URL("http://" + ConnectionFactory.serverIP + ":8080/eventos/usuarioid/" + strings[0] + "/eventoid/" + strings[1]);
            HttpURLConnection connection = (HttpURLConnection) userAddEvent.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(false);
            connection.setConnectTimeout(15000);
            connection.connect();

            if (connection.getResponseCode() == 200) return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
