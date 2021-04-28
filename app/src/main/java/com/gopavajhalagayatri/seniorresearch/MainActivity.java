package com.gopavajhalagayatri.seniorresearch;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Paint;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.Settings;
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
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;

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
    String[] times = {"Estimated Time to Complete", "5 min", "15 min", "30 min", "1 hour",
            "1 hour 30 min", "2+ hours"};
    int[] timesConvert = {0, 5, 15, 30, 60, 90, 180};
    DatabaseHelper db = new DatabaseHelper(this);
    ArrayList<Task> tasks = new ArrayList<Task>();
    View builderView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //Permissions
        AppOpsManager appOps = (AppOpsManager)getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());
        if(mode != AppOpsManager.MODE_ALLOWED){
            startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),1);
        }

        //activity manager
        final PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            //Log.i(TAG, packageInfo.packageName);
        }
        ActivityManager actvityManager = (ActivityManager)
                this.getSystemService( ACTIVITY_SERVICE );
        List<ActivityManager.RunningAppProcessInfo> procInfos = actvityManager.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo proc : procInfos){
            Log.i(TAG, proc.processName);
        }

        //Actual layout
        simpleList = findViewById(R.id.list_view);
        //Database setup
        tasks = db.getAllTasks();
        for(int i = 0; i < tasks.size(); i++){
            Log.i(TAG, tasks.get(i).toString());
            taskList.add(tasks.get(i).name);
        }
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
                if(tasks.get(position).state){
                    tv.setPaintFlags(tv.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                    tasks.get(position).state = false;
                }else{
                    tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    tasks.get(position).state = true;
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
                builder.setTitle("New Task");
                final Spinner spin = (Spinner)builderView.findViewById(R.id.task_time);
                ArrayAdapter<String> timesAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, times);
                timesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin.setAdapter(timesAdapter);
                spin.setSelection(0);
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
                        String[] temp = date.getText().toString().split("-");
                        Task t = new Task(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]),
                                Integer.parseInt(temp[2]), m_Text, timesConvert[spin.getSelectedItemPosition()], false);
                        arrayAdapter.notifyDataSetChanged();
                        tasks.add(t);
                        db.addTask(t);
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