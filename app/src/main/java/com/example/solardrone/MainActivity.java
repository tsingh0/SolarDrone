package com.example.solardrone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {
    private TextView outputText;

    public String message;
    private EditText inputText;
    private Button sendButton1,battery;
    private ImageView imageView;
    TextView lat;
    TextView lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputText = (EditText)findViewById(R.id.editText);
        sendButton1 = (Button)findViewById(R.id.sendButton);
        battery = (Button)findViewById(R.id.battery);
        imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setImageDrawable(null);
       /* clickable.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Send sendcode = new Send();
                message=inputText.getText().toString();
                sendcode.execute();
            }
        });*/
    }

    public void clear(View view){
        imageView.setImageDrawable(null);
    }

    public void displayImage(View view){
        Glide.with(this).load("https://geospatialmedia.s3.amazonaws.com/wp-content/uploads/2019/07/UAVs-help2.jpg").into(imageView);
    }

    public void askForBattery(View view) {
        Send sendCode = new Send();
        message="Battery";
        sendCode.execute();
    }


    public void sendToServer(View view) {
        Send sendCode = new Send();
        message=inputText.getText().toString();
        sendCode.execute();
    }

    public class Send extends AsyncTask<Void,Void,Void>{
        Socket s;
        PrintWriter pw;
        @Override
        protected  Void doInBackground(Void...params){
            try {
                //changed below from 192.168.56.1 to 127.0.0.1
                s = new Socket("192.168.56.1",8888);
                pw =new PrintWriter(s.getOutputStream());
                pw.write(message);
                pw.flush();

                InputStreamReader in = new InputStreamReader(s.getInputStream());
                BufferedReader br = new BufferedReader(in);
                String str = br.readLine();
                outputText = (TextView) findViewById(R.id.textOutput);
                outputText.append(str+"\n");


                System.out.println(str);

                pw.close();
                s.close();
            }catch(UnknownHostException e) {
                System.out.println("Fail");
                e.printStackTrace();
            }catch(IOException e){
                System.out.println("Fail");
                e.printStackTrace();
            }
            return null;
        }
    }
    public void showLocation(View v){
        lat =(TextView) findViewById(R.id.LatitudeVal);
        lon = (TextView) findViewById(R.id.LongitudeVal);

        float latVal = Float.parseFloat(lat.getText().toString());
        float lonVal = Float.parseFloat(lon.getText().toString());
        Intent intent = new Intent(this,DroneLocationActivity.class);
        intent.putExtra("latitude",latVal);
        intent.putExtra("longitude",lonVal);
        startActivity(intent);
    }
}