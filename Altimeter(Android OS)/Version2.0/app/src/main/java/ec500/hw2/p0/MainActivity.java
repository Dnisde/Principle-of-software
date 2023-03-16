package ec500.hw2.p0;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ViewFlipper;
import androidx.appcompat.app.AppCompatActivity;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import ec500.hw2.p0.database.GPSDatabase;
import ec500.hw2.p0.model.Loc;

public class MainActivity extends AppCompatActivity {

    private ViewFlipper flipper;
    private LocationManager locationManager;

//    private String strLocation, strSpeed;
    private Button btnChangeSize, btnHelp, btnPause, btnTest, btnReset, btn_previous, btn_next;
    private static Spinner DistanceUnit;
    private static Spinner TimeUnit;
    private static Spinner SpeedUnit;
    private EditText edtFontSize;
    private boolean isTest = false;
    private boolean isReset = false;
    private boolean isMeterPerSecond = true;
    private double valCurrentSpeed = 0.0;
    private double Sim_valCurrentSpeed = 0.0;
    private double valCurrentTime = 0.0;
    private double Sim_valCurrentTime = 0.0;
    private double valCurrentDistance = 0.0;
    private double Sim_valCurrentDistance = 0.0;
    private boolean isPause = false;
    private double valLatitude = 0.0;
    private double preLatitude = 0.0;
    private double valLongitude = 0.0;
    private double preLongitude = 0.0;
    private double valCurrentMaxSpeed = 0.0;
    private double valCurrentMinSpeed = Double.MAX_VALUE;
    private static int Unit_distance, Unit_Time, Unit_Speed;
    private int count_Speed_OnItemSelectedListener = 0;
    private int count_Distance_OnItemSelectedListener = 0;
    private int count_Time_OnItemSelectedListener = 0;
    private long startTime = System.nanoTime();
    private long runningTime = startTime;
    private long curTime = 0;
    private long preTime = startTime;

    // --- Use for High Score View only:
    private double valVarianceSpeed = 0.0;
    private double valGlobalMinSpeed = Double.MAX_VALUE;
    private double valGlobalMaxSpeed = 0.0;
    private double valVarianceAltitude = 0.0;
    private double valMaxAltitude = 0.0;
    private double valMinAltitude = Double.MAX_VALUE;

    // Textview in Main view.
    private TextView txtLongitudeValue, txtLongitude, txtLongitudeChange;
    private TextView txtLatitudeValue, txtLatitude, txtLatitudeChange;
    private TextView txtAltitudeValue, txtAltitude, txtAltitudeChange;
    private TextView txtDistanceValue, txtDistance, txtDistanceUnit;
    private TextView txtTimeValue, txtTimeShow, txtTimeUnit, txtRunningTime;
    private TextView txtSpeedValue, txtSpeedShow, txtSpeedUnit, txtSpeedChangeValue, txtSpeedChangeUnit;

    // Textview in High_Score view.
    private TextView titleHC_speedData, titleHC_Max, titleHC_Min, titleHC_Altitude, titleHC_MaxAltitude, titleHC_MinAltitude;
    private TextView txtHC_speedData, txtHC_variationSpeed, txtHC_maxSpeed, txtHC_minSpeed;
    private TextView txtHC_Altitude, txtHC_variationAltitude, txtHC_maxAltitude, txtHC_minAltitude;

    // For value shown in Textview() in Main Layout
    private String strLongitude, strLatitude, strAltitude, strLongitudeChange, strLatitudeChange, strAltitudeChange;
    private Double strLongitudeTemp, strLatitudeTemp, strAltitudeTemp, strSpeedTemp;
    private String strTimeValue, strTimeUnit;
    private String strRunningTime;
    private String strDistanceValue, strDistanceUnit;
    private String strSpeedValue, strSpeedUnit, strSpeedChangeValue, strSpeedChangeUnit;

    // For value shown in Textview() in High Score Layout:
    private String strVariantSpeed, strGlobalSpeedMax, strGlobalSpeedMin;
    private String strVariantAltitude, strMaxAltitude, strMinAltitude;

    // Define the Significant Filter:
    private static final int FRACTION_CONSTRAINT = 3;
    private static final int GPS_CONSTRAINT = 4;

    // A database to store all the temporary variables that used to record the Value of matrices. (Could be able to "RESET")
    private static GPSDatabase database;
    // A database to store all the global variables that used to record the Highest Value of matrices.
    private static GPSDatabase globalDatabase;


    public void Initialize() {
        // Set Value for the TextView in Main:
        // Longitude
        txtLongitude = (TextView) findViewById(R.id.txtLongitude);
        txtLongitudeValue = (TextView) findViewById(R.id.txtLongitudeValue);

        // Latitude
        txtLatitude = (TextView) findViewById(R.id.txtLatitude);
        txtLatitudeValue = (TextView) findViewById(R.id.txtLatitudeValue);

        // Altitude
        txtAltitude = (TextView) findViewById(R.id.txtAltitude);
        txtAltitudeValue = (TextView) findViewById(R.id.txtAltitudeValue);

        // Distance
        txtDistance = (TextView) findViewById(R.id.txtDistance);
        txtDistanceValue = (TextView) findViewById(R.id.txtDistanceValue);
        txtDistanceUnit = (TextView) findViewById(R.id.txtDistanceUnitShow);

        // Time
        txtTimeShow = (TextView) findViewById(R.id.txtTime);
        txtTimeValue = (TextView) findViewById(R.id.txtTimeValue);
        txtTimeUnit = (TextView) findViewById(R.id.txtTimeUnitShow);

        // Program RunningTime:
        txtRunningTime = (TextView) findViewById(R.id.txtRunningTime);

        // Speed
        txtSpeedShow = (TextView) findViewById(R.id.txtSpeedShow);
        txtSpeedValue = (TextView) findViewById(R.id.txtSpeedValue);
        txtSpeedUnit = (TextView) findViewById(R.id.txtSpeedUnitShow);

        /**
         * --------  High_Score View Here: ---------------
         */

//        // Title:
//        titleHC_speedData = (TextView) findViewById(R.id.titleHC_speedData);
//        titleHC_Max = (TextView) findViewById(R.id.titleHC_Max);
//        titleHC_Min = (TextView) findViewById(R.id.titleHC_Min);
//        titleHC_Altitude = (TextView) findViewById(R.id.titleHC_Altitude);
//        titleHC_MaxAltitude = (TextView) findViewById(R.id.titleHC_MaxAltitude);
//        titleHC_MinAltitude = (TextView) findViewById(R.id.titleHC_MinAltitude);
//
//        // Value:
//        txtHC_speedData = (TextView) findViewById(R.id.txtHC_speedData);
//        txtHC_variationSpeed = (TextView) findViewById(R.id.txtHC_variationSpeed);
//        txtHC_maxSpeed = (TextView) findViewById(R.id.txtHC_maxSpeed);
//        txtHC_minSpeed = (TextView) findViewById(R.id.txtHC_minSpeed);
//        txtHC_Altitude = (TextView) findViewById(R.id.txtHC_Altitude);
//        txtHC_variationAltitude = (TextView) findViewById(R.id.txtHC_variationAltitude);
//        txtHC_maxAltitude = (TextView) findViewById(R.id.txtHC_maxAltitude);
//        txtHC_minAltitude = (TextView) findViewById(R.id.txtHC_minAltitude);

        txtSpeedValue.setText("N/A");
        txtSpeedUnit.setText("N/A");
        txtLongitudeValue.setText("N/A");
        txtLatitudeValue.setText("N/A");
        txtAltitudeValue.setText("N/A");
        txtDistanceValue.setText("N/A");
        txtDistanceUnit.setText("N/A");
        txtTimeValue.setText("N/A");
        txtTimeUnit.setText("N/A");
        txtRunningTime.setText("N/A");

        // Set Value for the TextView in High Score:
        txtHC_speedData.setText("N/A");
        txtHC_speedData.setTextColor(Color.DKGRAY);
        txtHC_variationSpeed.setText("N/A");
        txtHC_variationSpeed.setTextColor(Color.BLUE);
        txtHC_maxSpeed.setText("N/A");
        txtHC_maxSpeed.setTextColor(Color.RED);
        txtHC_minSpeed.setText("N/A");
        txtHC_minSpeed.setTextColor(Color.GREEN);
        txtHC_Altitude.setText("N/A");
        txtHC_Altitude.setTextColor(Color.BLUE);
        txtHC_variationAltitude.setText("N/A");
        txtHC_variationAltitude.setTextColor(Color.BLUE);
        txtHC_maxAltitude.setText("N/A");
        txtHC_maxAltitude.setTextColor(Color.RED);
        txtHC_minAltitude.setText("N/A");
        txtHC_minAltitude.setTextColor(Color.GREEN);

        for (Loc locItem : database.locDao().getAll()) {
            database.locDao().delete(locItem);
        }
        for (Loc locItem : globalDatabase.locDao().getAll()) {
            database.locDao().delete(locItem);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flipper = findViewById(R.id.view_flipper);
        btn_previous = findViewById(R.id.prev_button);
        btn_next = findViewById(R.id.next_button);

        btn_nextClick();
        btn_previousClick();

        database = Room.databaseBuilder(this, GPSDatabase.class, "GPS_db").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        globalDatabase = Room.databaseBuilder(this, GPSDatabase.class, "GPS_global_db").allowMainThreadQueries().build();

        // Longitude
        txtLongitude = (TextView) findViewById(R.id.txtLongitude);
        txtLongitudeValue = (TextView) findViewById(R.id.txtLongitudeValue);
        txtLongitudeChange = (TextView) findViewById(R.id.txtLongitudeValueChange);

        // Latitude
        txtLatitude = (TextView) findViewById(R.id.txtLatitude);
        txtLatitudeValue = (TextView) findViewById(R.id.txtLatitudeValue);
        txtLatitudeChange = (TextView) findViewById(R.id.txtLatitudeValueChange);

        // Altitude
        txtAltitude = (TextView) findViewById(R.id.txtAltitude);
        txtAltitudeValue = (TextView) findViewById(R.id.txtAltitudeValue);
        txtAltitudeChange = (TextView) findViewById(R.id.txtAltitudeValueChange);

        // Distance
        txtDistance = (TextView) findViewById(R.id.txtDistance);
        txtDistanceValue = (TextView) findViewById(R.id.txtDistanceValue);
        txtDistanceUnit = (TextView) findViewById(R.id.txtDistanceUnitShow);

        // Time
        txtTimeShow = (TextView) findViewById(R.id.txtTime);
        txtTimeValue = (TextView) findViewById(R.id.txtTimeValue);
        txtTimeUnit = (TextView) findViewById(R.id.txtTimeUnitShow);

        // Program RunningTime:
        txtRunningTime = (TextView) findViewById(R.id.txtRunningTime);

        // Speed
        txtSpeedShow = (TextView) findViewById(R.id.txtSpeedShow);
        txtSpeedValue = (TextView) findViewById(R.id.txtSpeedValue);
        txtSpeedUnit = (TextView) findViewById(R.id.txtSpeedUnitShow);
        txtSpeedChangeValue = (TextView) findViewById(R.id.txtSpeedValueChange);
        txtSpeedChangeUnit = (TextView) findViewById(R.id.txtSpeedChangeUnit);

        /**
         * --------  High_Score View Here: ---------------
         */

        // Title:
        titleHC_speedData = findViewById(R.id.titleHC_speedData);
        titleHC_Max = findViewById(R.id.titleHC_Max);
        titleHC_Min = findViewById(R.id.titleHC_Min);
        titleHC_Altitude = findViewById(R.id.titleHC_Altitude);
        titleHC_MaxAltitude = findViewById(R.id.titleHC_MaxAltitude);
        titleHC_MinAltitude = findViewById(R.id.titleHC_MinAltitude);

        // Value:
        txtHC_speedData = findViewById(R.id.txtHC_speedData);
        txtHC_variationSpeed = findViewById(R.id.txtHC_variationSpeed);
        txtHC_maxSpeed = findViewById(R.id.txtHC_maxSpeed);
        txtHC_minSpeed = findViewById(R.id.txtHC_minSpeed);
        txtHC_Altitude = findViewById(R.id.txtHC_Altitude);
        txtHC_variationAltitude =  findViewById(R.id.txtHC_variationAltitude);
        txtHC_maxAltitude = findViewById(R.id.txtHC_maxAltitude);
        txtHC_minAltitude = findViewById(R.id.txtHC_minAltitude);


        // Location Object.
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationUpdate();

        // Change font size
        btnChangeSize = (Button) findViewById(R.id.btnChangeSize);
        edtFontSize = (EditText) findViewById(R.id.edtFontSize);
        btnChangeSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(edtFontSize.getText().toString().matches("[0-9]+")) {

                    // Set Font Size for Main view:
                    setFontSize(txtLongitude, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtLongitudeValue, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtLongitudeChange,Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtLatitude, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtLatitudeValue, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtLatitudeChange,Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtAltitude, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtAltitudeValue, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtAltitudeChange,Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtDistance, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtDistanceValue, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtDistanceUnit, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtTimeShow, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtTimeValue, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtTimeUnit, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtSpeedShow, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtSpeedValue, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtSpeedUnit, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtSpeedChangeValue,Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtSpeedChangeUnit,Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtRunningTime, Float.parseFloat(edtFontSize.getText().toString()));

                    // Set Font size for High_Score View:
                    // Title:
                    setFontSize(titleHC_speedData, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(titleHC_Max, Float.parseFloat(edtFontSize.getText().toString()) - 2);
                    setFontSize(titleHC_Min, Float.parseFloat(edtFontSize.getText().toString()) - 2);
                    setFontSize(titleHC_Altitude, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(titleHC_MaxAltitude, Float.parseFloat(edtFontSize.getText().toString()) - 2);
                    setFontSize(titleHC_MinAltitude, Float.parseFloat(edtFontSize.getText().toString()) - 2);

                    // Corresponding Value:
                    setFontSize(txtHC_speedData, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtHC_variationSpeed, Float.parseFloat(edtFontSize.getText().toString()) - 3);
                    setFontSize(txtHC_maxSpeed, Float.parseFloat(edtFontSize.getText().toString()) - 3);
                    setFontSize(txtHC_minSpeed, Float.parseFloat(edtFontSize.getText().toString()) - 3);
                    setFontSize(txtHC_Altitude, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtHC_variationAltitude, Float.parseFloat(edtFontSize.getText().toString()) - 3);
                    setFontSize(txtHC_maxAltitude, Float.parseFloat(edtFontSize.getText().toString()) - 3);
                    setFontSize(txtHC_minAltitude, Float.parseFloat(edtFontSize.getText().toString()) - 3);
                }
            }
        });

        // Instruction page of the APP.
        helpful_click();

        // Test app under synthetic source
        btnTest = (Button) findViewById(R.id.btnTest);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                isTest = !isTest;
            }
        });

        // Reset accumulated distance and time
        btnReset = (Button) findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                isReset = true;
            }
        });

        // pause display
        btnPause = (Button) findViewById(R.id.btnPause);
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPause){
                    onPause();
                    Toast.makeText(MainActivity.this, "Pause the location updates", Toast.LENGTH_SHORT).show();
                    isPause = true;
                }
                else{
                    preTime = System.nanoTime();
                    onResume();
                    Toast.makeText(MainActivity.this, "Resume the location updates", Toast.LENGTH_SHORT).show();
                    isPause = false;
                }
            }
        });

        DistanceSpinner();

        TimeSpinner();

        SpeedSpinner();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Initialize();
        makeToast("Welcome to My Altimeter! ");
    }

    @Override
    public void onResume() {
        super.onResume();
        locationUpdate();
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(mLocationListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        makeToast("My Altimeter is still recording... Resume in any time ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Initialize();
        locationManager.removeUpdates(mLocationListener);
        makeToast("Thank you, See you next time! ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        makeToast("Welcome back ^_^");
    }

    public void btn_nextClick(){
        btn_next.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        // Set the in and out animation of View flipper.
                        flipper.setInAnimation(MainActivity.this,
                                R.anim.slide_right);
                        flipper.setOutAnimation(MainActivity.this,
                                R.anim.slide_left);

                        // It shows next view..
                        flipper.showNext();
                    }
                });
    }

    public void btn_previousClick() {
        btn_previous.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        // Set the in and out animation of View flipper.
                        flipper.setInAnimation(MainActivity.this,
                                android.R.anim.slide_in_left);
                        flipper.setOutAnimation(MainActivity.this,
                                android.R.anim.slide_out_right);

                        // It shows previous view.
                        flipper.showPrevious();
                    }
                });
    }


    /**
     * ---------- All Spinner Views in the Layout - activity_main -----------
     */

    public void DistanceSpinner(){
        // change Distance unit
        DistanceUnit = (Spinner)findViewById(R.id.optionDistanceUnit);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter_Distance = ArrayAdapter.createFromResource(this,
                R.array.distance_unit, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_Distance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        DistanceUnit.setAdapter(adapter_Distance);
        final String[] Distance = new String[1];
        DistanceUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (count_Distance_OnItemSelectedListener == 1) {
                    Distance[0] = parent.getItemAtPosition(position).toString();
                    Unit_distance = position;
                }
                else{
                    count_Distance_OnItemSelectedListener += 1;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void TimeSpinner(){
        // change Time unit
        TimeUnit = (Spinner)findViewById(R.id.optionTimeUnit);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter_Time = ArrayAdapter.createFromResource(this,
                R.array.time_unit, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_Time.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        TimeUnit.setAdapter(adapter_Time);
        final String[] Time = new String[1];
        TimeUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (count_Time_OnItemSelectedListener == 1) {
                    Time[0] = parent.getItemAtPosition(position).toString();
                    Unit_Time = position;
                }
                else{
                    count_Time_OnItemSelectedListener += 1;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void SpeedSpinner(){
        // change Speed unit
        SpeedUnit = (Spinner)findViewById(R.id.optionSpeedUnit);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter_Speed = ArrayAdapter.createFromResource(this,
                R.array.speed_unit, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_Speed.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        SpeedUnit.setAdapter(adapter_Speed);
        final String[] Speed = new String[1];
        SpeedUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (count_Speed_OnItemSelectedListener == 1) {
                    Speed[0] = parent.getItemAtPosition(position).toString();
                    Unit_Speed = position;
                }
                else{
                    count_Speed_OnItemSelectedListener += 1;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    // Handle messages, TODO: Add more code explaination at here:
    private Handler handler = new Handler(new Handler.Callback(){
        @Override
        public boolean handleMessage(Message msg) {
            if ( msg.what == 0x001 ) {
                if (valCurrentSpeed < 10.0) {
                    txtSpeedValue.setTextColor(Color.BLACK);
                }
                else if (valCurrentSpeed < 20.0){
                    txtSpeedValue.setTextColor(Color.GREEN);
                }
                else if (valCurrentSpeed < 30.0){
                    txtSpeedValue.setTextColor(Color.BLUE);
                }
                else if (valCurrentSpeed < 50.0){
                    txtSpeedValue.setTextColor(Color.CYAN);
                }
                else{
                    txtSpeedValue.setTextColor(Color.RED);
                }

                // Set Value for the TextView in Main:
                txtSpeedValue.setText(strSpeedValue);
                txtSpeedUnit.setText(strSpeedUnit);
                txtSpeedChangeValue.setText(strSpeedChangeValue);
                txtSpeedChangeUnit.setText(strSpeedChangeUnit);
                txtLongitudeValue.setText(strLongitude);
                txtLongitudeChange.setText(strLongitudeChange);
                txtLatitudeValue.setText(strLatitude);
                txtLatitudeChange.setText(strLatitudeChange);
                txtAltitudeValue.setText(strAltitude);
                txtAltitudeChange.setText(strAltitudeChange);
                txtDistanceValue.setText(strDistanceValue);
                txtDistanceUnit.setText(strDistanceUnit);
                txtTimeValue.setText(strTimeValue);
                txtTimeUnit.setText(strTimeUnit);
                txtRunningTime.setText(strRunningTime);

                // Set Value for the TextView in High Score:
                String text_speedData = strSpeedValue + " /" + strSpeedUnit;
                txtHC_speedData.setText(text_speedData);
                txtHC_speedData.setTextColor(Color.BLACK);
                txtHC_variationSpeed.setText(strVariantSpeed);
                txtHC_variationSpeed.setTextColor(Color.BLUE);
                txtHC_maxSpeed.setText(strGlobalSpeedMax);
                txtHC_maxSpeed.setTextColor(Color.RED);
                txtHC_minSpeed.setText(strGlobalSpeedMin);
                txtHC_minSpeed.setTextColor(Color.GREEN);

                String text_altitudeData = strAltitude + " /" + strDistanceUnit;
                txtHC_Altitude.setText(text_altitudeData);
                txtHC_Altitude.setTextColor(Color.MAGENTA);
                txtHC_variationAltitude.setText(strVariantAltitude);
                txtHC_variationAltitude.setTextColor(Color.BLUE);
                txtHC_maxAltitude.setText(strMaxAltitude);
                txtHC_maxAltitude.setTextColor(Color.RED);
                txtHC_minAltitude.setText(strMinAltitude);
                txtHC_minAltitude.setTextColor(Color.GREEN);

            }

            return false;
        }
    });


    // Update variable within the class of Location.
    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            // update message when location changes
            updateShow(location);
        }

        @Override
        public void onProviderEnabled(String provider) {

            // check GPS authority
            if ( checkCallingOrSelfPermission(ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(MainActivity.this, "Turn on GPS", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, 0);
                return;
            }

            updateShow(locationManager.getLastKnownLocation(provider));
        }

        @Override
        public void onProviderDisabled(String provider) {
            updateShow(null);
        }
    };


    public double speed_unit_transfer_value(double speed, int Unit_Speed){
        switch (Unit_Speed) {
            case 0:
                return significant_fraction(speed, FRACTION_CONSTRAINT);
            case 1:
                return significant_fraction(3.6 * speed, FRACTION_CONSTRAINT);
            case 2:
                return significant_fraction(2.2369362921 * speed, FRACTION_CONSTRAINT);
            default:
                return significant_fraction(2.2369362921 / 60 * speed, FRACTION_CONSTRAINT);
        }
    }

    public String speed_unit_transfer_unit(int Unit_Speed){
        switch (Unit_Speed) {
            case 0:
                return "Mps";
            case 1:
                return "Kph";
            case 2:
                return "Mph";
            default:
                return "Mpm";
        }
    }

    public double distance_unit_transfer_value(double distance, int Unit_Distance){
        switch (Unit_Distance) {
            case 0:
                return significant_fraction(distance, FRACTION_CONSTRAINT) ;
            case 1:
                return significant_fraction(distance / 1000, FRACTION_CONSTRAINT);
            case 2:
                return significant_fraction(distance * 0.000621371192, FRACTION_CONSTRAINT);
            default:
                return significant_fraction(distance * 3.2808399, FRACTION_CONSTRAINT);
        }
    }

    public String distance_unit_transfer_unit(int Unit_Distance){
        switch (Unit_Distance) {
            case 0:
                return "m";
            case 1:
                return "km";
            case 2:
                return "miles";
            default:
                return "feet";
        }
    }

    public double time_unit_transfer_value(double time, int Unit_Time){
        switch (Unit_Time) {
            case 0:
                return significant_fraction(time, FRACTION_CONSTRAINT);
            case 1:
                return significant_fraction(time / 60, FRACTION_CONSTRAINT);
            case 2:
                return significant_fraction(time / 3600, FRACTION_CONSTRAINT);
            default:
                return significant_fraction(time / 3600 / 24, FRACTION_CONSTRAINT);
        }
    }

    public String time_unit_transfer_unit(int Unit_Time){
        switch (Unit_Time) {
            case 0:
                return "s";
            case 1:
                return "mins";
            case 2:
                return "hrs";
            default:
                return "days";
        }
    }

    /**
     * Given a string, and check if it matches the regular expression of
     * @param var: Input decimal number that needed to be rounded up.
     * @param decimal_places : Number of decimal place to round up
     * @return A rounded value in certain significant figures.
     */
    private synchronized double significant_fraction(double var, int decimal_places) {

        String str_var = Double.toString(Math.abs(var));
        int decimal_place = str_var.length() - str_var.indexOf('.') - 1;

        // If variables' decimal places length is larger than our expectation:
        if (decimal_place > decimal_places)
        {
            int integer_part = (int)var;
            BigDecimal big_var = new BigDecimal(var - integer_part);
            big_var = big_var.round(new MathContext(decimal_places));
            return big_var.doubleValue() + integer_part;
        }
        // Else: No need to round up, return the original value to expedite running of the program.
        else
        {
            return var;
        }

    }


    /**
     * Synchronize update the MainActivity by the update of Location variables.
     * Synchronized used: Wait until responses from Local Database update finishing, and then update
     * @param location: The data class object of the Location, consists of a latitude, longitude, timestamp, accuracy,
     *                and other information such as bearing, altitude and velocity
     */
    private synchronized void updateShow(Location location) {

        // Not test mode, get real-time data.
        if(!isTest) {
            curTime = System.nanoTime();

            if (location != null) {

                StringBuilder stringBuilderLongitude = new StringBuilder();
                StringBuilder stringBuilderLatitude = new StringBuilder();
                StringBuilder stringBuilderAltitude = new StringBuilder();

                StringBuilder stringBuilderLongitudeChange = new StringBuilder();
                StringBuilder stringBuilderLatitudeChange = new StringBuilder();
                StringBuilder stringBuilderAltitudeChange = new StringBuilder();

                NumberFormat formatter = new DecimalFormat("#0.0000");
                if(strLongitudeTemp!=null){
                    stringBuilderLongitudeChange.append(formatter.format(significant_fraction(location.getLongitude(), GPS_CONSTRAINT) - strLongitudeTemp));
                    stringBuilderLatitudeChange.append(formatter.format(significant_fraction(location.getLatitude(), GPS_CONSTRAINT) - strLatitudeTemp));
                    stringBuilderAltitudeChange.append(formatter.format(speed_unit_transfer_value(significant_fraction(location.getAltitude(), FRACTION_CONSTRAINT), Unit_distance) - strAltitudeTemp));
                    stringBuilderAltitudeChange.append(distance_unit_transfer_unit(Unit_distance));
                }

                strLongitudeTemp = significant_fraction(location.getLongitude(), GPS_CONSTRAINT);
                strLatitudeTemp = significant_fraction(location.getLatitude(), GPS_CONSTRAINT);
                strAltitudeTemp = speed_unit_transfer_value(significant_fraction(location.getAltitude(), FRACTION_CONSTRAINT), Unit_distance);

                stringBuilderLongitude.append(significant_fraction(location.getLongitude(), GPS_CONSTRAINT));
                stringBuilderLatitude.append(significant_fraction(location.getLatitude(), GPS_CONSTRAINT));
                stringBuilderAltitude.append(
                                speed_unit_transfer_value(
                                significant_fraction(location.getAltitude(), FRACTION_CONSTRAINT), Unit_distance)
                );
                stringBuilderAltitude.append(distance_unit_transfer_unit(Unit_distance));

                // String Builder for calculate values of matrices in Main View:
                StringBuilder stringBuilderSpeedValue = new StringBuilder();
                StringBuilder stringBuilderSpeedUnit = new StringBuilder();
                StringBuilder stringBuilderSpeedChangeValue = new StringBuilder();
                StringBuilder stringBuilderSpeedChangeUnit = new StringBuilder();
                StringBuilder stringBuilderTimeValue = new StringBuilder();
                StringBuilder stringBuilderTimeUnit = new StringBuilder();
                StringBuilder stringBuilderDistanceValue = new StringBuilder();
                StringBuilder stringBuilderDistanceUnit = new StringBuilder();
                StringBuilder stringBuilderRunningTime = new StringBuilder();

                // String Builder for calculate values of matrices in HighScore View:
                StringBuilder stringBuilderVarianceSpeed = new StringBuilder();
                StringBuilder stringBuilderGlobalMaxSpeed = new StringBuilder();
                StringBuilder stringBuilderGlobalMinSpeed = new StringBuilder();
                StringBuilder stringBuilderVarianceAltitude = new StringBuilder();
                StringBuilder stringBuilderMaxAltitude = new StringBuilder();
                StringBuilder stringBuilderMinAltitude = new StringBuilder();

                valCurrentSpeed = 3.6 * location.getSpeed();

                if(!(preLatitude == location.getLatitude() && preLongitude == location.getLongitude())){
                    valCurrentTime = (curTime - preTime) / 1E9 + valCurrentTime;
                }
                preLatitude = location.getLatitude();
                preLongitude = location.getLongitude();
                valCurrentDistance = valCurrentDistance + location.getSpeed() * (curTime - preTime) / 1E9;

                // ------  Functionalities of RESET:  ------
                preTime = curTime;
                if(isReset){
                    preTime = System.nanoTime();
                    valCurrentTime = 0;
                    valCurrentDistance = 0;
                    valGlobalMaxSpeed = 0.0;
                    valGlobalMinSpeed = Double.MAX_VALUE;
                    valMaxAltitude = 0.0;
                    valMinAltitude = Double.MAX_VALUE;
                    valVarianceAltitude = 0.0;
                    valVarianceSpeed = 0.0;

                    // Clear Database:
                    for (Loc locItem : database.locDao().getAll()) {
                        database.locDao().delete(locItem);
                    }

                    for (Loc locItem : globalDatabase.locDao().getAll()) {
                        globalDatabase.locDao().delete(locItem);
                    }

                    isReset = false;
                }

                if(strSpeedTemp!=null){
                    stringBuilderSpeedChangeValue.append(speed_unit_transfer_value(location.getSpeed(), Unit_Speed)-strSpeedTemp);
                    stringBuilderSpeedChangeUnit.append(speed_unit_transfer_unit(Unit_Speed));
                }
                strSpeedTemp = speed_unit_transfer_value(location.getSpeed(), Unit_Speed);

                // Append transferred unit value into the TextView and showing:
                stringBuilderSpeedValue.append(speed_unit_transfer_value(location.getSpeed(), Unit_Speed));
                stringBuilderSpeedUnit.append(speed_unit_transfer_unit(Unit_Speed));
                stringBuilderTimeValue.append(time_unit_transfer_value(valCurrentTime,Unit_Time));
                stringBuilderTimeUnit.append(time_unit_transfer_unit(Unit_Time));
                stringBuilderDistanceValue.append(distance_unit_transfer_value(valCurrentDistance,Unit_distance));
                stringBuilderDistanceUnit.append(distance_unit_transfer_unit(Unit_distance));
                stringBuilderRunningTime.append(time_unit_transfer_value((curTime - runningTime) / 1E9, Unit_Time)).append(time_unit_transfer_unit(Unit_Time));

                // ------ Global Database and High Score View update: -------
                updateHighScoreData(location, stringBuilderVarianceSpeed,
                                    stringBuilderGlobalMaxSpeed,
                                    stringBuilderGlobalMinSpeed,
                                    stringBuilderVarianceAltitude,
                                    stringBuilderMaxAltitude,
                                    stringBuilderMinAltitude);

                /* ------------------------------------------------------------------------------------------------------*/
                // ---- Main View: ----

                // GPS information:
                strLongitude = stringBuilderLongitude.toString();
                strLatitude = stringBuilderLatitude.toString();
                strAltitude = stringBuilderAltitude.toString();

                strLongitudeChange = stringBuilderLongitudeChange.toString();
                strLatitudeChange = stringBuilderLatitudeChange.toString();
                strAltitudeChange = stringBuilderAltitudeChange.toString();

                // Speed information:
                strSpeedValue = stringBuilderSpeedValue.toString();
                strSpeedUnit = stringBuilderSpeedUnit.toString();

                strSpeedChangeValue = stringBuilderSpeedChangeValue.toString();
                strSpeedChangeUnit = stringBuilderSpeedChangeUnit.toString();

                // Time information:
                strTimeValue = stringBuilderTimeValue.toString();
                strTimeUnit = stringBuilderTimeUnit.toString();

                // Distance information:
                strDistanceValue = stringBuilderDistanceValue.toString();
                strDistanceUnit = stringBuilderDistanceUnit.toString();

                // Running time:
                strRunningTime = stringBuilderRunningTime.toString();

                // Put location data to back-end database
                Loc loc = new Loc();
                loc.id = strRunningTime;
                loc.speed = location.getSpeed();
                loc.height = location.getAltitude();
                database.locDao().insertAll(loc);

            } else {
                // If Location is null, cannot grab information from Location (etc. "Non Fine authority of GPS").
                strLongitude = "";
                strLatitude = "";
                strAltitude = "";
                strSpeedValue = "";
                strTimeValue = "";
                strTimeUnit = "";
                strDistanceValue = "";
                strDistanceUnit = "";
                strLongitudeChange = "";
                strLatitudeChange = "";
                strAltitudeChange = "";
                strSpeedChangeUnit = "";
                strSpeedChangeValue = "";

                // Highest Scores:
                strVariantSpeed = "";
                strGlobalSpeedMax = "";
                strGlobalSpeedMin = "";
                strVariantAltitude = "";
                strMaxAltitude = "";
                strMinAltitude = "";
                strRunningTime = time_unit_transfer_value((curTime - runningTime) / 1E9, Unit_Time)+ time_unit_transfer_unit(Unit_Time);
            }
        }
        else {
            // ----------  Testing mode: ---------
            simulation_distance();
            curTime = System.nanoTime();
            if (location != null) {

                StringBuilder stringBuilderLongitude = new StringBuilder();
                StringBuilder stringBuilderLatitude = new StringBuilder();
                StringBuilder stringBuilderAltitude = new StringBuilder();
                StringBuilder stringBuilderSpeedValue = new StringBuilder();
                StringBuilder stringBuilderSpeedUnit = new StringBuilder();
                StringBuilder stringBuilderTimeValue = new StringBuilder();
                StringBuilder stringBuilderTimeUnit = new StringBuilder();
                StringBuilder stringBuilderDistanceValue = new StringBuilder();
                StringBuilder stringBuilderDistanceUnit = new StringBuilder();
                StringBuilder stringBuilderRunningTime = new StringBuilder();


                StringBuilder stringBuilderLongitudeChange = new StringBuilder();
                StringBuilder stringBuilderLatitudeChange = new StringBuilder();
                StringBuilder stringBuilderAltitudeChange = new StringBuilder();

                if(strLongitudeTemp!=null){
                    stringBuilderLongitudeChange.append(significant_fraction(location.getLongitude(), GPS_CONSTRAINT) - strLongitudeTemp);
                    stringBuilderLatitudeChange.append(significant_fraction(location.getLatitude(), GPS_CONSTRAINT) - strLatitudeTemp);
                    stringBuilderAltitudeChange.append(significant_fraction(curTime/1E20, GPS_CONSTRAINT) - strAltitudeTemp);
                }

                strLongitudeTemp = significant_fraction(location.getLongitude(), GPS_CONSTRAINT);
                strLatitudeTemp = significant_fraction(location.getLatitude(), GPS_CONSTRAINT);
                strAltitudeTemp = significant_fraction(curTime/1E20, GPS_CONSTRAINT);

                stringBuilderLongitude.append(significant_fraction(valLongitude, GPS_CONSTRAINT));
                stringBuilderLatitude.append(significant_fraction(valLatitude, GPS_CONSTRAINT));
                stringBuilderAltitude.append(significant_fraction(curTime/1E20, GPS_CONSTRAINT));
                Sim_valCurrentTime = (curTime - preTime) / 1E9 + Sim_valCurrentTime;
                Sim_valCurrentDistance = Sim_valCurrentDistance + Sim_valCurrentSpeed * (curTime - preTime) / 1E9;

                valCurrentSpeed = 3.6 * location.getSpeed();
                if(!(preLatitude == location.getLatitude() && preLongitude == location.getLongitude())){
                    valCurrentTime = (curTime - preTime) / 1E9 + valCurrentTime;
                }
                preLatitude = location.getLatitude();
                preLongitude = location.getLongitude();
                valCurrentDistance = valCurrentDistance + location.getSpeed() * (curTime - preTime) / 1E9;

                preTime = curTime;
                if(isReset){
                    preTime = System.nanoTime();
                    Sim_valCurrentTime = 0;
                    Sim_valCurrentDistance = 0;
                    valCurrentMaxSpeed = 0;
                    valCurrentMinSpeed = Double.MAX_VALUE;
                    valGlobalMinSpeed = Double.MAX_VALUE;
                    valGlobalMaxSpeed = 0.0;
                    valMaxAltitude = 0.0;
                    valMinAltitude = Double.MAX_VALUE;
                    valVarianceAltitude = 0.0;
                    valVarianceSpeed = 0.0;

                    // Clearing Database:
                    for (Loc locItem : database.locDao().getAll()) {
                        database.locDao().delete(locItem);
                    }
                    for (Loc locItem : globalDatabase.locDao().getAll()) {
                        globalDatabase.locDao().delete(locItem);
                    }

                    isReset = false;
                }

                // Unit Transfer
                stringBuilderSpeedValue.append(speed_unit_transfer_value(Sim_valCurrentSpeed,Unit_Speed));
                stringBuilderSpeedUnit.append(speed_unit_transfer_unit(Unit_Speed));
                stringBuilderTimeValue.append(time_unit_transfer_value(Sim_valCurrentTime,Unit_Time));
                stringBuilderTimeUnit.append(time_unit_transfer_unit(Unit_Time));
                stringBuilderDistanceValue.append(distance_unit_transfer_value(Sim_valCurrentDistance,Unit_distance));
                stringBuilderDistanceUnit.append(distance_unit_transfer_unit(Unit_distance));
                stringBuilderRunningTime.append(time_unit_transfer_value((curTime - runningTime) / 1E9, Unit_Time)).append(time_unit_transfer_unit(Unit_Time));

                strLongitude = stringBuilderLongitude.toString();
                strLatitude = stringBuilderLatitude.toString();
                strAltitude = stringBuilderAltitude.toString();
                strSpeedValue = stringBuilderSpeedValue.toString();
                strSpeedUnit = stringBuilderSpeedUnit.toString();
                strTimeValue = stringBuilderTimeValue.toString();
                strTimeUnit = stringBuilderTimeUnit.toString();
                strDistanceValue = stringBuilderDistanceValue.toString();
                strDistanceUnit = stringBuilderDistanceUnit.toString();
                strRunningTime = stringBuilderRunningTime.toString();

                strLongitudeChange = stringBuilderLongitudeChange.toString();
                strLatitudeChange = stringBuilderLatitudeChange.toString();
                strAltitudeChange = stringBuilderAltitudeChange.toString();

            } else {

                strLongitude = "";
                strLatitude = "";
                strAltitude = "";
                strSpeedValue = "";
                strSpeedUnit = "";
                strTimeValue = "";
                strTimeUnit = "";
                strDistanceValue = "";
                strDistanceUnit = "";
                strLongitudeChange = "";
                strLatitudeChange = "";
                strAltitudeChange = "";

//                // Highest Scores:
//                strVariantSpeed = "";
//                strGlobalSpeedMax = "";
//                strGlobalSpeedMin = "";
//                strVariantAltitude = "";
//                strMaxAltitude = "";
//                strMinAltitude = "";
                strRunningTime = time_unit_transfer_value((curTime - runningTime) / 1E9, Unit_Time)+ time_unit_transfer_unit(Unit_Time);
            }
        }

        handler.sendEmptyMessage(0x001);
    }

    /**
     * Using Dom to Record all extreme values of matrices from the User
     * @param location: Location Object,
     * @param stringBuilderGlobalMaxSpeed:
     * @param stringBuilderGlobalMinSpeed:
     * High Score View Back-end:
     *      @Speed:     MaximumSpeed, MinimumSpeed Reached; variation of speed;
     *      @Altitude:  LongestAltitude Reached; variation of Longitude;
     *      @Distance:  LongestDistance Traveled;
     *      @Time:      GlobalLongestTime Traveled;
     */

    private synchronized void updateHighScoreData(Location location, StringBuilder stringBuilderVarianceSpeed,
                                                  StringBuilder stringBuilderGlobalMaxSpeed,
                                                  StringBuilder stringBuilderGlobalMinSpeed,
                                                  StringBuilder stringBuilderVarianceAltitude,
                                                  StringBuilder stringBuilderMaxAltitude,
                                                  StringBuilder stringBuilderMinAltitude)  {

        // ---------- Update Variance Speed ----------
        String[] variant_s = new String[1];
        variant_s[0] = "variant_speed";
        if (globalDatabase.locDao().loadAllByIds(variant_s).size() > 0) {
            valVarianceSpeed = globalDatabase.locDao().loadAllByIds(variant_s).get(0).speed;
            // If Current Speed is greater than the last time stamp's speed, Print the Differences and store the current Speed.
            if (location.getSpeed() > valVarianceSpeed) {
                double difference = location.getSpeed() - valVarianceSpeed;
                Loc loc = new Loc();
                loc.speed = location.getSpeed();
                loc.id = "variant_speed";
                globalDatabase.locDao().delete(loc);
                globalDatabase.locDao().insertAll(loc);
                stringBuilderVarianceSpeed.append("+ ");
                stringBuilderVarianceSpeed.append(speed_unit_transfer_value(difference, Unit_Speed));
            }
            // Else: Current Speed is smaller than the last time stamp's speed, Print the Differences and store the current Speed.
            else {
                double difference = valVarianceSpeed - location.getSpeed();
                Loc loc = new Loc();
                loc.speed = location.getSpeed();
                loc.id = "variant_speed";
                globalDatabase.locDao().delete(loc);
                globalDatabase.locDao().insertAll(loc);
                stringBuilderVarianceSpeed.append("- ");
                stringBuilderVarianceSpeed.append(speed_unit_transfer_value(difference, Unit_Speed));
            }

        } else {
            Loc loc = new Loc();
            loc.speed = location.getSpeed();
            loc.id = "variant_speed";
            globalDatabase.locDao().delete(loc);
            globalDatabase.locDao().insertAll(loc);
            stringBuilderVarianceSpeed.append(speed_unit_transfer_value(location.getSpeed(), Unit_Speed));
        }
        stringBuilderVarianceSpeed.append(speed_unit_transfer_unit(Unit_Speed));
        strVariantSpeed = stringBuilderVarianceSpeed.toString();


        // --------- Update Global Maximum Speed; ----------
        String[] global_sMax = new String[1];
        global_sMax[0] = "g_max";
        // If existed this entity in Global-Database
        if (globalDatabase.locDao().loadAllByIds(global_sMax).size() > 0) {
            valGlobalMaxSpeed = globalDatabase.locDao().loadAllByIds(global_sMax).get(0).speed;

            if (location.getSpeed() > valGlobalMaxSpeed) {
                valGlobalMaxSpeed = location.getSpeed();
                Loc loc = new Loc();
                loc.speed = valGlobalMaxSpeed;
                loc.id = "g_max";
                globalDatabase.locDao().delete(loc);
                globalDatabase.locDao().insertAll(loc);
            }
        } else {
            // Initiate this entity
            Loc loc = new Loc();
            loc.speed = valGlobalMaxSpeed;
            loc.id = "g_max";
            globalDatabase.locDao().delete(loc);
            globalDatabase.locDao().insertAll(loc);
        }

        stringBuilderGlobalMaxSpeed.append(speed_unit_transfer_value(valGlobalMaxSpeed, Unit_Speed));
        stringBuilderGlobalMaxSpeed.append(speed_unit_transfer_unit(Unit_Speed));
        strGlobalSpeedMax = stringBuilderGlobalMaxSpeed.toString();


        // Update Global Minimum Speed;
        String[] global_sMin = new String[1];
        global_sMin[0] = "g_min";
        // If existed this entity in Global-Database
        if (globalDatabase.locDao().loadAllByIds(global_sMin).size() > 0) {
            valGlobalMinSpeed = globalDatabase.locDao().loadAllByIds(global_sMin).get(0).speed;

            if (location.getSpeed() < valGlobalMinSpeed) {
                valGlobalMinSpeed = location.getSpeed();
                Loc loc = new Loc();
                loc.speed = valGlobalMinSpeed;
                loc.id = "g_min";
                globalDatabase.locDao().delete(loc);
                globalDatabase.locDao().insertAll(loc);
            }
        } else {
            // Initiate this entity
            Loc loc = new Loc();
            loc.speed = valGlobalMinSpeed;
            loc.id = "g_min";
            globalDatabase.locDao().delete(loc);
            globalDatabase.locDao().insertAll(loc);
        }
        stringBuilderGlobalMinSpeed.append(speed_unit_transfer_value(valGlobalMinSpeed, Unit_Speed));
        stringBuilderGlobalMinSpeed.append(speed_unit_transfer_unit(Unit_Speed));
        strGlobalSpeedMin = stringBuilderGlobalMinSpeed.toString();


        // ---------- Update Variance Altitude ----------
        String[] variant_a = new String[1];
        variant_a[0] = "variant_altitude";
        if (globalDatabase.locDao().loadAllByIds(variant_a).size() > 0) {
            valVarianceAltitude = globalDatabase.locDao().loadAllByIds(variant_a).get(0).speed;

            // If Current Speed is Greater than the last time stamp's Height(Altitude),
            // Print the Differences and store the current Speed.
            if (location.getAltitude() > valVarianceAltitude) {
                double difference = location.getAltitude() - valVarianceAltitude;
                Loc loc = new Loc();
                loc.speed = location.getAltitude();
                loc.id = "variant_altitude";
                globalDatabase.locDao().delete(loc);
                globalDatabase.locDao().insertAll(loc);
                stringBuilderVarianceAltitude.append("+ ");
                stringBuilderVarianceAltitude.append(speed_unit_transfer_value(difference, Unit_distance));
            }
            // Else: Current Speed is Smaller than the last time stamp's Height(altitude),
            // Print the Differences and store the current Speed.
            else {
                double difference = valVarianceAltitude - location.getAltitude();
                Loc loc = new Loc();
                loc.speed = location.getAltitude();
                loc.id = "variant_altitude";
                globalDatabase.locDao().delete(loc);
                globalDatabase.locDao().insertAll(loc);
                stringBuilderVarianceAltitude.append("- ");
                stringBuilderVarianceAltitude.append(speed_unit_transfer_value(difference, Unit_distance));
            }

        } else {
            // Initialize difference as = 0.0;
            Loc loc = new Loc();
            loc.speed = 0.0;
            loc.id = "variant_altitude";
            globalDatabase.locDao().delete(loc);
            globalDatabase.locDao().insertAll(loc);
            stringBuilderVarianceAltitude.append(speed_unit_transfer_value(0.0, Unit_distance));
        }

        stringBuilderVarianceAltitude.append(speed_unit_transfer_unit(Unit_distance));
        strVariantAltitude = stringBuilderVarianceAltitude.toString();


        // --------- Update Global Highest Altitude; ----------
        String[] global_HighAL = new String[1];
        global_HighAL[0] = "g_maxAltitude";

        // If existed this entity in Global-Database
        if (globalDatabase.locDao().loadAllByIds(global_HighAL).size() > 0) {
            valMaxAltitude = globalDatabase.locDao().loadAllByIds(global_HighAL).get(0).speed;

            if (location.getAltitude() >= valMaxAltitude) {
                valGlobalMaxSpeed = location.getAltitude();
                Loc loc = new Loc();
                loc.speed = valMaxAltitude;
                loc.id = "g_maxAltitude";
                globalDatabase.locDao().delete(loc);
                globalDatabase.locDao().insertAll(loc);
            }
        } else {
            Loc loc = new Loc();
            loc.speed = valMaxAltitude;
            loc.id = "g_maxAltitude";
            globalDatabase.locDao().delete(loc);
            globalDatabase.locDao().insertAll(loc);
        }

        stringBuilderMaxAltitude.append(distance_unit_transfer_value(valMaxAltitude, Unit_distance));
        stringBuilderMaxAltitude.append(distance_unit_transfer_unit(Unit_distance));
        strMaxAltitude = stringBuilderMaxAltitude.toString();


        // --------- Update Global Lowest Altitude; ----------
        String[] global_LowAL = new String[1];
        global_LowAL[0] = "g_minAltitude";

        // If existed this entity in Global-Database
        if (globalDatabase.locDao().loadAllByIds(global_LowAL).size() > 0) {
            valMinAltitude = globalDatabase.locDao().loadAllByIds(global_LowAL).get(0).speed;

            if (location.getAltitude() < valMinAltitude) {
                valMinAltitude = location.getAltitude();
                Loc loc = new Loc();
                loc.speed = valMinAltitude;
                loc.id = "g_minAltitude";
                globalDatabase.locDao().delete(loc);
                globalDatabase.locDao().insertAll(loc);
            }
        } else {
            Loc loc = new Loc();
            loc.speed = valMinAltitude;
            loc.id = "g_minAltitude";
            globalDatabase.locDao().delete(loc);
            globalDatabase.locDao().insertAll(loc);
        }
        stringBuilderMinAltitude.append(distance_unit_transfer_value(valMinAltitude, Unit_distance));
        stringBuilderMinAltitude.append(distance_unit_transfer_unit(Unit_distance));
        strMinAltitude = stringBuilderMinAltitude.toString();

    }


    //--------------------------------------------------------

    private void simulation_distance() {
        double r_earth = 6378.0;
        double dy = 0;
        double dx = 16.098 / 36000;
        double new_latitude = valLatitude + (dy / r_earth) * (180 / Math.PI);
        double new_longitude = valLongitude + (dx / r_earth) * (180 / Math.PI) / Math.cos(valLatitude * Math.PI/180);
        Sim_valCurrentSpeed = 16.098 / 3.6;
        double speed = valCurrentSpeed;
        StringBuilder sb = new StringBuilder();
        valLatitude = new_latitude;
        valLongitude = new_longitude;
    }

    public void locationUpdate() {

        // check GPS authority from User and App..
        if (checkCallingOrSelfPermission(ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            makeToast("You should enable GPS to get full functionalities work !");
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 0);
            return;
        }

        Location location = locationManager.getLastKnownLocation(LocationManager. GPS_PROVIDER);

        updateShow(location);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, mLocationListener);
    }

    public void setFontSize(View v, float fontSizeValue) {
        if(v instanceof TextView)
        {
            ((TextView) v).setTextSize(fontSizeValue);
        }
        else if(v instanceof EditText)
        {
            ((EditText) v).setTextSize(fontSizeValue);
        }
        else if(v instanceof Button)
        {
            ((Button) v).setTextSize(fontSizeValue);
        }
        else
        {
            int vChildCount = ((ViewGroup) v).getChildCount();
            for(int i = 0; i < vChildCount; i++)
            {
                View v1 = ((ViewGroup) v).getChildAt(i);
                setFontSize(v1, fontSizeValue);
            }
        }
    }

    public void helpful_click() {
        // help Button.
        btnHelp = (Button) findViewById(R.id.btnHelp);

        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Intent intent = new Intent();
                // Jump to Help Activity:
                intent.setClass(getApplicationContext(), HelpActivity.class);
                startActivity(intent);

            }
        });

    }

    /**
     * Show a Toast of the given string
     *
     * @param str The string to show in the Toast
     */
    public void makeToast(String str) {
        runOnUiThread(() -> Toast.makeText(this, str, Toast.LENGTH_LONG).show());
    }

}