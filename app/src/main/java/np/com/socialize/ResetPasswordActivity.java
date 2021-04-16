package np.com.socialize;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {


    Button btn_login_forget;
    TextView mEmail;
    String email;
    FirebaseAuth auth;
    ImageView lArrow;
    ProgressBar progress_bar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        auth = FirebaseAuth.getInstance();
        btn_login_forget = findViewById(R.id.btn_login_forget);
        mEmail = findViewById(R.id.mEmail);
        lArrow = findViewById(R.id.lArrow);
         progress_bar = findViewById(R.id.progress_bar);

         progress_bar.setVisibility(View.INVISIBLE);

        lArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              activity(ResetPasswordActivity.this,EmailPassActivity.class);

            }
        });


        btn_login_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progress_bar.setVisibility(View.VISIBLE);



                if(mEmail.getText().toString().length() ==0){

                    mEmail.setError("please insert the email");
                    progress_bar.setVisibility(View.INVISIBLE);
                    return;
                }

                progress_bar.setVisibility(View.VISIBLE);

                   email = mEmail.getText().toString();
                    auth.sendPasswordResetEmail(email)

                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        progress_bar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(ResetPasswordActivity.this, "Check your email to reset the password", Toast.LENGTH_SHORT).show();

                                      activity(ResetPasswordActivity.this,EmailPassActivity.class);

                                    } else {
                                        progress_bar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(ResetPasswordActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

            }
        });

    }



    public  void activity(Activity activityTo, Class<EmailPassActivity> activityFrom){


        Intent intent = new Intent( activityTo, activityFrom);
        startActivity(intent);
        finish();

    }

}