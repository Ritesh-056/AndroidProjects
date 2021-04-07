package np.com.socialize;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OtpVerifyActivity extends AppCompatActivity {

    private static final String TAG ="OtpVerificationActivity" ;
    Button btn_verify;
    EditText otp_phone;
    private String verifyCodeBySystem;
    private FirebaseAuth mAuth;
    TextView otp_message;
    ImageView icon_completed;
    ProgressBar progress_bar;
    ImageView lArrow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verify);
        mAuth=FirebaseAuth.getInstance();

        btn_verify = findViewById(R.id.btn_verify);
        otp_phone = findViewById(R.id.otp_phone);
        otp_message = findViewById(R.id.otp_message);
        icon_completed = findViewById(R.id.icon_completed);
        progress_bar = findViewById(R.id.progress_bar);
        lArrow = findViewById(R.id.lArrow);


       lArrow.setVisibility(View.INVISIBLE);

        String phone_number = getIntent().getStringExtra("phone_number");
        Log.d(TAG, "onCreate: "+phone_number);

        sendVerificationToUser(phone_number);

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress_bar.setVisibility(View.INVISIBLE);


                String code = otp_phone.getEditableText().toString();


                if (code.length() == 0){

                    otp_phone.setError("please insert the OTP");
                   return;
                }

                if(code.length() != 6){
                    otp_phone.setError("inserted input size otp is not matched");
                }



                verifyCode(code);

            }
        });


        lArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(OtpVerifyActivity.this,PhoneNumberActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }




    private void sendVerificationToUser(String number) {


        Log.d(TAG, "sendVerificationToUser: "+number);
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks// ForceResendingToken from callbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

             verifyCodeBySystem = s;


            SharedPreferences.Editor editor = getSharedPreferences("MY_PHONE", MODE_PRIVATE).edit();
            editor.putString("code", s);
            editor.apply();


            btn_verify.setVisibility(View.VISIBLE);
             otp_message.setText("OTP send ");
             icon_completed.setVisibility(View.VISIBLE);
             progress_bar.setVisibility(View.INVISIBLE);
            Log.d(TAG, "onCodeSent: "+s);



        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {



             String code = phoneAuthCredential.getSmsCode();
             if (code != null){

                 verifyCode(code);
             }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            e.printStackTrace();

            Toast.makeText(OtpVerifyActivity.this, ""+e, Toast.LENGTH_SHORT).show();
        }
    };


    private  void verifyCode(String verficationCode){

        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verifyCodeBySystem,verficationCode);
        signInUserCredential(phoneAuthCredential);
    }

    private void signInUserCredential(PhoneAuthCredential phoneAuthCredential) {

      progress_bar.setVisibility(View.VISIBLE);

        mAuth=FirebaseAuth.getInstance();
        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(OtpVerifyActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                      if (task.isSuccessful()){


                          Toast.makeText(OtpVerifyActivity.this, "Verification Completed", Toast.LENGTH_SHORT).show();

                          Intent intent = new Intent(OtpVerifyActivity.this,MainActivity.class);
                          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                          startActivity(intent);
                          finish();
                      }

                      else{
                       //   Toast.makeText(OtpVerifyActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                          progress_bar.setVisibility(View.INVISIBLE);
                          otp_phone.setError("Inserted Otp is wrong");
                          lArrow.setVisibility(View.VISIBLE);
                          Toast.makeText(OtpVerifyActivity.this, "Otp Authentication Failed", Toast.LENGTH_SHORT).show();
                      }


                    }
                });

    }





}