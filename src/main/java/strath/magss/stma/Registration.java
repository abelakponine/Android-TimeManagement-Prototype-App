package strath.magss.stma;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/* Registration Class */
public class Registration extends AppCompatActivity {
    private Intent intent = new Intent();
    /* OnCreate Method to start activitiy */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        final Button loginbtn = findViewById(R.id.loginbtn2);
        final Button regbtn = findViewById(R.id.regbtn2);
        final EditText firstname = findViewById(R.id.firstname);
        final EditText lastname = findViewById(R.id.lastname);
        final EditText email = findViewById(R.id.email);
        final RadioButton male = findViewById(R.id.male);
        final RadioButton female = findViewById(R.id.female);
        final ImageView reg_spinner = findViewById(R.id.login_spinner2);
        final EditText username = findViewById(R.id.username);
        final EditText password = findViewById(R.id.password);
        final TextView loginError = findViewById(R.id.loginError);

        // onClick listener for registration button
        regbtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                String gender = "";
                if (male.isChecked() && !female.isChecked()){
                    gender = "Male";
                }
                if (!male.isChecked() && female.isChecked()){
                    gender = "Female";
                }

                // animate spinner
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
                reg_spinner.startAnimation(animation);

                if (username.getText().length() < 1 || password.getText().length() < 1){
                    loginError.setText("Username or Password Cannot be empty.");
                    loginError.setVisibility(View.VISIBLE);
                }
                else if (password.getText().length() < 8){
                    loginError.setText("Password length must not be less than 8 characters");
                    loginError.setVisibility(View.VISIBLE);
                }
                else {
                    reg_spinner.setVisibility(View.VISIBLE);
                    // Database connection
                    String urlString = "http://strathtime.ml/processor.php";
                    String params = "firstname="+firstname.getText()+"&lastname="+lastname.getText()+
                            "&email="+email.getText()+"&gender="+gender+"&username="+username.getText()+"&password="
                            +password.getText()+"&activity=registration";
                    new DatabaseRequestTask().execute(urlString, params);
                }
            }
        });
        // onclick listener for login button
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class DatabaseRequestTask extends AsyncTask<String, Integer, String> {
        final TextView loginError = findViewById(R.id.loginError);

        @Override
        protected void onPreExecute(){
            loginError.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... string) {

            StringBuilder httpContent = new StringBuilder();
            URL url;
            HttpURLConnection conn = null;

            try {
                url = new URL(string[0]);
                conn = (HttpURLConnection) url.openConnection();
                byte[] data = string[1].getBytes(StandardCharsets.UTF_8);
                int dataLength = data.length;
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(dataLength));
                conn.setInstanceFollowRedirects(false);
                conn.setRequestProperty("charset", "UTF-8");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");

                // send post data
                DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
                outputStream.write(data);
                outputStream.flush();

                // get post response status
                InputStream inputStream = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                while (reader.readLine() != null){
                    httpContent.append(reader.readLine());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (conn != null){
                    conn.disconnect();
                }
            }

            return httpContent.toString().trim();
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            ImageView reg_spinner = findViewById(R.id.login_spinner2);
            reg_spinner.clearAnimation();
            reg_spinner.setVisibility(View.GONE);

            try {
                JSONObject object = new JSONObject(result);
                String status = (String) object.get("status");
                String description = (String) object.get("description");

                if (status.equals("success")){
                    // trigger login success
                    Toast.makeText(getApplicationContext(),"Registration status: "+status, Toast.LENGTH_LONG).show();
                    intent.setClass(getApplicationContext(), ViewAcivity.class);
                    startActivity(intent);
                }
                else {
                    // trigger login
                    Toast.makeText(getApplicationContext(), description, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
