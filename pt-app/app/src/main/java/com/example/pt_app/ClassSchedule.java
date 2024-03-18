package com.example.pt_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.mysql.cj.ClientPreparedQuery;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import kotlin.text.Charsets;

public class ClassSchedule extends AppCompatActivity implements AsyncResponse {

    Button btnCancel,btnSave;
    Spinner spClasses;
    ArrayList<ClassScheduleClassModel> arrClasses;
    ClassScheduleClassAdapter spClassesAdapter ;
    EditText etvStartDate,etvEndDate;
    CheckBox cbMon,cbTue,cbWed,cbThu,cbFri,cbSat,cbSun;
    EditText etvMonTime,etvTueTime,etvWedTime,etvThuTime,etvFriTime,etvSatTime,etvSunTime;
    int userID,classID,spClassesPos;
    String userType;
    Boolean isClassOnMon=false,isClassOnTue=false,isClassOnWed=false,isClassOnThu=false,isClassOnFri=false,isClassOnSat=false,isClassOnSun=false;
    AsyncResponse asyncResponse;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_schedule);
        asyncResponse = this;
        context = this;

        // TO DO: GET USERID AND ACCOUNT TYPE FROM SHARED PREFERENCES
        userID = 99999;
        userType = "TRAINER";

        spClasses = findViewById(R.id.classSchedClassID);
        cbMon = findViewById(R.id.classSchedMon);
        cbTue = findViewById(R.id.classSchedTue);
        cbWed = findViewById(R.id.classSchedWed);
        cbThu = findViewById(R.id.classSchedThu);
        cbFri = findViewById(R.id.classSchedFri);
        cbSat = findViewById(R.id.classSchedSat);
        cbSun = findViewById(R.id.classSchedSun);
        etvMonTime = findViewById(R.id.classSchedMonTime);
        etvTueTime = findViewById(R.id.classSchedTueTime);
        etvWedTime = findViewById(R.id.classSchedWedTime);
        etvThuTime = findViewById(R.id.classSchedThuTime);
        etvFriTime = findViewById(R.id.classSchedFriTime);
        etvSatTime = findViewById(R.id.classSchedSatTime);
        etvSunTime = findViewById(R.id.classSchedSunTime);

        etvStartDate = findViewById(R.id.classSchedStartDate);
        etvEndDate = findViewById(R.id.classSchedEndDate);

        arrClasses = new ArrayList<ClassScheduleClassModel>();
        spClasses = findViewById(R.id.classSchedClassID);
        spClassesAdapter = new ClassScheduleClassAdapter(this, arrClasses);
        spClasses.setAdapter(spClassesAdapter);

        spClasses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                spClassesPos = spClasses.getSelectedItemPosition();

                classID = Integer.parseInt(((TextView)view.findViewById(R.id.rClassSchedClassID)).getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Get all classes from the database to build the classes spinner
        String data = "classes.php?arg1=gac";

        //Create new database connection
        ServerConnection serverConnection = new ServerConnection();
        //Setup response value
        serverConnection.delegate = asyncResponse;
        //Send data to server
        serverConnection.execute(data,"spinClass");


        etvStartDate.setFocusable(false);
        etvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(ClassSchedule.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date = String.format("%02d", dayOfMonth) + "/" + String.format("%02d", monthOfYear+1) + "/" + String.format("%04d", year);
                        etvStartDate.setText(date);
                    }
                }, yy, mm, dd);
                datePicker.show();
            }
        });

        etvEndDate.setFocusable(false);
        etvEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(ClassSchedule.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date = String.format("%02d", dayOfMonth) + "/" + String.format("%02d", monthOfYear+1) + "/" + String.format("%04d", year);
                        etvEndDate.setText(date);
                    }
                }, yy, mm, dd);
                datePicker.show();
            }
        });

        // setup time picker for each day
        etvMonTime.setFocusable(false);
        etvMonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                // on below line we are initializing
                // our Time Picker Dialog
                TimePickerDialog timePicker = new TimePickerDialog(ClassSchedule.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        String time = String.format("%02d", hour) + ":" + String.format("%02d", minute);
                        etvMonTime.setText(time);

                    }
                }, hour, minute, true);
                timePicker.show();
            }
        });

        etvTueTime.setFocusable(false);
        etvTueTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                // on below line we are initializing
                // our Time Picker Dialog
                TimePickerDialog timePicker = new TimePickerDialog(ClassSchedule.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        String time = String.format("%02d", hour) + ":" + String.format("%02d", minute);
                        etvTueTime.setText(time);

                    }
                }, hour, minute, true);
                timePicker.show();
            }
        });

        etvWedTime.setFocusable(false);
        etvWedTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                // on below line we are initializing
                // our Time Picker Dialog
                TimePickerDialog timePicker = new TimePickerDialog(ClassSchedule.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        String time = String.format("%02d", hour) + ":" + String.format("%02d", minute);
                        etvWedTime.setText(time);

                    }
                }, hour, minute, true);
                timePicker.show();
            }
        });

        etvThuTime.setFocusable(false);
        etvThuTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                // on below line we are initializing
                // our Time Picker Dialog
                TimePickerDialog timePicker = new TimePickerDialog(ClassSchedule.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        String time = String.format("%02d", hour) + ":" + String.format("%02d", minute);
                        etvThuTime.setText(time);

                    }
                }, hour, minute, true);
                timePicker.show();
            }
        });

        etvFriTime.setFocusable(false);
        etvFriTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                // setup Time Picker Dialog
                TimePickerDialog timePicker = new TimePickerDialog(ClassSchedule.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        String time = String.format("%02d", hour) + ":" + String.format("%02d", minute);
                        etvFriTime.setText(time);

                    }
                }, hour, minute, true);
                timePicker.show();
            }
        });

        etvSatTime.setFocusable(false);
        etvSatTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                // setup Time Picker Dialog
                TimePickerDialog timePicker = new TimePickerDialog(ClassSchedule.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        String time = String.format("%02d", hour) + ":" + String.format("%02d", minute);
                        etvSatTime.setText(time);

                    }
                }, hour, minute, true);
                timePicker.show();
            }
        });

        etvSunTime.setFocusable(false);
        etvSunTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                // setup Time Picker Dialog
                TimePickerDialog timePicker = new TimePickerDialog(ClassSchedule.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        String time = String.format("%02d", hour) + ":" + String.format("%02d", minute);
                        etvSunTime.setText(time);

                    }
                }, hour, minute, true);
                timePicker.show();
            }
        });

        btnSave = findViewById(R.id.classSchedSaveBtn);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // first check screen values are valid
                if (!IsValidData()) {
                    return;
                }

                // Work out all class dates and times between the start and end date
                if (cbMon.isChecked()) {isClassOnMon = true;}
                if (cbTue.isChecked()) {isClassOnTue = true;}
                if (cbWed.isChecked()) {isClassOnWed = true;}
                if (cbThu.isChecked()) {isClassOnThu = true;}
                if (cbFri.isChecked()) {isClassOnFri = true;}
                if (cbSat.isChecked()) {isClassOnSat = true;}
                if (cbSun.isChecked()) {isClassOnSun = true;}


//                JSONArray ja = new JSONArray();
//                JSONObject jo = new JSONObject();
                String timeslots="";
                int timeslotCount=0;
                String startDate = etvStartDate.getText().toString();
                String endDate = etvEndDate.getText().toString();
                Date dtStartDate,dtEndDate,curDate;
                Boolean isDateToBeScheduled;
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat sdfDB = new SimpleDateFormat("yyyy-MM-dd");

                if(!(startDate.equals("") || endDate.equals(""))) {
                    try {
                        dtStartDate = sdf.parse(startDate);
                        curDate = dtStartDate;
                        dtEndDate = sdf.parse(endDate);
                        long difference = Math.abs(dtStartDate.getTime() - dtEndDate.getTime());
                        long differenceDates = (difference / (24 * 60 * 60 * 1000));
                        int dayOfWeek;
                        String outDate = "", outTime = "";
                        Calendar c = Calendar.getInstance();

                        for (int i = 0; i < differenceDates+1; i++) {
                            isDateToBeScheduled = false;
                            outTime = null;
                            // get day of thw week no
                            c.setTime(curDate);
                            dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

                            if (dayOfWeek == 1 && isClassOnSun) {
                                isDateToBeScheduled = true;
                                outTime = etvSunTime.getText().toString();
                            } else if (dayOfWeek == 2 && isClassOnMon) {
                                isDateToBeScheduled = true;
                                outTime = etvMonTime.getText().toString();
                            } else if (dayOfWeek == 3 && isClassOnTue) {
                                isDateToBeScheduled = true;
                                outTime = etvTueTime.getText().toString();
                            } else if (dayOfWeek == 4 && isClassOnWed) {
                                isDateToBeScheduled = true;
                                outTime = etvWedTime.getText().toString();
                            } else if (dayOfWeek == 5 && isClassOnThu) {
                                isDateToBeScheduled = true;
                                outTime = etvThuTime.getText().toString();
                            } else if (dayOfWeek == 6 && isClassOnFri) {
                                isDateToBeScheduled = true;
                                outTime = etvFriTime.getText().toString();
                            } else if (dayOfWeek == 7 && isClassOnSat) {
                                isDateToBeScheduled = true;
                                outTime = etvSatTime.getText().toString();
                            }

                            if (isDateToBeScheduled) {
                                outDate = sdfDB.format(curDate) + " " + outTime;

//                                // add to JsonArray containing all the schedule dates and times to be added to the database
//                                jo.put("classDateTime", outDate);
//                                ja.put(jo);
                                // build a semicolon separated list of timeslots
                                if (timeslotCount>0) {
                                    timeslots += ";";
                                }
                                timeslots += outDate;
                                timeslotCount ++;
                            }

                            // Calculate next date
                            c.setTime(curDate);
                            c.add(Calendar.DATE, 1);
                            curDate = sdf.parse(sdf.format(c.getTime()));
                        }
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    // Add all the dates and times to the database
 //                   if(!(ja == null)) {
//                        String encodeParams = null;
//                        encodeParams = "arg1=as&arg2=" + userID + "&arg3=" + classID + "&arg4=" + startDate + "&arg5=" + endDate + "&arg6=" + ja.toString();
                    if (!timeslots.equals("")) {
                        String data = "classes.php?arg1=as&arg2=" + userID + "&arg3=" + classID + "&arg4=" + sdfDB.format(dtStartDate) + "&arg5=" + sdfDB.format(dtEndDate) + "&arg6=" + timeslots;
//                        String data = "classes.php?" + encodeParams;
                        //Create new database connection
                        ServerConnection serverConnection = new ServerConnection();
                        //Setup response value
                        serverConnection.delegate = asyncResponse;
                        //Send data to server
                        serverConnection.execute(data, "INSERTSCHEDULES");
                    }
                }
            }
        });

        btnCancel = findViewById(R.id.classSchedCancelBtn);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Classes.class);
                startActivity(i);
                finish();
            }
        });

    }

    //Get the result of async process
    public void processFinish(String result, String destination) {

        try {
            // CONVERT RESULT STRING TO JSON OBJECT
            JSONObject jo = null;
            jo = new JSONObject(result);

            if (jo.length() > 0) {
                if (jo.getString("status").equals("Error")) {
                    Log.d("Error", jo.getString("msg"));

                    new AlertDialog.Builder(context)
                            .setTitle("Error Retrieving Class Schedule Data")
                            .setMessage(jo.getString("msg"))
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("OK", null)
                            .show();
                } else {
                    JSONArray jaData = null;
                    int bookingsFound = 0;

                    if (destination.equals("INSERTSCHEDULES")) {

                        // display success message and open the classes activity showing the new class schedule
                        new AlertDialog.Builder(context)
                                .setTitle("Create Class Schedule")
                                .setMessage(jo.getString("msg"))
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(context, Classes.class);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .show();
                    } else if(destination.equals("spinClass")) {
                        if (jo.getString("status").equals("OK")) {
                            jaData = jo.getJSONArray("data");
                            bookingsFound = jaData.length();
                        }

                        PopulateClassSpinner(jaData);
                    }
                }
            }
        } catch(Exception e){
            new AlertDialog.Builder(context)
                    .setTitle("Class Schedule - Serious Error")
                    .setMessage(e.getMessage())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("OK", null)
                    .show();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //close activity and return to the Class info activity
        finish();
        Intent i = new Intent(context, Classes.class);
        startActivity(i);
    }

    public boolean IsValidData () {
        // Check screen data is valid and show error message if not
        String errTitle = "", errMsg = "";
        boolean bolValidData = true;
        String startDate = etvStartDate.getText().toString();
        String endDate = etvEndDate.getText().toString();
        Date dStartDate = new Date(),dEndDate = new Date();

        try {
            SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
            if (!startDate.equals("")) {dStartDate = sdfDate.parse(startDate);}
            if (!endDate.equals("")) {dEndDate = sdfDate.parse(endDate);}
        }catch(ParseException pe){
            pe.printStackTrace();
        }

        spClassesPos = spClasses.getSelectedItemPosition();

        // check that a class has been selected
        if (spClassesPos == 0) {
            errMsg = "You have not selected a class";
            spClasses.requestFocus();
            bolValidData = false;
        } else if (startDate.equals("")) {
            errMsg = "You have not selected a start Date";
            etvStartDate.requestFocus();
            bolValidData = false;
        } else if (endDate.equals("")) {
            errMsg = "You have not selected an end Date";
            etvEndDate.requestFocus();
            bolValidData = false;
        } else if (dStartDate.after(dEndDate)) {
            errMsg = "Start date must be before end date";
            etvStartDate.requestFocus();
            bolValidData = false;
        } else if (!(cbMon.isChecked() || cbTue.isChecked() || cbWed.isChecked() || cbThu.isChecked() || cbFri.isChecked() || cbSat.isChecked() || cbSun.isChecked())) {
            errMsg = "At least one day of the week must be selected";
            bolValidData = false;
        } else if (cbMon.isChecked() && etvMonTime.getText().toString().equals("")) {
            errMsg = "You are scheduling a class for Monday/s but haven't set the class time";
            etvMonTime.requestFocus();
            bolValidData = false;
        } else if (cbTue.isChecked() && etvTueTime.getText().toString().equals("")) {
            errMsg = "You are scheduling a class for Tuesday/s but haven't set the class time";
            etvTueTime.requestFocus();
            bolValidData = false;
        } else if (cbWed.isChecked() && etvWedTime.getText().toString().equals("")) {
            errMsg = "You are scheduling a class for Wednesday/s but haven't set the class time";
            etvWedTime.requestFocus();
            bolValidData = false;
        } else if (cbThu.isChecked() && etvThuTime.getText().toString().equals("")) {
            errMsg = "You are scheduling a class for Thursday/s but haven't set the class time";
            etvThuTime.requestFocus();
            bolValidData = false;
        } else if (cbFri.isChecked() && etvFriTime.getText().toString().equals("")) {
            errMsg = "You are scheduling a class for Friday/s but haven't set the class time";
            etvFriTime.requestFocus();
            bolValidData = false;
        } else if (cbSat.isChecked() && etvSatTime.getText().toString().equals("")) {
            errMsg = "You are scheduling a class for Saturday/s but haven't set the class time";
            etvSatTime.requestFocus();
            bolValidData = false;
        } else if (cbSun.isChecked() && etvSunTime.getText().toString().equals("")) {
            errMsg = "You are scheduling a class for Sunday/s but haven't set the class time";
            etvSunTime.requestFocus();
            bolValidData = false;
        }

        if (bolValidData) {
            return true;
        } else {
            new AlertDialog.Builder(context)
                    .setTitle(errTitle)
                    .setMessage(errMsg)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("OK", null)
                    .show();

            return false;
        }
    }

    private void PopulateClassSpinner (JSONArray ja) {
        // Populate Class spinner with db data

        arrClasses.clear();
        // add default All option
        arrClasses.add(new ClassScheduleClassModel(0,"ANY",0,0,""));
        // GET INDIVIDUAL JSON OBJECT FROM JSON ARRAY
        if (!(ja == null)) {
            for (int i = 0; i < ja.length(); i++) {
                try {
                    JSONObject jo = ja.getJSONObject(i);
                    int classID = Integer.parseInt(jo.getString("classID"));
                    String name = jo.getString("name");
                    int duration = Integer.parseInt(jo.getString("duration"));
                    int maxOccupancy = Integer.parseInt(jo.getString("maxOccupancy"));
                    String notes = jo.getString("notes");
                    ClassScheduleClassModel classInfo = new ClassScheduleClassModel(classID, name,duration,maxOccupancy,notes);
                    arrClasses.add(classInfo);
                } catch (JSONException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        spClassesAdapter.notifyDataSetChanged(); // this will make the adapter refresh the data in the spinner
    }

}