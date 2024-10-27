package edu.fatecitaquera.eventapp;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
import edu.fatecitaquera.eventapp.model.User;

public class UserListActivity extends AppCompatActivity {
    private LinearLayout userListView;
    private EventDAO eventDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userListView = findViewById(R.id.userListView);

        eventDAO = new EventDAO();
        List<Event> events = eventDAO.findAll();
        LayoutInflater inflater = LayoutInflater.from(this);

        if (events == null) {
            View playerView = inflater.inflate(R.layout.message_error, userListView, false);
            TextView textView = playerView.findViewById(R.id.message);

            textView.setText("Não há conexão com a internet.");

            userListView.addView(playerView);
        } else {

            String eventId = getIntent().getStringExtra("eventId");
            Event event = events.stream().filter((e) -> e.getId().equals(eventId)).findFirst().get();

            if (event.getUsers().isEmpty()) {
                View playerView = inflater.inflate(R.layout.message_error, userListView, false);
                TextView textView = playerView.findViewById(R.id.message);
                ImageView imageView = playerView.findViewById(R.id.image_error);

                textView.setText("Nenhum usuário registrou-se neste evento ainda.");
                textView.setTextColor(R.color.orange);
                imageView.setImageResource(R.drawable.cara_feliz);

                userListView.addView(playerView);
            } else {
                for (User user : event.getUsers()) {
                    View playerView = inflater.inflate(R.layout.user_item, userListView, false);

                    TextView id = playerView.findViewById(R.id.user_id);
                    TextView name = playerView.findViewById(R.id.user_name);
                    TextView userIn = playerView.findViewById(R.id.user_in);
                    TextView userOut = playerView.findViewById(R.id.user_out);

                    id.setText("ID: " + user.getId());
                    name.setText("Nome: " + user.getName());
                    userIn.setText("Entrou: " + user.getUserIn());
                    userOut.setText("Saiu: " + user.getUserOut());

                    userListView.addView(playerView);
                }
            }
        }
    }
}