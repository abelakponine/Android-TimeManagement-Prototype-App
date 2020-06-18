package strath.magss.stma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DetailsOfActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_of);
    }

    public void goToEditEvent(View v){
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), EditEventActivity.class);
        startActivity(intent);
    }
}
