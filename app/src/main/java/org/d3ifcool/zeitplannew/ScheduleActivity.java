package org.d3ifcool.zeitplannew;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ScheduleActivity extends AppCompatActivity {

    TextView tvScheduleHari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        tvScheduleHari = findViewById(R.id.tv_schedule_hari);

        String hari = getIntent().getStringExtra("hari");

        tvScheduleHari.setText(hari);
    }
}
