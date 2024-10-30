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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.fatecitaquera.eventapp.dao.EventDAO;
import edu.fatecitaquera.eventapp.model.Event;

public class AddEventActivity extends AppCompatActivity {
    private TextInputLayout nameEvent, startEvent, finishEvent;
    private Button button;
    private EventDAO eventDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_event);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nameEvent = findViewById(R.id.eventName);
        startEvent = findViewById(R.id.startEvent);
        finishEvent = findViewById(R.id.finishEvent);

        button = findViewById(R.id.buttonSave);

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

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean isNameFilled = nameEvent.getEditText().getText().toString().trim().isEmpty();
                boolean isStartFilled = startEvent.getEditText().getText().toString().trim().isEmpty();
                boolean isFinishFilled = finishEvent.getEditText().getText().toString().trim().isEmpty();

                button.setEnabled(!isNameFilled && !isStartFilled && !isFinishFilled);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };

        nameEvent.getEditText().addTextChangedListener(textWatcher);
        startEvent.getEditText().addTextChangedListener(textWatcher);
        finishEvent.getEditText().addTextChangedListener(textWatcher);

        eventDAO = new EventDAO();
        button.setOnClickListener((event) -> {

            Event eventNew = new Event();
            eventNew.setName(nameEvent.getEditText().getText().toString());
            eventNew.setStartEvent(startEvent.getEditText().getText().toString());
            eventNew.setFinishEvent(finishEvent.getEditText().getText().toString());

            eventNew.getStartEvent();
            eventNew.getFinishEvent();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            try {
                Date start = simpleDateFormat.parse(eventNew.getStartEvent());
                Date finish = simpleDateFormat.parse(eventNew.getFinishEvent());

                if (start.before(finish)) {
                    if (eventDAO.insert(eventNew)) Toast.makeText(getApplicationContext(), "Evento registrado com sucesso!", Toast.LENGTH_LONG).show();
                    else Toast.makeText(getApplicationContext(), "Não há conexão com a internet!", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "A data de início não pode ser posterior a data de término!", Toast.LENGTH_LONG).show();
                }

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }


        });
    }
}