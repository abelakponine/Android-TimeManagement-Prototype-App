package strath.magss.stma;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/* Login activity (Main activity) Class */
public class MainActivity extends AppCompatActivity {
    private Intent intent = new Intent();

    /* onCreate method to start activity */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_main);

        final Button regbtn = findViewById(R.id.regbtn);
        final Button loginbtn = findViewById(R.id.loginbtn);
        final EditText username = findViewById(R.id.username);
        final EditText password = findViewById(R.id.password);
        final TextView loginError = findViewById(R.id.loginError);
        final ImageView login_spinner = findViewById(R.id.login_spinner);

        // onClick listener for registration button
        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(getApplicationContext(), Registration.class);
                startActivity(intent);
            }
        });

        // onClick listener for login button
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                // animate spinner
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
                login_spinner.startAnimation(animation);

                if (username.getText().length() < 1 || password.getText().length() < 1){
                    loginError.setText("Username or Password Cannot be empty.");
                    loginError.setVisibility(View.VISIBLE);
                }
                else if (password.getText().length() < 8){
                    loginError.setText("Password length must not be less than 8 characters");
                    loginError.setVisibility(View.VISIBLE);
                }
                else {
                    login_spinner.setVisibility(View.VISIBLE);
                    // Database connection
                    String urlString = "http://strathtime.ml/processor.php?username="
                            +username.getText()+"&password="
                            +password.getText()+"&activity=login";

                    new DatabaseRequestTask().execute(urlString);
                }
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
                conn.setRequestMethod("GET");
                conn.setRequestProperty("charset", "UTF-8");
                conn.setDoInput(true);
                conn.setInstanceFollowRedirects(true);
                DataInputStream inputStream = new DataInputStream(conn.getInputStream());
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
            ImageView login_spinner = findViewById(R.id.login_spinner);
            login_spinner.clearAnimation();
            login_spinner.setVisibility(View.GONE);

            try {
                JSONObject object = new JSONObject(result);
                String status = (String) object.get("status");
                String description = (String) object.get("description");

                if (status.equals("success")){
                    // trigger login success
                    Toast.makeText(getApplicationContext(),"Login status: "+status, Toast.LENGTH_LONG).show();
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
