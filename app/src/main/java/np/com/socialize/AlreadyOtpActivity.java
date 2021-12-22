package np.com.socialize;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class AlreadyOtpActivity extends AppCompatActivity {



    EditText otp_phone;
    Button btn_verify;
    FirebaseAuth mAuth;
    ImageView lArrow;
    ProgressBar progress_bar;
    private static final String TAG = "AlreadyOtpActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.already_have_otp_verify);


        otp_phone = findViewById(R.id.otp_phone);
        btn_verify = findViewById(R.id.btn_verify);
        lArrow =findViewById(R.id.lArrow);
        progress_bar = findViewById(R.id.progress_bar);


        progress_bar.setVisibility(View.INVISIBLE);

        lArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                  Intent intent= new Intent(AlreadyOtpActivity.this,PhoneNumberActivity.class);
                  startActivity(intent);
                  finish();
            }
        });



        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                  String phoneNumber= otp_phone_number.getEditableText().toString();
                  String otpNumber=otp_phone.getEditableText().toString();



                  if(otpNumber.length() == 0)
                  {
                      otp_phone.setError("please insert otp");
                      return;
                  }


                  if (otpNumber.length() !=6)
                  {

                      otp_phone.setError("please insert the valid OTP number");
                      return;
                  }
                  verifyCode(otpNumber);

            }
        });

    }

    private  void verifyCode(String verficationCode){

        progress_bar.setVisibility(View.VISIBLE);

        SharedPreferences prefs = getSharedPreferences("MY_PHONE", MODE_PRIVATE);
        String name = prefs.getString("code","");//"No name defined" is the default value.


        if(name.length() !=0)
        {
            Log.d(TAG, "verifyCode: "+name);

            PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(name,verficationCode);
            signInUserCredential(phoneAuthCredential);

        }

        else {

            Toast.makeText(this, "OTp code is not send", Toast.LENGTH_SHORT).show();
        }

      }

    private void signInUserCredential(PhoneAuthCredential phoneAuthCredential) {
        progress_bar.setVisibility(View.VISIBLE);

        mAuth=FirebaseAuth.getInstance();
        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(AlreadyOtpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){


                            Toast.makeText(AlreadyOtpActivity.this, "Verification Completed", Toast.LENGTH_SHORT).show();

//                            Intent intent = new Intent(AlreadyOtpActivity.this,MainActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);
//                            finish();



                            Intent intent = new Intent(AlreadyOtpActivity.this,SplashScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        }

                        else{
                          //  Toast.makeText(AlreadyOtpActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            otp_phone.setError("Inserted OTP is wrong");
                            Toast.makeText(AlreadyOtpActivity.this, "OTP authentication Failed", Toast.LENGTH_SHORT).show();
                            progress_bar.setVisibility(View.INVISIBLE);

                        }


                    }
                });

    }


}