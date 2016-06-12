package com.example.assignment8;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.OutputStream;
import java.net.Socket;
import android.util.Log;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    /** Init Variable for IP page **/
    EditText inputIP;
    Button ipSend;
    String ipAdd = "";

    /** Init Variable for Page 1 **/
    EditText inputNumTxt1;
    EditText inputNumTxt2;

    Button btnAdd;
    Button btnSub;
    Button btnMul;
    Button btnDiv;

    /** Init Variable for Page 2 **/
    TextView textResult;
    Button return_button;

    /** Init Variable **/
    String oper = "";
    String result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_page);
        inputIP = (EditText)findViewById(R.id.edIP);
        ipSend = (Button)findViewById(R.id.sendBtn);
        System.out.println("on create");
        ipSend.setOnClickListener(new Button.OnClickListener() {
            //when click OK! btn
            @Override
            public void onClick(View view) {
                // Func() for activity_main setup
                ipAdd = inputIP.getText().toString();
                System.out.println(ipAdd);
                Thread t = new thread();
                t.start();
                jumpToMainLayout();
            }
        });
    }

    class thread extends Thread{
        public void run(){
            try{
                System.out.println("Client: Waiting to connect...");
                int serverPort = 8000;

                // Create socket connect server
                Socket socket = new Socket(ipAdd, serverPort);
                System.out.println("Connected!");

                // Create stream communicate with server
                OutputStream out = socket.getOutputStream();

                byte[] sendStrByte = new byte[1024];
                System.arraycopy(result.getBytes(), 0, sendStrByte, 0, result.length());
                out.write(sendStrByte);

            }catch (Exception e){
                System.out.println("Error" + e.getMessage());
            }
        }
    }

    /** Function for activity_main setup */
    public void jumpToMainLayout() {
        //Change layout to activity_main
        setContentView(R.layout.activity_main);

        //Find and bind all elements
        inputNumTxt1 = (EditText) findViewById(R.id.edNum1);
        inputNumTxt2 = (EditText) findViewById(R.id.edNum2);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnSub = (Button) findViewById(R.id.btnSub);
        btnMul = (Button) findViewById(R.id.btnMul);
        btnDiv = (Button) findViewById(R.id.btnDiv);

        //Set 4 buttons' listener
        btnAdd.setOnClickListener(this);
        btnSub.setOnClickListener(this);
        btnMul.setOnClickListener(this);
        btnDiv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        float num1 = 0; // Store input num 1
        float num2 = 0; // Store input num 2
        float result = 0; // Store result after calculating

        // check if the fields are empty
        if (TextUtils.isEmpty(inputNumTxt1.getText().toString())
                || TextUtils.isEmpty(inputNumTxt2.getText().toString())) {
            return;
        }

        // read EditText and fill variables with numbers
        num1 = Float.parseFloat(inputNumTxt1.getText().toString());
        num2 = Float.parseFloat(inputNumTxt2.getText().toString());

        //caculate result
        switch (v.getId()) {
            case R.id.btnAdd:
                oper = "+";
                result = num1 + num2;
                break;
            case R.id.btnSub:
                oper = "-";
                result = num1 - num2;
                break;
            case R.id.btnMul:
                oper = "*";
                result = num1 * num2;
                break;
            case R.id.btnDiv:
                oper = "/";
                result = num1 / num2;
                break;
            default:
                break;
        }
        // HINT:Using log.d to check your answer is correct before implement page turning
        Log.d("debug","ANS "+result);
        //Pass the result String to jumpToResultLayout() and show the result at Result view
        jumpToResultLayout(new String(num1 + " " + oper + " " + num2 + " = " + result));
    }

    public void jumpToResultLayout(String resultStr){
        result = resultStr;
        //send result to ip_page, and ip_page will send it to server
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, ip_page.class);
        Bundle bundle =new Bundle();
        // Put the information in bundle
        bundle.putString("result", resultStr);
        intent.putExtras(bundle); // Pass bundle to intent
        // startActivity(intent); //Switch Activity

        //
        setContentView(R.layout.activity_result_page);

        //Bind return_button and textResult form result view
        return_button = (Button) findViewById(R.id.btnReturn);
        textResult = (TextView) findViewById(R.id.txtResult);

        if (textResult != null) {
            //Set the result text
            textResult.setText(resultStr);
        }

        if (return_button != null) {
            //prepare button listener for return button
            return_button.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    jumpToMainLayout();
                }

            });
        }
    }
}

