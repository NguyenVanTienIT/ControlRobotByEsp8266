package com.example.asus.controlrobotbyesp;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

public class Main2Activity extends AppCompatActivity {
    //UI Elements


    Button btnTop, btnLeft, btnRight, btnDown,btnStop,btnGet,btnGet2;
    TextView txtNhietDo, txtDoAm;
    ImageView imageView;
    FrameLayout frameLayout;
    FrameLayout frameLayout2;


    public static String wifiModuleIP = "";
    public static int wifiModulePort = 0;
    public static String wifiModuleUrl;
    public static String ESPIP = "0";
    private WebView webView;


    Boolean send = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();
        wifiModuleIP = intent.getStringExtra("IP");
        wifiModulePort = intent.getIntExtra("PORT", 100);
        wifiModuleUrl = intent.getStringExtra("CAMERA");



        btnTop = (Button) findViewById(R.id.btnLED1);
        btnLeft = (Button) findViewById(R.id.btnLED2);
        btnRight = (Button) findViewById(R.id.btnLED3);
        btnDown = (Button) findViewById(R.id.btnLED4);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnGet = (Button) findViewById(R.id.getInfo);
        btnGet2 = (Button) findViewById(R.id.get);

        txtNhietDo = findViewById(R.id.nhietDo);
        txtDoAm = findViewById(R.id.doAm);

        /*btnTop.setBackgroundColor(Color.GRAY);
        btnLeft.setBackgroundColor(Color.GRAY);
        btnRight.setBackgroundColor(Color.GRAY);
        btnDown.setBackgroundColor(Color.GRAY);
        btnStop.setBackgroundColor(Color.GRAY);*/


        imageView = (ImageView) findViewById(R.id.expanLayout);
        frameLayout = findViewById(R.id.frameLayout);
        frameLayout2 = findViewById(R.id.frameLayout2);

// view cameraip

        webView=(WebView) findViewById(R.id.WebView1 );
        webView.setWebViewClient(new MyBroswer());
        //String url = "http://192.168.43.197:8888/";
        String url = "http://"+wifiModuleUrl+"/";
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);

        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(url);



        // xet su kien nhan giu nut


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(frameLayout.getVisibility() == View.VISIBLE){
                    imageView.setImageResource(R.drawable.ic_expand_less);
                    frameLayout2.setVisibility(View.VISIBLE);
                    frameLayout.setVisibility(View.GONE);
                    return;
                }
                if(frameLayout.getVisibility() == View.GONE){
                    imageView.setImageResource(R.drawable.ic_expand_more);
                    frameLayout2.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.VISIBLE);
                    return;
                }
            }
        });



        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ESPIP = "5";
                new ConnectToServerTask().execute();
            }
        });

        btnGet2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ConnectToListenerServerTask().execute();
            }
        });


        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ESPIP = "0";
                new ConnectToServerTask().execute();
            }
        });


        btnTop.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        ESPIP = "1";
                        btnTop.setBackgroundColor(Color.RED);
                        new ConnectToServerTask().execute();
                        return false;
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        ESPIP = "0";
                        btnTop.setBackgroundColor(Color.GRAY);
                        new ConnectToServerTask().execute();
                        return true;
                    }
                    return false;
                }

        });

        btnLeft.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    ESPIP = "2";
                    btnLeft.setBackgroundColor(Color.RED);
                    new ConnectToServerTask().execute();
                    return false;
                }

                if(event.getAction() == MotionEvent.ACTION_UP){
                    ESPIP = "0";
                    btnLeft.setBackgroundColor(Color.GRAY);
                    new ConnectToServerTask().execute();
                    return  true;
                }

                return false;
            }
        });

        btnRight.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    ESPIP = "3";
                    btnRight.setBackgroundColor(Color.RED);
                    new ConnectToServerTask().execute();
                    return false;
                }

                if(event.getAction() == MotionEvent.ACTION_UP){
                    ESPIP = "0";
                    btnRight.setBackgroundColor(Color.GRAY);
                    new ConnectToServerTask().execute();
                    return  true;
                }

                return false;
            }
        });

        btnDown.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    ESPIP = "4";
                    btnDown.setBackgroundColor(Color.RED);
                    new ConnectToServerTask().execute();
                    return false;
                }

                if(event.getAction() == MotionEvent.ACTION_UP){
                    ESPIP = "0";
                    btnDown.setBackgroundColor(Color.GRAY);
                    new ConnectToServerTask().execute();
                    return  true;
                }

                return false;
            }
        });
    }


    public class ConnectToServerTask extends AsyncTask<Void, Void, Boolean> {
        Socket socket;
        DataInputStream dataInputStream;
        DataOutputStream dataOutputStream;


        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                socket = new Socket(wifiModuleIP, wifiModulePort);
                if (socket.isConnected()) {
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataInputStream = new DataInputStream(socket.getInputStream());
                }
            }
            catch (IOException e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean connnected) {

            if(connnected){
                new Socket_AsyncTask().execute();
            }
            else{
                Toast.makeText(getApplicationContext(),"Lỗi kết nối vui lòng xem lại đường dây",Toast.LENGTH_SHORT).show();
            }
        }


        public class Socket_AsyncTask extends AsyncTask<Void, Void, Boolean>
        {
            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    dataOutputStream.writeBytes(ESPIP);
                    dataOutputStream.close();
                    socket.close();
                    return true;
                }
                catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplication(),"Loi",Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            @Override
            protected void onPostExecute(Boolean connnected) {

                if(connnected){

                }
            }
        }

    }


    public class ConnectToListenerServerTask extends AsyncTask<Void, Void, Boolean> {
        Socket socket;
        DataInputStream dataInputStream;
        DataOutputStream dataOutputStream;


        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                socket = new Socket(wifiModuleIP, wifiModulePort);
                if (socket.isConnected()) {
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataInputStream = new DataInputStream(socket.getInputStream());
                }
            } catch (IOException e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean connnected) {

            if (connnected) {
                new ServerListenner().execute();
            } else {
                Toast.makeText(getApplicationContext(), "Lỗi kết nối vui lòng xem lại đường dây", Toast.LENGTH_SHORT).show();
            }
        }



        public class ServerListenner extends AsyncTask<Void, String, Void>{

            @Override
            protected Void doInBackground(Void... params) {

                try {
                    dataInputStream = new DataInputStream(socket.getInputStream());

                    String messenger = dataInputStream.readLine();
                    String Data = "";

                    while (messenger != null){
                        Data = Data + messenger;
                        messenger = dataInputStream.readLine();
                    }
                    socket.close();
                    dataInputStream.close();
                    Toast.makeText(getApplicationContext(),Data,Toast.LENGTH_SHORT).show();

                    return  null;
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplication(),"Loi",Toast.LENGTH_SHORT).show();
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                Toast.makeText(getApplication(),"Loi",Toast.LENGTH_SHORT).show();
            }

        }

    }



  /*  public class ServerListenner extends AsyncTask<Void, String, Void>{
        Socket socket;
        BufferedReader in;
        //DataInputStream dataInputStream;
        //String nhietDo_doAm = "null";
        @Override
        protected Void doInBackground(Void... params) {

            InetAddress inetAddress = null;
            try {
                inetAddress = InetAddress.getByName(MainActivity.wifiModuleIP);
                socket = new java.net.Socket(inetAddress, MainActivity.wifiModulePort);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String messenge = null;
                messenge = in.readLine();

                while (messenge != null){
                    publishProgress(messenge);
                    messenge = in.readLine();
                }


                //dataInputStream = new DataInputStream(socket.getInputStream());
                //nhietDo_doAm = dataInputStream.readUTF();
                //dataInputStream.close();
                in.close();
                socket.close();
                //publishProgress(nhietDo_doAm);
                return  null;
            }
            catch (UnknownHostException e) {
                e.printStackTrace();
                Toast.makeText(getApplication(),"Loi",Toast.LENGTH_SHORT).show();
            }
            catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplication(),"Loi",Toast.LENGTH_SHORT).show();
            }

            try {
                in.close();
                //dataInputStream.close();
                socket.close();
            }
            catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplication(),"Loi",Toast.LENGTH_SHORT).show();
            }


            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
           // if(nhietDo_doAm != null)  txtNhietdo.setText(nhietDo_doAm);
           // Toast.makeText(getApplicationContext(),nhietDo_doAm,Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
          //  Toast.makeText(getApplicationContext(),,Toast.LENGTH_SHORT).show();
        }
    }*/

    private class MyBroswer extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url){
            view.loadUrl(url);
            return true;
        }
    }




}
