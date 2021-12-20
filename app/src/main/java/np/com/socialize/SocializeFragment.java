package np.com.socialize;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import np.com.socialize.category.CategoryModel;
import np.com.socialize.hobbies.HobbiesAdapterForSocialize;

public class SocializeFragment  extends Fragment {


    RecyclerView recyclerView1;
    ArrayList<CategoryModel> mhobbies;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    Button btn_logout;
    AlertDialog.Builder builder1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_socialize_fragment,container,false);


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


            mAuth=FirebaseAuth.getInstance();
            db=FirebaseFirestore.getInstance();
            recyclerView1 =view.findViewById(R.id.recyclerView1);
            btn_logout = view.findViewById(R.id.btn_logout);
            recyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));


           onClickLogoutButton();
           load_hobbies();
    }

    public  void load_hobbies(){


        if(mAuth.getUid() !=null){


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
                                    recyclerView1.setAdapter(hobbiesAdapter);
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


   public void onClickLogoutButton(){
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
   }

    private static final String TAG = "SocializeFragment";
}
