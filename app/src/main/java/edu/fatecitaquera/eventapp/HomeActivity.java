package edu.fatecitaquera.eventapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import edu.fatecitaquera.eventapp.dao.EventDAO;
import edu.fatecitaquera.eventapp.model.Event;

public class HomeActivity extends AppCompatActivity {
    private Button button;
    private EventDAO eventDAO;
    private LinearLayout eventListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        button = findViewById(R.id.buttonAdd);
        button.setOnClickListener((event) -> startActivity(new Intent(this, AddEventActivity.class)));
        eventListView = findViewById(R.id.eventListView);

        eventDAO = new EventDAO();
        List<Event> eventList = eventDAO.findAll();

        LayoutInflater inflater = LayoutInflater.from(this);

        if (eventList == null) {
            View playerView = inflater.inflate(R.layout.message_error, eventListView, false);
            TextView textView = playerView.findViewById(R.id.message);

            textView.setText("Não há conexão com a internet.");

            eventListView.addView(playerView);
        } else if (eventList.isEmpty()) {
            View playerView = inflater.inflate(R.layout.message_error, eventListView, false);
            TextView textView = playerView.findViewById(R.id.message);
            ImageView imageView = playerView.findViewById(R.id.image_error);

            textView.setText("Não há eventos registrados, registre-os logo abaixo.");
            textView.setTextColor(R.color.orange);
            imageView.setImageResource(R.drawable.cara_feliz);

            eventListView.addView(playerView);
        } else {
            for (Event event : eventList) {
                View playerView = inflater.inflate(R.layout.event_item, eventListView, false);

                TextView id = playerView.findViewById(R.id.user_id);
                TextView name = playerView.findViewById(R.id.event_name);
                TextView startEvent = playerView.findViewById(R.id.start_event);
                TextView finishEvent = playerView.findViewById(R.id.finish_event);
                Button buttonEdit = playerView.findViewById(R.id.buttonEdit);
                Button buttonQRCode = playerView.findViewById(R.id.buttonQRCODE);
                Button buttonUserList = playerView.findViewById(R.id.buttonList);

                buttonEdit.setOnClickListener((e) -> {
                    Intent intent = new Intent(this, EventActivity.class);
                    intent.putExtra("eventId", event.getId());
                    startActivity(intent);
                });

                buttonQRCode.setOnClickListener((e) -> {
                    Intent intent = new Intent(this, ScannerActivity.class);
                    intent.putExtra("eventId", event.getId());
                    startActivity(intent);
                });

                buttonUserList.setOnClickListener((e) -> {
                    Intent intent = new Intent(this, UserListActivity.class);
                    intent.putExtra("eventId", event.getId());
                    startActivity(intent);
                });

                id.setText("ID: " + event.getId());
                name.setText("NOME: " + event.getName());
                startEvent.setText("INICIO: " + event.getStartEvent());
                finishEvent.setText("TERMINO: " + event.getFinishEvent());

                eventListView.addView(playerView);
            }
        }
    }
}