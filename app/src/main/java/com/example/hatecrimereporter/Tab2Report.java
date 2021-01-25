package com.example.hatecrimereporter;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.hatecrimereporter.ui.main.PageViewModel;
import com.example.hatecrimereporter.ui.main.PageViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class Tab2Report extends Fragment implements View.OnClickListener{
    private static final String ARG_SECTION_NUMBER = "section_number";
    private PageViewModel pageViewModel;
    final Calendar myCalendar = Calendar.getInstance();
    EditText editDate;
    EditText editTime;
    View view;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.report, container, false);
        this.view = view;
        Button button = (Button) view.findViewById(R.id.submitReport);
        button.setOnClickListener(this);

        EditText editDate= (EditText) view.findViewById(R.id.editTextDate);
        this.editDate = editDate;
        editDate.setOnClickListener(this);

        EditText editTime= (EditText) view.findViewById(R.id.editTextTime);
        this.editTime = editTime;
        editTime.setOnClickListener(this);

        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submitReport:
                EditText body = (EditText) view.findViewById(R.id.describeCrimeInput);
                String tex = body.getText().toString();
                String dateD = editDate.getText().toString();
                String timeT = editTime.getText().toString();

                String format = "";
                format += "Date: " + dateD +"<br><br>";
                format += "Time: " + timeT +"<br><br>";
                format += "Crime description: " +tex +"<br><br>";
                format += "";
                SendMail mailer = new SendMail("futurus.001@gmail.com",
                        "oliviazhou2001@gmail.com","Hate Crime","TextBody",
                         format);
                try {
                    mailer.sendAuthenticated();
                } catch (Exception e) {
                    Log.e("error", "Failed sending email.", e);
                }
                break;

            case R.id.editTextTime:

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        editTime.setText(" "+selectedHour+":"+selectedMinute);
                    }

                };
                TimePickerDialog tDialog = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, time, hour, minute, true);
                tDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                tDialog.show();
                break;
            case R.id.editTextDate:

                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "MM/dd/yy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.CANADA);

                        editDate.setText(" "+sdf.format(myCalendar.getTime()));
                    }

                };
                DatePickerDialog dDialog = new DatePickerDialog(Objects.requireNonNull(getActivity()), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));

                dDialog.show();
                break;
            default:
                return;

        };

    }


}