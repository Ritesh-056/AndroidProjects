package np.com.socialize;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;

import np.com.socialize.category.State;
import np.com.socialize.category.User;
import np.com.socialize.category.UserDataViewModel;

public class MainActivity extends AppCompatActivity {

   EditText mUsername,mNumber;
   TextView mDateOfBirth;
   ImageView gMale,gFemale,rArrow;
   Button btn_logout;
   Boolean isMale =  null;
   UserDataViewModel userDataViewModel;
   AlertDialog.Builder builder1;
   User mUser;
   private static final String TAG = "MainActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        userDataViewModel.getData();

        mUsername = findViewById(R.id.mUsername);
        mNumber = findViewById(R.id.mNumber);
        mDateOfBirth = findViewById(R.id.mDateOfBirth);
        gMale = findViewById(R.id.gMale);
        gFemale =  findViewById(R.id.gFemale);
        rArrow = findViewById(R.id.rArrow);
        btn_logout = findViewById(R.id.btn_logout);



      mDateOfBirth.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

              ViewGroup viewGroup = findViewById(android.R.id.content);
              final View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.date_picker_layout, viewGroup, false);
              alertDialog.setView(dialogView);
              alertDialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int id) {

                      DatePicker datePicker;
                      datePicker=dialogView.findViewById(R.id.datePicker);

                      int mYear =datePicker.getYear();
                      int mMonth = datePicker.getMonth();
                      int mDay=datePicker.getDayOfMonth();


                      mDateOfBirth.setText(mDay+"-"+mMonth+"-"+mYear);


                  }
              })
                      .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                          public void onClick(DialogInterface dialog, int id) {
                              dialog.cancel();
                          }
                      });

              alertDialog.show();

          }
      });


        userDataViewModel.getCurrentUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {


                if(user !=null){

                    mUser =user;

                    if ( user.getName() !=null){

                        mUsername.setText(user.getName());

                    }


                    if ( user.getPhoneNumber() !=null){

                        mNumber.setText(user.getPhoneNumber());

                    }


                    if (user.getGender() !=null){

                    }

                    if (user.getDateofBirth() !=null){


                        mDateOfBirth.setText(user.getDateofBirth());

                    }



                }
            }
        });



        userDataViewModel.getCurrentState().observe(this, new Observer<State>() {
            @Override
            public void onChanged(State state) {

                Log.d(TAG, "onChanged: "+state);

                  switch (state){


                      case LOADING:


                          break;
                      case SUCCESS:{

                          Toast.makeText(MainActivity.this, "Selected is " +isMale, Toast.LENGTH_SHORT).show();

                          Intent intent= new Intent(MainActivity.this,CompleteProfileActivity.class);
                          intent.putExtra("add_hobbies",false);
                          startActivity(intent);
                          finish();
                      }
                          break;
                      case FAILED:
                          break;
                  }
            }
        });




        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                builder1 = new AlertDialog.Builder(MainActivity.this);


                final View customLayout
                        = getLayoutInflater()
                        .inflate(
                                R.layout.alert_dialog,
                                null);
               builder1.setView(customLayout);

                final ImageView btn_done =customLayout.findViewById(R.id.btn_done);
                final ImageView btn_cancel =customLayout.findViewById(R.id.btn_cancel);

                btn_done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        FirebaseAuth.getInstance().signOut();


                        Toast.makeText(MainActivity.this, "Logout successfully", Toast.LENGTH_SHORT).show();

                        activity(MainActivity.this, LoginActivity.class);

                    }
                });



                AlertDialog alert1 =builder1.create();


                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     alert1.cancel();

                    }
                });

                alert1.show();
            }
        });

   gMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gMale.setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.selected_drawable));
                gFemale.setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.unselected_drawable));


               isMale=true;
            }




        });

        gFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gFemale.setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.selected_drawable));
                gMale.setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.unselected_drawable));
                isMale=false;


            }
        });






        rArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mUsername.getText().toString().length() == 0 &&
                        mNumber.getText().toString().length() == 0 &&
                        mDateOfBirth.getText().toString().length() == 0) {
                    mUsername.setError("please insert username");
                    mNumber.setError("please insert phone number");
                    mDateOfBirth.setError("please insert username");
                }

                else if(mUsername.getText().toString().length() == 0){
                    mUsername.setError("please insert username");
                }
                else if(mNumber.getText().toString().length() == 0){
                    mNumber.setError("please insert phone number");
                }

                else if(mDateOfBirth.getText().toString().length() == 0){
                    mDateOfBirth.setError("please insert date of birth");
                }

                else if(isMale == null){
                    Toast.makeText(MainActivity.this, "Gender should be selected", Toast.LENGTH_SHORT).show();

                }

                else {

                    if (mUser == null){

                        mUser =new User();
                    }

                    mUser.setGender(mDateOfBirth.getText().toString());
                    mUser.setName(mUsername.getText().toString());
                    mUser.setPhoneNumber(mNumber.getText().toString());
                    mUser.setDateofBirth(mDateOfBirth.getText().toString());

                    String  gender;

                    if(isMale){
                         gender="Male";
                    }else{

                        gender ="Female";
                    }

                    mUser.setGender(gender);



                    userDataViewModel.addData(mUser);



                    Toast.makeText(MainActivity.this, "Selected is " +isMale, Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(MainActivity.this,CompleteProfileActivity.class);
                    startActivity(intent);
                    finish();


                }

            }
        });


    }


    public  void activity(Activity activityTo, Class<LoginActivity> activityFrom){


        Intent intent = new Intent( activityTo, activityFrom);
        startActivity(intent);
        finish();

    }



}







