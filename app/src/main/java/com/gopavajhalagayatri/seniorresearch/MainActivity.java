package com.gopavajhalagayatri.seniorresearch;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String m_Text = "";
    private static final String TAG = "DEBUG MESSAGES";
    ListView simpleList;
    ArrayList<String> taskList = new ArrayList<String>();
    ArrayList<Boolean> state = new ArrayList<Boolean>();
    ArrayList<String> timeRequired = new ArrayList<String>();
    String[] times = {"5 min", "15 min", "30 min", "1 hour", "1 hour 30 min", "2+ hours"};
    ArrayList<DateSelected> dueDates = new ArrayList<DateSelected>();
    View builderView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        simpleList = findViewById(R.id.list_view);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.tasks_list, R.id.taskItem, taskList);
        simpleList.setAdapter(arrayAdapter);
        setSupportActionBar(toolbar);

        simpleList.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                ViewGroup temp = (ViewGroup)view;
                TextView tv = (TextView) temp.getChildAt(0);
                if(state.get(position)){
                    tv.setPaintFlags(tv.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                    state.set(position, false);
                }else{
                    tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    state.set(position, true);
                }
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Title");
                LayoutInflater inflater = getLayoutInflater();
                final View dialoglayout = inflater.inflate(R.layout.add_task, null);
                builder.setView(builderView = dialoglayout);
                /*final Spinner spin = (Spinner)builderView.findViewById(R.id.task_time);
                ArrayAdapter<String> timesAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, times);
                timesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                */
                final Button date = (Button)builderView.findViewById(R.id.task_due_date);
                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar c = Calendar.getInstance();
                        DatePickerDialog dpd = new DatePickerDialog(MainActivity.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                        DateSelected temp = new DateSelected(dayOfMonth, monthOfYear, year);
                                        dueDates.add(temp);
                                    }
                                }, c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DATE));
                        dpd.show();
                    }
                });
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText a = (EditText)builderView.findViewById(R.id.task_name);
                        m_Text = a.getText().toString();
                        taskList.add(m_Text);
                        state.add(false);
                        //timeRequired.add(spin.getSelectedItem().toString());
                        arrayAdapter.notifyDataSetChanged();
                        int temp = taskList.size() - 1;
                        Log.i(TAG, taskList.get(temp) + " " + dueDates.get(temp));
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


class DateSelected {
    int day;
    int month;
    int year;

    public DateSelected(int a, int b, int c){
        day = a;
        month = b;
        year = c;
    }

    @Override
    public String toString() {
        return "DateSelected{" +
                "day=" + day +
                ", month=" + month +
                ", year=" + year +
                '}';
    }
}