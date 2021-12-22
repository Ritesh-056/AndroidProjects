package np.com.socialize;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import np.com.socialize.category.CategoryModel;
import np.com.socialize.category.User;
import np.com.socialize.category.UserDataViewModel;
import np.com.socialize.hobbies.HobbiesAdapterForSocialize;

public class ShowProfileFragment extends Fragment {


    private static final String TAG = "SettingFragment";
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    Button mHobbies,btn_save;
    CircleImageView profile_image;
    UserDataViewModel userDataViewModel;
    User mUser;


    Button  btn_logout;
    TextView gender;
    Button btnEdit;
    TextView mUsername,mNumber;
    TextView mDateOfBirth;
    ProgressBar progress_bar;
    RecyclerView mHobbiesRecyclerViewShowProfile;
    ArrayList<CategoryModel> mhobbies;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return  inflater.inflate(R.layout.show_profile_fragment,container,false);

    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        userDataViewModel = new ViewModelProvider(getActivity()).get(UserDataViewModel.class);


        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        gender = view.findViewById(R.id.gender);
        mHobbies = view.findViewById(R.id.mHobbies);
        profile_image = view.findViewById(R.id.profile_image);
        mUsername = view.findViewById(R.id.mUsername);
        mNumber = view.findViewById(R.id.mNumber);
        mDateOfBirth = view.findViewById(R.id.mDateOfBirth);
        btn_save = view.findViewById(R.id.btn_save);
        btn_logout = view.findViewById(R.id.btn_logout);
        progress_bar = view.findViewById(R.id.progress_bar);
        btnEdit = view.findViewById(R.id.btnEdit);
        mHobbiesRecyclerViewShowProfile = view.findViewById(R.id.mHobbiesRecyclerViewShowProfile);
        userDataViewModel.getData();


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onFragmentSwitch: Successful");
                FragmentTransaction ft1 = getFragmentManager().beginTransaction();
                UpdateProfileFragment updateProfileFragment = new UpdateProfileFragment();
                ft1.replace(R.id.fragment, updateProfileFragment);
                ft1.commit();


            }
        });


        userDataViewModel.getCurrentUser().observe(getActivity(), new Observer<User>() {
            @Override
            public void onChanged(User user) {

                load_hobbies();


                if (user != null) {

                    mUser = user;

                    if (user.getName() != null) {

                        mUsername.setText(user.getName());

                    }


                    if (user.getPhoneNumber() != null) {

                        mNumber.setText(user.getPhoneNumber());

                    }

                    if (user.getGender() != null) {

                        gender.setText(user.getGender());
                    }


                    if (user.getDateofBirth() != null) {


                        mDateOfBirth.setText(user.getDateofBirth());

                    }

                    if (user.getProfile_photo() != null) {

                        Picasso
                                .get()
                                .load(user.getProfile_photo())
                                .placeholder(R.drawable.loading)
                                .into(profile_image);
                    }


                }
            }
        });
    }


    public  void load_hobbies(){
        if(mAuth.getUid() !=null){
            mHobbiesRecyclerViewShowProfile.setLayoutManager(new LinearLayoutManager(getContext()));
            db.collection("users")
                    .document(mAuth.getUid())
                    .collection("hobbies")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value,
                                            @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.w(TAG, "Listen failed.", e);
                                return;
                            }

                            mhobbies = new ArrayList<>();
                            for (QueryDocumentSnapshot doc : value) {
                                if (doc.get("name") != null) {


                                    CategoryModel categoryModel=doc.toObject(CategoryModel.class);

                                    mhobbies.add(categoryModel);
                                    HobbiesAdapterForSocialize hobbiesAdapter= new HobbiesAdapterForSocialize(mhobbies);
                                    mHobbiesRecyclerViewShowProfile.setAdapter(hobbiesAdapter);
                                    hobbiesAdapter.notifyDataSetChanged();
                                }
                            }
                            Log.d(TAG, "Listed hobbies are: " +mhobbies);
                        }
                    });
        }
        else{
            Toast.makeText(getActivity(), "User id is null", Toast.LENGTH_SHORT).show();
        }
    }

}







