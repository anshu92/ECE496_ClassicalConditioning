package com.example.asahoo264.ece496_cc;

/**
 * Created by YanyanZ on 2/10/16.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.StringTokenizer;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    Intent data = new Intent();
    Button _loginButton;
    EditText _nameText, _passwordText;
    TextView _signupLink;

    private static String dir_str = null;
    String fname = "userlist";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        _loginButton = (Button) findViewById(R.id.btn_login);
        _nameText = (EditText) findViewById(R.id.input_name);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _signupLink = (TextView) findViewById(R.id.link_signup);

        _loginButton.setOnClickListener(this);
        _signupLink.setOnClickListener(this);
        dir_str = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_login) {
            login();

        } else if (v.getId() == R.id.link_signup) {
            Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
            startActivityForResult(intent, REQUEST_SIGNUP);
        }

    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.
        File file = new File(dir_str, fname);
        try {
            BufferedReader input = new BufferedReader(new FileReader(file));
            while(true) {
                String line = input.readLine();
                if (line == null) break;
                StringTokenizer st = new StringTokenizer(line);
                while(st.hasMoreTokens()) {
                    String user = st.nextToken();
                    String pass = st.nextToken();
                    Log.d("Token: ", user + " " + pass);
                /*cipher myCipher = new cipher();
                byte[] decryptedData = null;
                try{
                    decryptedData = myCipher.decrypt(key.getBytes("utf-8"), pass.getBytes("utf-8"));
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                */
                    MD5 enc = new MD5();
                    String encryptedData = enc.crypt(password);

                    String fcontent = name + " " + encryptedData;
                    Log.d("Entered: ", fcontent);
                    fcontent = user + " " + pass;
                    Log.d("Stored: ", fcontent);

                    if (user.equals(name)) {
                        if (pass.equals(encryptedData))
                            onLoginSuccess(name);
                        else
                            onLoginFailed();
                    }
                }
            }

        }catch (IOException e){
            e.printStackTrace();
        }

        /*new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        //onLoginSuccess();
                        onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);*/
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess(String name) {
        _loginButton.setEnabled(true);
        Bundle conData = new Bundle();
        conData.putString("User: ", name);
        data.putExtras(conData);
        setResult(RESULT_OK, data);
        if (getParent() == null) {
            setResult(Activity.RESULT_OK, data);
        } else {
            getParent().setResult(Activity.RESULT_OK, data);
        }
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}