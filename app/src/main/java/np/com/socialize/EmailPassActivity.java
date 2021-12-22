package np.com.socialize;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EmailPassActivity extends AppCompatActivity {


    Button  btn_login,btn_sign_up;
    ImageView lArrow;
    EditText  mPassword,mEmail;
    private FirebaseAuth auth;
    private String email,password;
    private static final String TAG = "EmailActivity.class";
    TextView  forget_password;
    FirebaseUser user;
    ProgressBar progress_bar;
    FirebaseFirestore mDocument;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_pass);

        progress_bar = findViewById(R.id.progress_bar);
        btn_login= findViewById(R.id.btn_login);
        btn_sign_up = findViewById(R.id.btn_sign_up);
        mEmail = findViewById(R.id.mEmail);
        mPassword = findViewById(R.id.mPassword);
        forget_password = findViewById(R.id. forget_password);

        lArrow = findViewById(R.id.lArrow);



         auth = FirebaseAuth.getInstance();
         user = auth.getCurrentUser();
         mDocument =FirebaseFirestore.getInstance();


         progress_bar.setVisibility(View.INVISIBLE);

         lArrow.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {


                 Intent intent= new Intent(EmailPassActivity.this,LoginActivity.class);
                 startActivity( intent);
                 finish();

             }
         });



        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



               Intent intent= new Intent(EmailPassActivity.this,ResetPasswordActivity.class);
               startActivity( intent);
               finish();

            }
        });





        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progress_bar.setVisibility(View.VISIBLE);


               email = mEmail.getEditableText().toString();
               password = mPassword.getEditableText().toString();


                if (email.length() ==0 ){

                    mEmail.setError("please insert email to login");
                    progress_bar.setVisibility(View.INVISIBLE);
                    return;
                }

                if(password.length() ==0){

                    mPassword.setError("please insert the password");
                    progress_bar.setVisibility(View.INVISIBLE);
                   return;
                }



                auth.createUserWithEmailAndPassword(email,password)
                       .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                           @Override
                           public void onSuccess(AuthResult authRsult) {
                               FirebaseUser user = authRsult.getUser();
                               if(! user.isEmailVerified()){

                                   user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                       @Override
                                       public void onSuccess(Void aVoid) {

                                           progress_bar.setVisibility(View.INVISIBLE);
                                           Toast.makeText(EmailPassActivity.this, "Check your mail to authenticate to the app", Toast.LENGTH_SHORT).show();

                                       }
                                   });
                               }
                           }
                       }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       e.printStackTrace();
                       progress_bar.setVisibility(View.INVISIBLE);
                       Toast.makeText(EmailPassActivity.this, "Authentication failed.",
                               Toast.LENGTH_SHORT).show();
                       updateUI(null);
                   }
               });

            }
        });




        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progress_bar.setVisibility(View.VISIBLE);

                email = mEmail.getEditableText().toString();
                password = mPassword.getEditableText().toString();



                if (email.length() ==0 ){

                    mEmail.setError("please insert email to login");
                    progress_bar.setVisibility(View.INVISIBLE);
                }

                if(password.length() ==0){

                    mPassword.setError("please insert the password");
                    progress_bar.setVisibility(View.INVISIBLE);

                }


                else{

                    auth.signInWithEmailAndPassword(email,password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    FirebaseUser user = authResult.getUser();
                                    updateUI(user);


                                    Toast.makeText(EmailPassActivity.this, "Login Successfully.", Toast.LENGTH_SHORT).show();


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    e.printStackTrace();
                                    progress_bar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(EmailPassActivity.this, "Email password don't match.", Toast.LENGTH_SHORT).show();


                                }
                            });


                }



            }
        });



    }



    private  void updateUI(FirebaseUser user){

        if(user!= null && user.isEmailVerified()){

            //activity(EmailPassActivity.this,MainActivity.class);

             document_check(user);

        }
    }



        // Construct the email link credential from the current URL.




    public  void document_check(FirebaseUser mUser){



        DocumentReference docRef = mDocument.collection("users").document(mUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        // Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        Intent intent= new Intent(EmailPassActivity.this,SocializeDashboardActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        // Log.d(TAG, "No such document");

                        Intent intent= new Intent(EmailPassActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });



    }
    public  void activity(Activity activityTo, Class<MainActivity> activityFrom){


        Intent intent = new Intent( activityTo, activityFrom);
        startActivity(intent);
        finish();

    }

}