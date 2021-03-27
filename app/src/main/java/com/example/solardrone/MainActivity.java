package com.example.solardrone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {
    private TextView outputText;
    public String message;
    TextView lat;
    TextView lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText inputText = (EditText)findViewById(R.id.editText);
        final Button clickable = (Button)findViewById(R.id.button);
        clickable.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                send sendcode = new send();
                message=inputText.getText().toString();
                sendcode.execute();
            }
        });
    }

    class send extends AsyncTask<Void,Void,Void>{
        Socket s;
        PrintWriter pw;
        @Override
        protected  Void doInBackground(Void...params){
            try {
                //changed below from 192.168.56.1 to 127.0.0.1
                s = new Socket("192.168.1.30",8888);
                pw =new PrintWriter(s.getOutputStream());
                pw.write(message);
                pw.flush();

                InputStreamReader in = new InputStreamReader(s.getInputStream());
                BufferedReader br = new BufferedReader(in);
                String str = br.readLine();
                outputText = (TextView) findViewById(R.id.textOutput);
                outputText.setText(str);

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