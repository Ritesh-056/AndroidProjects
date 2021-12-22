package np.com.socialize;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;

public class PhoneNumberActivity extends AppCompatActivity {

    private String verifyCodeBySystem;
    Button btn_continue;
    EditText otp_phone;
    private FirebaseAuth mAuth;
    TextView already_otp;
    ImageView lArrow;
    CountryCodePicker country_code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);

       btn_continue = findViewById(R.id.btn_continue);
       otp_phone  = findViewById(R.id.otp_phone);
       already_otp = findViewById(R.id.already_otp);
       lArrow = findViewById(R.id.lArrow);
       country_code = findViewById(R.id.country_code);




        lArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent= new Intent(PhoneNumberActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


       already_otp.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

              // getSupportFragmentManager().beginTransaction().replace(R.id.main, new OtpVerificationFragment()).commit();
               Intent intent= new Intent(PhoneNumberActivity.this, AlreadyOtpActivity.class);
               startActivity(intent);
               finish();
           }
       });


       btn_continue.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {


               String number=otp_phone.getText().toString();
               String phone_number =country_code.getSelectedCountryCode();

               String value="+"+phone_number+number;


               if (otp_phone.getText().toString().length() >0){

                   if(otp_phone.getText().toString().length()==10) {



                      Toast.makeText(PhoneNumberActivity.this, "Check your message for verification of OTP", Toast.LENGTH_SHORT).show();

                       Intent intent=new Intent(getApplicationContext(),OtpVerifyActivity.class);
                       intent.putExtra("phone_number",value);
                       startActivity(intent);

                   }

                   else{

                       otp_phone.setError("Enter the valid phone number");

                   }

               }

               else{

                   otp_phone.setError("Enter the phone number");

               }

           }


       });

    }


}