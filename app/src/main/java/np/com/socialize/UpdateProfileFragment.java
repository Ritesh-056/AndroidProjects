package np.com.socialize;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import np.com.socialize.category.User;
import np.com.socialize.category.UserDataViewModel;

import static android.app.Activity.RESULT_OK;

public class UpdateProfileFragment extends Fragment implements AdapterView.OnItemSelectedListener {


    private static final String TAG = "SettingFragment";
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser currenUser;
    Button btn_save;
    ImageView mHobbies;
    CircleImageView profile_image;
    Uri selectedImageUri=null;
    UserDataViewModel userDataViewModel;
    User mUser;
    String spinnerChooseGender;


    Button  btn_logout,btnEdit;
    AlertDialog.Builder builder1;

    EditText Username;
    EditText mUsername,mNumber;
    TextView mDateOfBirth;
    private static final int REQUEST_CODE_CHOOSE= 23;
    private static  final  int STORAGE_PERMISSION_CODE=1;
    InputStream pictureInputStream;
    MutableLiveData<User> currentUser = new MutableLiveData<>();
    ProgressBar progress_bar;
    Spinner spinnerGender ;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return  inflater.inflate(R.layout.update_profile_fragment,container,false);

    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        userDataViewModel = new ViewModelProvider(getActivity()).get(UserDataViewModel.class);


        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();


        mHobbies=view.findViewById(R.id.mHobbies);
        profile_image =view.findViewById(R.id.profile_image);
        mUsername= view.findViewById(R.id.mUsername);
        mNumber= view.findViewById(R.id.mNumber);
        mDateOfBirth= view.findViewById(R.id.mDateOfBirth);
        btn_save=view.findViewById(R.id.btn_save);
        btn_logout=view.findViewById(R.id.btn_logout);
        progress_bar=view.findViewById(R.id.progress_bar);
        btnEdit = view.findViewById(R.id.btnEdit);
        userDataViewModel.getData();
        spinnerGender = view.findViewById(R.id.spinnerGender);



        Log.d(TAG, "onFragmentSwitched: Successful");


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);
        spinnerGender.setOnItemSelectedListener(this);

        mDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

                ViewGroup viewGroup =v.findViewById(android.R.id.content);
                final View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.date_picker_layout, viewGroup, false);
                alertDialog.setView(dialogView);
                alertDialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        DatePicker datePicker;
                        datePicker=dialogView.findViewById(R.id.datePicker);

                        int mYear =datePicker.getYear();
                        int mMonth = datePicker.getMonth()+1;
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


        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                builder1 = new AlertDialog.Builder(getActivity());


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

                        Toast.makeText(getActivity(), "Logout Successful", Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        requireActivity().finish();
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


        userDataViewModel.getCurrentUser().observe(getActivity(), new Observer<User>() {
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


                    if (user.getDateofBirth() !=null){
                        mDateOfBirth.setText(user.getDateofBirth()); }

                    if(user.getProfile_photo() != null){

                        Picasso
                                .get()
                                .load(user.getProfile_photo())
                                .placeholder(R.drawable.loading)
                                .into(profile_image);
                    }
                }
            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mUsername.getText().toString().length() == 0 &&
                        mNumber.getText().toString().length() == 0 &&
                        mDateOfBirth.getText().toString().length() == 0) {
                        mUsername.setError("please insert username");
                        mNumber.setError("please insert phone number");
                        mDateOfBirth.setError("please insert username");
                } else if (mUsername.getText().toString().length() == 0) {
                    mUsername.setError("please insert username");
                } else if (mNumber.getText().toString().length() == 0) {
                    mNumber.setError("please insert phone number");
                } else if (mDateOfBirth.getText().toString().length() == 0) {
                    mDateOfBirth.setError("please insert username");
                } else if(spinnerChooseGender.length() == 0){
                    Toast.makeText(getActivity(), "Please select gender", Toast.LENGTH_SHORT).show();
                }

                else {

                    if (mUser == null) {

                        mUser = new User();
                    }

                    mUser.setDateofBirth(mDateOfBirth.getText().toString());
                    mUser.setName(mUsername.getText().toString());
                    mUser.setPhoneNumber(mNumber.getText().toString());
                    mUser.setGender(spinnerChooseGender);




                    if (selectedImageUri !=null){


                        uploadImage(selectedImageUri,mUser);

                    }
                    else{


                        userDataViewModel.addData(mUser);


                    }


                    Toast.makeText(getActivity(),"Profile was edited successfully" , Toast.LENGTH_SHORT).show();
                    FragmentTransaction ft1 = getFragmentManager().beginTransaction();
                    ShowProfileFragment showProfileFragment = new ShowProfileFragment();
                    ft1.replace(R.id.fragment, showProfileFragment);
                    ft1.commit();


                }
            }
        });


        mHobbies.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                Intent intent = new Intent(getActivity(), HobbiesItemActivity.class);
                                startActivity(intent);

                            }
                        });






                        profile_image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

//                                   choose(); //Matisse function
//                                   checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,STORAGE_PERMISSION_CODE );
                                choose();


                            }
                            });

        ActivityCompat.requestPermissions(getActivity(),
                new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                        STORAGE_PERMISSION_CODE);

        }



        public void choose() {

            Matisse.from(UpdateProfileFragment.this)
                    .choose(MimeType.ofImage(), false)
                    .countable(false)
                    .maxSelectable(1)
                    .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                    .gridExpectedSize(120)
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                    .thumbnailScale(0.85f)
                    .imageEngine(new MyGlideEngine())
                    .setOnSelectedListener((uriList, pathList) -> {
                        // DO SOMETHING IMMEDIATELY HERE
                        Log.e(TAG, "onSelected: pathList=" + pathList);
                        Toast.makeText(getActivity(), "Clcked path "+pathList+"  size is " +uriList.size(), Toast.LENGTH_SHORT).show();

                    })
                    .showSingleMediaType(true)
                    .originalEnable(true)
                    .maxOriginalSize(30)
                    .autoHideToolbarOnSingleTap(true)
                    .setOnCheckedListener(isChecked -> {
                        //Do something immediately here

                        Log.e(TAG, "onCheck: isChecked=" + isChecked);
                    })
                    .forResult(REQUEST_CODE_CHOOSE);
        }


        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);


            Log.d(TAG, "onActivityResult: Fragment " + requestCode+ ", " + resultCode);
                        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
                            List<Uri> result = Matisse.obtainResult(data);
                            Log.d(TAG, "onActivityResult: " + result.size());

                            if (result.size() >0){

                                Uri uri=result.get(0);
                                UCrop.of(uri, Uri.fromFile(new File(getContext().getCacheDir(), "temp.jpg")))
                                        .withAspectRatio(1,1)
                                        .start(requireContext(),this);
                            }
                        }

                        else  if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
                            final Uri resultUri = UCrop.getOutput(data);

                            profile_image.setImageURI(resultUri);
                            selectedImageUri=resultUri;


                        }


        }

    public void checkPermission(String permission, int requestCode)
    {

        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(
                getActivity(),
                permission)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat
                    .requestPermissions(
                            getActivity(),
                            new String[] { permission },
                            requestCode);
        }
        else {
            Toast
                    .makeText(getActivity(),
                            "Permission already granted",
                            Toast.LENGTH_SHORT)
                    .show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super
                .onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);


        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(),
                        "Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(getActivity(),
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }



    private StorageReference mStorageRef;

    public void uploadImage(Uri uri, User mUser){

        mStorageRef = FirebaseStorage.getInstance().getReference();


        StorageReference riversRef = mStorageRef.child("images/"+mAuth.getUid()+"/profile.jpg");

        riversRef.putFile(uri)
                /*.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content



                    }
                })

                */

                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                mUser.setProfile_photo(uri.toString());
                                userDataViewModel.addData(mUser);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        spinnerChooseGender = parent.getItemAtPosition(position).toString();
        Log.d(TAG, "onItemSelected: "+spinnerChooseGender);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}







