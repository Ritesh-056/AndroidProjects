package np.com.socialize;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class CompletedProfileCategory extends AppCompatActivity {


    ImageView lArrow;
    Button btn_done;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_profile_category);



        lArrow = findViewById(R.id.lArrow);
        btn_done= findViewById(R.id.btn_done);

        lArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent( CompletedProfileCategory.this, CompleteProfileActivity.class);
                startActivity(intent);
                finish();

            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                activity(CompletedProfileCategory.this,SocializeDashboardActivity.class);

            }
        });

    }



    public  void activity(Activity activityTo, Class<SocializeDashboardActivity> activityFrom){


        Intent intent = new Intent( activityTo, activityFrom);
        startActivity(intent);
        finish();

    }
}