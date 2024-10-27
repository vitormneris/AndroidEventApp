package edu.fatecitaquera.eventapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

import edu.fatecitaquera.eventapp.dao.EventDAO;
import edu.fatecitaquera.eventapp.model.Event;

public class EventActivity extends AppCompatActivity {
    private TextInputLayout idEvent, nameEvent, startEvent, finishEvent;
    private Button buttonDelete, buttonUpadte;
    private EventDAO eventDAO;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        idEvent = findViewById(R.id.eventId);
        nameEvent = findViewById(R.id.eventName);
        startEvent = findViewById(R.id.startEvent);
        finishEvent = findViewById(R.id.finishEvent);

        buttonUpadte = findViewById(R.id.buttonUpdate);
        buttonDelete = findViewById(R.id.buttonDelete);

        startEvent.getEditText().setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);

                        TimePickerDialog timePickerDialog = new TimePickerDialog(
                                this,
                                (timeView, selectedHour, selectedMinute) -> {
                                    String selectedDateTime = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear +
                                            " " + selectedHour + ":" + String.format("%02d", selectedMinute);
                                    startEvent.getEditText().setText(selectedDateTime);
                                },
                                hour, minute, true
                        );
                        timePickerDialog.show();
                    },
                    year, month, day);
            datePickerDialog.show();
        });


        finishEvent.getEditText().setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);

                        TimePickerDialog timePickerDialog = new TimePickerDialog(
                                this,
                                (timeView, selectedHour, selectedMinute) -> {
                                    String selectedDateTime = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear +
                                            " " + selectedHour + ":" + String.format("%02d", selectedMinute);
                                    finishEvent.getEditText().setText(selectedDateTime);
                                },
                                hour, minute, true
                        );
                        timePickerDialog.show();
                    },
                    year, month, day);
            datePickerDialog.show();
        });

        eventDAO = new EventDAO();

        String eventId = getIntent().getStringExtra("eventId");
        Event event = eventDAO.findById(eventId);

        if (event == null) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        idEvent.getEditText().setText(event.getId());
        nameEvent.getEditText().setText(event.getName());
        startEvent.getEditText().setText(event.getStartEvent());
        finishEvent.getEditText().setText(event.getFinishEvent());

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean isNameFilled = nameEvent.getEditText().getText().toString().trim().isEmpty();
                boolean isStartFilled = startEvent.getEditText().getText().toString().trim().isEmpty();
                boolean isFinishFilled = finishEvent.getEditText().getText().toString().trim().isEmpty();

                buttonUpadte.setEnabled(!isNameFilled && !isStartFilled && !isFinishFilled);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };

        nameEvent.getEditText().addTextChangedListener(textWatcher);
        startEvent.getEditText().addTextChangedListener(textWatcher);
        finishEvent.getEditText().addTextChangedListener(textWatcher);

        buttonDelete.setOnClickListener((e) -> {
            count++;
            if (count < 2) {
                Toast.makeText(getApplicationContext(), "Clique mais uma vez para deletar o evento", Toast.LENGTH_LONG).show();
            } else {

                if (eventDAO.deleteById(eventId))
                    Toast.makeText(getApplicationContext(), "Evento deletado com sucesso!", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(), "Não a conexão com a internet!", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        buttonUpadte.setOnClickListener((e) -> {
            Event eventUpdated = new Event();
            eventUpdated.setName(nameEvent.getEditText().getText().toString());
            eventUpdated.setStartEvent(startEvent.getEditText().getText().toString());
            eventUpdated.setFinishEvent(finishEvent.getEditText().getText().toString());

            if (eventDAO.update(eventId, eventUpdated))
                Toast.makeText(getApplicationContext(), "Evento atualizado com sucesso!", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(), "Não a conexão com a internet!", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

    }
}