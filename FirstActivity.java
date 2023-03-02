package com.example.modbtp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.zgkxzx.modbus4And.requset.ModbusParam;
import com.zgkxzx.modbus4And.requset.ModbusReq;
import com.zgkxzx.modbus4And.requset.OnRequestBack;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class FirstActivity extends AppCompatActivity {
    List<FetchData> fetchData;
    ArrayAdapter<String> adapter,heading;
    ArrayAdapter<String> adapter1,heading1;
    ArrayAdapter<String> adapter2,heading2;
    RecyclerView recyclerView;
    HelperAdapter helperAdapter;
    Button button;
    Button Nextbutton;
    TextView tvmonth;
    TextView status;
    EditText value;
    private static String valueSet;
    private static boolean writeSuccess;
    private static int writeAddress;
    private static short countFail;
    String TAG;
    String ipSet = "192.168.1.7";
    int portSet = 502;
    int timeOutSet = 100000;
    int slaveSet = 1;
    int addressSet = 72;
    int lengthSet = 1;
    int addressSet0 = 77;
    int lengthSet0 = 1;
    int scanRateSet = 1000;
    int addressSet1 = 5;
    int lengthSet1 = 1;
    int addressSet2 = 13;
    int lengthSet2 = 1;
    int addressSet3 = 9;  //lATITUDE
    int lengthSet3 = 1;
    int addressSet4 = 10; //lATITUDE1
    int lengthSet4 = 1;
    int addressSet5 = 11; //lONGITUDE
    int lengthSet5 = 1;
    int addressSet6 = 12; //LONGITUDE1
    int lengthSet6 = 1;
    int addressSet7 = 0;
    int lengthSet7 = 1;
    int addressSet8 = 0;
    int lengthSet8 = 1;
    int addressSet9 = 0;
    int lengthSet9 = 1;
    int addressSet10 = 0;
    int lengthSet10 = 1;
    int addressSet11 = 0;
    int lengthSet11 = 1;
    int addressSet12 = 0;
    int lengthSet12 = 1;
    int addressSet13 = 0;
    int lengthSet13 = 1;
    Double BLatitude;
    Double BLongitude;
    double BLatitude1;
    double BLongitude1;
    private boolean[] readBufferl;
    private boolean[] readBufferl1;
    private boolean[] readBuffera;
    private static boolean reWriteFlag;
    private short[] readBuffer;
    private short[] readBufferb;
    private short[] readBufferb1;
    private short[] readBufferb2;
    private short[] readBufferb3;
    private short[] readBufferb4;
    private short[] readBufferb5;
    private short[] readBufferb6;
    private short[] readBufferb7;
    private short[] readBufferb8;
    private boolean readSuccess;
    private boolean initialSuccess;
    private boolean scrollList;
    private ProgressDialog progressDialog;
    private static boolean setupFlag;
    ListView dataList,dataTitle;
    ArrayList<String> items,title;
    ListView dataList1,dataTitle1;
    ArrayList<String> items1,title1;
    ListView dataList2,dataTitle2;
    ArrayList<String> items2,title2;
    EditText address;
    EditText length;
    EditText slave;
    TextView addressText;
    TextView lengthText;
    TextView slaveText;
    int scanRate;
    String TAG_P = "Main";
    String TAG_S = "ScrollList";
    String TAG_C = "ClickList";
    float x1,x2,y1,y2;
    private TextView latitudeTextView;
    private TextView longitudeTextView;
    TextView latdiff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        status = ( TextView ) findViewById(R.id.status);
        modbusInit(ipSet, portSet, timeOutSet);
        TextView textView = findViewById(R.id.tvmonth);
       /* SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy G 'at' HH:mm:ss z");
        String currentDateandTime = sdf.format(new Date());
        textView.setText(currentDateandTime);*/
        dataList = ( ListView ) findViewById(R.id.data_list);
        dataTitle = ( ListView ) findViewById(R.id.data_title);
        dataList1 = ( ListView ) findViewById(R.id.data_list2);
        dataTitle1 = ( ListView ) findViewById(R.id.data_title1);
        dataList2 = ( ListView ) findViewById(R.id.data_list1);
        dataTitle2 = ( ListView ) findViewById(R.id.data_title2);
        //dataList.setOnClickListener(mItem1);
        button = findViewById(R.id.button3);
        button.setOnClickListener(mItem);
        final Handler handler = new Handler();
        latitudeTextView = findViewById(R.id.latitude);
        longitudeTextView = findViewById(R.id.longitude);
        latdiff = findViewById(R.id.latdiff);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
                } else {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }
            }
        }
        public class FirstActivity extends AppCompatActivity implements LocationListener {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                double lat1 = BLatitude1;
                double lon1 = BLongitude1;

                lat1 = Math.toRadians(lat1);
                lon1 = Math.toRadians(lon1);
                latitude = Math.toRadians(latitude);   //lat2
                longitude = Math.toRadians(longitude); //lon2

                latitudeTextView.setText("Latitude: " + latitude);
                longitudeTextView.setText("Longitude: " + longitude);

                // Haversine formula
                double dlon = longitude - lon1;
                double dlat = latitude - lat1;
                double a = Math.pow(Math.sin(dlat / 2), 2)
                        + Math.cos(lat1) * Math.cos(latitude)
                        * Math.pow(Math.sin(dlon / 2), 2);

                double c = 2 * Math.asin(Math.sqrt(a));

                // Radius of earth in kilometers.
                // for miles Use 3956
                double dist = (6371 * c) * 1000;
                String str = String.format("%.2f", dist);
                latdiff.setText("Distance: " + str + " Meter");
                // calculate the result
                //return(c * r);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        }

            Runnable refresh = new Runnable() {
                @Override
                public void run() {

                    readHoldingRegisters(slaveSet, addressSet3, lengthSet3);
                    readHoldingRegisters1(slaveSet, addressSet4, lengthSet4);
                    readHoldingRegisters2(slaveSet, addressSet5, lengthSet5);
                    readHoldingRegisters3(slaveSet, addressSet6, lengthSet6);
                    readHoldingRegisters4(slaveSet, addressSet7, lengthSet7);
                    readHoldingRegisters5(slaveSet, addressSet8, lengthSet8);
                    readHoldingRegisters6(slaveSet, addressSet9, lengthSet9);
                    readHoldingRegisters7(slaveSet, addressSet10, lengthSet10);
                    readHoldingRegisters8(slaveSet, addressSet11, lengthSet11);

                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy G 'at' HH:mm:ss z");
                    String currentDateandTime = sdf.format(new Date());
                    textView.setText(currentDateandTime);

                    updateDataOnList();
                    handler.postDelayed(this, 20000);
                }
            };

        handler.postDelayed(refresh,1000);
        }

    private View.OnClickListener mItem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            readHoldingRegisters(slaveSet, addressSet3, lengthSet3);
            readHoldingRegisters1(slaveSet, addressSet4, lengthSet4);
            readHoldingRegisters2(slaveSet, addressSet5, lengthSet5);
            readHoldingRegisters3(slaveSet, addressSet6, lengthSet6);
            readHoldingRegisters4(slaveSet, addressSet7, lengthSet7);
            readHoldingRegisters5(slaveSet, addressSet8, lengthSet8);
            readHoldingRegisters6(slaveSet, addressSet9, lengthSet9);
            readHoldingRegisters7(slaveSet, addressSet10, lengthSet10);
            readHoldingRegisters8(slaveSet, addressSet11, lengthSet11);

            if (reWriteFlag && readBuffer[writeAddress - Integer.valueOf(addressSet)] != Short.valueOf(valueSet)) {
                writeSingleRegister(Integer.valueOf(slaveSet), writeAddress, Integer.valueOf(valueSet));
                //Create Progress Dialog
                progressDialog = new ProgressDialog(FirstActivity.this);
                progressDialog.setMessage("Re-writing to Register"); //Set progress message
                progressDialog.show(); //Show progress dialog when waite writSuccess flag
                //Delay for write finish
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss(); //Close progress dialog
                        // Do something delay finish
                        if (writeSuccess) {
                            writeSuccess = false;
                        } //Clear flag
                        reWriteFlag = true; //Set flag
                    }
                }, 1000);
            } else {
                reWriteFlag = false; //Clear flag
            }
            //Detect scroll on data list
            dataList.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    //  Log.e(TAG_S, "View: "+view+" scrollState: "+scrollState);
                    // updateTimer.cancel();
                }


                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    Log.e(TAG_S, "First: " + firstVisibleItem + " visibleCount: " + visibleItemCount + " totalCount: " + totalItemCount);
                    if (firstVisibleItem == 0) {
                        scrollList = false; //Set flag when scroll on top item //Update list
                    } else {
                        scrollList = true; //Set flag when top item change //Don't update list
                    }
                }
            });
            //Detect click on data list for write request
            dataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.e(TAG_C, "Position: " + position);

                    reWriteFlag = false; //Clear flag

                    AlertDialog.Builder builder = new AlertDialog.Builder(FirstActivity.this);
                    LayoutInflater inflater = getLayoutInflater();

                    View popView = inflater.inflate(R.layout.write_layout, null);
                    builder.setView(popView);

                    value = ( EditText ) popView.findViewById(R.id.value_add);

                    writeAddress = Integer.valueOf(addressSet) + position;


                    //Set dialog can't cancelable
                    builder.setCancelable(false);
                    builder.setTitle("Writing to Address: " + writeAddress); //Set title on dialog
                    //Work when you press "Add"
                    builder.setPositiveButton("Write", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Move program to CustomSetupListener class
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    //Add animation with dialog before show
                    // alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimations_SmileWindow;
                    alertDialog.show();
                    Button addButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    //Check data before close dialog when you press "Write"
                    addButton.setOnClickListener(new CustomWriteListener(alertDialog));

                }
            });

            if (!scrollList) {
                //Update list
                updateDataOnList();
            }
        }
    };

    private void readHoldingRegisters8(int id, final int startAddress, final int length) {
        ModbusReq.getInstance().readHoldingRegisters(new OnRequestBack<short[]>() {
            @Override
            public void onSuccess(short[] data) {
                {
                    //Copy data to buffer
                    readBufferb8 = Arrays.copyOf(data, data.length);
                    System.out.println(readBufferb8);
                    readSuccess = true;
                    Log.d(TAG, "readHoldingRegisters onSuccess " + Arrays.toString(data));
                    System.out.println(Arrays.toString(data));
                }
            }

            @Override
            public void onFailed(String msg) {
                readSuccess = false;
                Log.e(TAG, "readHoldingRegisters onFailed " + msg);
            }
        }, id, startAddress, length);
    }
    private void readHoldingRegisters7(int id, final int startAddress, final int length) {
        ModbusReq.getInstance().readHoldingRegisters(new OnRequestBack<short[]>() {
            @Override
            public void onSuccess(short[] data) {
                {
                    //Copy data to buffer
                    readBufferb7 = Arrays.copyOf(data, data.length);
                    System.out.println(readBufferb7);
                    readSuccess = true;
                    Log.d(TAG, "readHoldingRegisters onSuccess " + Arrays.toString(data));
                    System.out.println(Arrays.toString(data));
                }
            }

            @Override
            public void onFailed(String msg) {
                readSuccess = false;
                Log.e(TAG, "readHoldingRegisters onFailed " + msg);
            }
        }, id, startAddress, length);
    }
    private void readHoldingRegisters6(int id, final int startAddress, final int length) {
        ModbusReq.getInstance().readHoldingRegisters(new OnRequestBack<short[]>() {
            @Override
            public void onSuccess(short[] data) {
                {
                    //Copy data to buffer
                    readBufferb6 = Arrays.copyOf(data, data.length);
                    System.out.println(readBufferb6);
                    readSuccess = true;
                    Log.d(TAG, "readHoldingRegisters onSuccess " + Arrays.toString(data));
                    System.out.println(Arrays.toString(data));
                }
            }

            @Override
            public void onFailed(String msg) {
                readSuccess = false;
                Log.e(TAG, "readHoldingRegisters onFailed " + msg);
            }
        }, id, startAddress, length);
    }
    private void readHoldingRegisters5(int id, final int startAddress, final int length) {
        ModbusReq.getInstance().readHoldingRegisters(new OnRequestBack<short[]>() {
            @Override
            public void onSuccess(short[] data) {
                {
                    //Copy data to buffer
                    readBufferb5 = Arrays.copyOf(data, data.length);
                    System.out.println(readBufferb5);
                    readSuccess = true;
                    Log.d(TAG, "readHoldingRegisters onSuccess " + Arrays.toString(data));
                    System.out.println(Arrays.toString(data));
                }
            }

            @Override
            public void onFailed(String msg) {
                readSuccess = false;
                Log.e(TAG, "readHoldingRegisters onFailed " + msg);
            }
        }, id, startAddress, length);
    }
    private void readHoldingRegisters4(int id, final int startAddress, final int length) {
        ModbusReq.getInstance().readHoldingRegisters(new OnRequestBack<short[]>() {
            @Override
            public void onSuccess(short[] data) {
                {
                    //Copy data to buffer
                    readBufferb4 = Arrays.copyOf(data, data.length);
                    System.out.println(readBufferb4);
                    readSuccess = true;
                    Log.d(TAG, "readHoldingRegisters onSuccess " + Arrays.toString(data));
                    System.out.println(Arrays.toString(data));
                }
            }

            @Override
            public void onFailed(String msg) {
                readSuccess = false;
                Log.e(TAG, "readHoldingRegisters onFailed " + msg);
            }
        }, id, startAddress, length);
    }
    private void readHoldingRegisters3(int id, final int startAddress, final int length) {
        ModbusReq.getInstance().readHoldingRegisters(new OnRequestBack<short[]>() {
            @Override
            public void onSuccess(short[] data) {
                {
                    //Copy data to buffer
                    readBufferb3 = Arrays.copyOf(data, data.length);
                    System.out.println(readBufferb3);
                    readSuccess = true;
                    Log.d(TAG, "readHoldingRegisters onSuccess " + Arrays.toString(data));
                    System.out.println(Arrays.toString(data));
                }
            }

            @Override
            public void onFailed(String msg) {
                readSuccess = false;
                Log.e(TAG, "readHoldingRegisters onFailed " + msg);
            }
        }, id, startAddress, length);
    }

    private void readHoldingRegisters2(int id, final int startAddress, final int length) {
        ModbusReq.getInstance().readHoldingRegisters(new OnRequestBack<short[]>() {
            @Override
            public void onSuccess(short[] data) {
                {
                    //Copy data to buffer
                    readBufferb2 = Arrays.copyOf(data, data.length);
                    System.out.println(readBufferb2);
                    readSuccess = true;
                    Log.d(TAG, "readHoldingRegisters onSuccess " + Arrays.toString(data));
                    System.out.println(Arrays.toString(data));
                }
            }

            @Override
            public void onFailed(String msg) {
                readSuccess = false;

                Log.e(TAG, "readHoldingRegisters onFailed " + msg);
            }
        }, id, startAddress, length);
    }

    private void readHoldingRegisters1(int id, final int startAddress, final int length) {
        ModbusReq.getInstance().readHoldingRegisters(new OnRequestBack<short[]>() {
            @Override
            public void onSuccess(short[] data) {
                {
                    //Copy data to buffer
                    readBufferb1 = Arrays.copyOf(data, data.length);
                    System.out.println(readBufferb1);
                    readSuccess = true;
                    Log.d(TAG, "readHoldingRegisters onSuccess " + Arrays.toString(data));
                    System.out.println(Arrays.toString(data));
                }
            }

            @Override
            public void onFailed(String msg) {
                readSuccess = false;
                Log.e(TAG, "readHoldingRegisters onFailed " + msg);
            }
        }, id, startAddress, length);
    }

    /* Function: Check data when you click Setup on Dialog */
    private void readCoil(int id, final int startAddress, final int length) {
        ModbusReq.getInstance().readCoil(new OnRequestBack<boolean[]>() {
            @Override
            public void onSuccess(boolean[] data) {
                //Copy data to buffer
                readBufferl = Arrays.copyOf(data, data.length);
                System.out.println(readBufferl);
                readSuccess = true;
                Log.d(TAG, "readCoil onSuccess " + Arrays.toString(data));
                System.out.println(Arrays.toString(data));
            }

            @Override
            public void onFailed(String msg) {
                readSuccess = false;
                Log.e(TAG, "readCoil onFailed " + msg);
            }
        }, id, startAddress, length);
    }

    private void readCoil1(int id, final int startAddress, final int length) {
        ModbusReq.getInstance().readCoil(new OnRequestBack<boolean[]>() {
            @Override
            public void onSuccess(boolean[] data) {
                //Copy data to buffer
                readBufferl1 = Arrays.copyOf(data, data.length);
                System.out.println(readBufferl1);
                readSuccess = true;
                Log.d(TAG, "readCoil onSuccess " + Arrays.toString(data));
                System.out.println(Arrays.toString(data));
            }

            @Override
            public void onFailed(String msg) {
                readSuccess = false;
                Log.e(TAG, "readCoil onFailed " + msg);
            }
        }, id, startAddress, length);
    }

    private void readHoldingRegisters(int id, final int startAddress, final int length) {
        ModbusReq.getInstance().readHoldingRegisters(new OnRequestBack<short[]>() {
            @Override
            public void onSuccess(short[] data) {
                {
                    //Copy data to buffer
                    readBufferb = Arrays.copyOf(data, data.length);
                    System.out.println(readBufferb);
                    readSuccess = true;
                    Log.d(TAG, "readHoldingRegisters onSuccess " + Arrays.toString(data));
                    System.out.println(Arrays.toString(data));
                }
            }

            @Override
            public void onFailed(String msg) {
                readSuccess = false;
                Log.e(TAG, "readHoldingRegisters onFailed " + msg);
            }
        }, id, startAddress, length);

    }

    public void modbusInit(String ip, int port, int time) {

        ModbusReq.getInstance().setParam(new ModbusParam()
                        .setHost(ip)
                        //.setHost(ip1)
                        .setPort(port)
                        .setEncapsulated(false)
                        .setKeepAlive(true)
                        .setTimeout(time)
                        .setRetries(2))
                .init(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        //Set flag initial
                        initialSuccess = true;
                        status.setText("Connected");
                        readHoldingRegisters(slaveSet,addressSet3, lengthSet3);
                        readHoldingRegisters1(slaveSet,addressSet4, lengthSet4);
                        readHoldingRegisters2(slaveSet,addressSet5, lengthSet5);
                        readHoldingRegisters3(slaveSet,addressSet6, lengthSet6);
                        readHoldingRegisters4(slaveSet,addressSet7, lengthSet7);
                        readHoldingRegisters5(slaveSet,addressSet8, lengthSet8);
                        readHoldingRegisters6(slaveSet,addressSet9, lengthSet9);
                        readHoldingRegisters7(slaveSet,addressSet10, lengthSet10);
                        readHoldingRegisters8(slaveSet,addressSet11, lengthSet11);
                        Log.d(TAG, "onSuccess " + s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        initialSuccess = false;
                        status.setText("Not Connected");
                        Log.d(TAG, "onFailed " + msg);
                    }
                });
    }

    class CustomWriteListener implements View.OnClickListener {
        private final Dialog dialog;

        public CustomWriteListener(Dialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void onClick(View v) {

            valueSet = value.getText().toString();

            //Check data if you don't check format data, it may bug
            if (!valueSet.equals("")){
                //Write data
                Log.e(TAG_C, "Write value: "+valueSet);
                writeSingleRegister(Integer.valueOf(slaveSet), writeAddress, Integer.valueOf(valueSet));
                //Create Progress Dialog
                progressDialog = new ProgressDialog(FirstActivity.this);
                progressDialog.setMessage("Writing to Register"); //Set progress message
                progressDialog.show(); //Show progress dialog when waite writSuccess flag
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something delay finish
                        progressDialog.dismiss(); //Close progress dialog
                        if (!writeSuccess){
                            Toast.makeText(FirstActivity.this, "Write failed!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(FirstActivity.this, "Write completed!", Toast.LENGTH_SHORT).show();
                            writeSuccess = false; //Clear flag
                            updateDataOnList();//Update List
                        }
                        reWriteFlag = true; //Set re-write flag
                    }
                }, 1000);
                dialog.dismiss();
            }else {

            }
        }
    }

    public void writeSingleRegister(int id, int address, int data){
        ModbusReq.getInstance().writeRegister(new OnRequestBack<String>() {
            @Override
            public void onSuccess(String s) {
                writeSuccess = true;
                Log.e(TAG, "writeRegister onSuccess " + s);
            }

            @Override
            public void onFailed(String msg) {
                writeSuccess = false;
                Log.e(TAG, "writeRegister onFailed " + msg);
            }
        }, id, address, data);}

    public void updateDataOnList() {
        if (readSuccess) {
            items = new ArrayList<>();
            title = new ArrayList<>();
            items1 = new ArrayList<>();
            title1 = new ArrayList<>();
            items2 = new ArrayList<>();
            title2 = new ArrayList<>();
            System.out.print(items);
            System.out.print(items1);
            int k = 0;
            int k4 = 0;
            do {
                int k5 = 0;
                //int k1 = 10000;
                //int k2 = 30000;
                int k3 = 40000;

                // title.add("TagName");
                items.add("B-Latitude");
                items.add("B-Longitude");
                // title1.add("Values");

                String s1 = Integer.toString(readBufferb[k4]);
                String s2 = Integer.toString(readBufferb1[k4]);
                String s =  s1 + s2;
                int BLat = Integer.parseInt(s);
                BLatitude1 = (BLat/10000.0);

                String s3 = Integer.toString(readBufferb2[k4]);
                String s4 = Integer.toString(readBufferb3[k4]);
                String s5 =  s3 + s4;
                int BLon = Integer.parseInt(s5);
                BLongitude1 =  BLon/10000.0;

                items1.add(String.valueOf(readBufferb[k4])+"."+String.valueOf(readBufferb1[k4]));  //Latitude
                items1.add(String.valueOf(readBufferb2[k4])+"."+String.valueOf(readBufferb3[k4])); //Longitude


                System.out.println("Address: " + (Integer.valueOf(addressSet3) + k3) + " = " + String.valueOf(readBufferb[k4]));
                System.out.println("Address: " + (Integer.valueOf(addressSet4) + k3) + " = " + String.valueOf(readBufferb1[k4]));
                System.out.println("Address: " + (Integer.valueOf(addressSet5) + k3) + " = " + String.valueOf(readBufferb2[k4]));
                System.out.println("Address: " + (Integer.valueOf(addressSet6) + k3) + " = " + String.valueOf(readBufferb3[k4]));
                System.out.println("Address: " + (Integer.valueOf(addressSet7) + k3) + " = " + String.valueOf(readBufferb4[k4]));
                System.out.println("Address: " + (Integer.valueOf(addressSet8) + k3) + " = " + String.valueOf(readBufferb5[k4]));
                System.out.println("Address: " + (Integer.valueOf(addressSet9) + k3) + " = " + String.valueOf(readBufferb6[k4]));
                System.out.println("Address: " + (Integer.valueOf(addressSet10) + k3) + " = " + String.valueOf(readBufferb7[k4]));
                System.out.println("Address: " + (Integer.valueOf(addressSet11) + k3) + " = " + String.valueOf(readBufferb8[k4]));
                k++;

                k3++;
                k5++;
            } while (k < readBufferb3.length && k4 < readBufferb2.length && k4 < readBufferb.length && k4 < readBufferb1.length);
            // adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, items);
            adapter = new ArrayAdapter<>(getBaseContext(), R.layout.list_txt, items);
            heading = new ArrayAdapter<>(getBaseContext(), R.layout.head_txt, title);
            adapter1 = new ArrayAdapter<>(getBaseContext(), R.layout.list_txt, items1);
            heading1 = new ArrayAdapter<>(getBaseContext(), R.layout.head_txt, title1);
            adapter2 = new ArrayAdapter<>(getBaseContext(), R.layout.list_txt, items2);
            heading2 = new ArrayAdapter<>(getBaseContext(), R.layout.head_txt, title2);
            adapter.notifyDataSetChanged();
            adapter1.notifyDataSetChanged();
            adapter2.notifyDataSetChanged();
            //dataList.setChoiceMode(ListView);
            dataList.setAdapter(adapter);
            dataTitle.setAdapter(heading);
            dataList1.setAdapter(adapter1);
            dataTitle1.setAdapter(heading1);
            dataList2.setAdapter(adapter2);
            dataTitle2.setAdapter(heading2);
            //System.out.println(ListView.CHOICE_MODE_SINGLE);
            System.out.println(adapter);
            System.out.println(adapter1);
            System.out.println(adapter2);
        } else {
            countFail++;
            if (countFail > 5) {
                countFail = 0; //Reset count
            }
        }
    }
    /*
     * Function: Write Text to Internal Storage
     * Input: file name and data (String type) */
    private void writeInternalStorage(String fileName, String data){
        try {
            FileOutputStream fOut = openFileOutput(fileName,MODE_PRIVATE);
            fOut.write(data.getBytes());
            fOut.close();
            //  Toast.makeText(getBaseContext(),"file saved",Toast.LENGTH_SHORT).show();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
    /* Function: Read Text from Internal Storage
     * Input: file name
     * @return: text */
    private String readInternalStorage(String fileName){
        String temp = "";
        try {
            FileInputStream fin = openFileInput(fileName);
            int c;
            while( (c = fin.read()) != -1){
                temp = temp + Character.toString((char)c);
            }
            //   Toast.makeText(getBaseContext(),"file read",Toast.LENGTH_SHORT).show();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return temp;
    }
    public boolean onTouchEvent(MotionEvent touchEvent){
        switch(touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();
                if(x1 > x2){
                    Intent i = new Intent(FirstActivity.this, MainActivity.class);
                    startActivity(i);
                }
                break;
        }
        return false;
    }

}