package com.example.hatecrimereporter.ui.main;
//
//import android.app.DatePickerDialog;
//import android.view.View;
//import android.widget.DatePicker;
//import android.widget.EditText;
//
//import com.example.HateCrimeReporter.R;
//
//import java.util.Calendar;
//
//public class PickDatetime implements View.OnClickListener {
//    final Calendar myCalendar = Calendar.getInstance();
//
//    EditText edittext= (EditText) findViewById(R.id.Birthday);
//    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
//
//        @Override
//        public void onDateSet(DatePicker view, int year, int monthOfYear,
//                              int dayOfMonth) {
//            // TODO Auto-generated method stub
//            myCalendar.set(Calendar.YEAR, year);
//            myCalendar.set(Calendar.MONTH, monthOfYear);
//            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
////            updateLabel();
//        }
//
//    };
//
//
//
//    @Override
//    public void onClick(View view) {
//        new DatePickerDialog(classname.this, date, myCalendar
//                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//
//    };
//}
