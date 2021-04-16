package np.com.socialize;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import np.com.socialize.category.CategoryModel;
import np.com.socialize.category.OnItemCheckInterface;
import np.com.socialize.hobbies.HobbiesAdapter;

public class HobbiesItemActivity extends AppCompatActivity  implements OnItemCheckInterface {


     RecyclerView recyclerView;
     FloatingActionButton float_add;
     FirebaseFirestore db;
     FirebaseAuth mAuth;
     ArrayList<CategoryModel>  mhobbies;
     ImageView lArrow;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hobbies_data);

        recyclerView =findViewById(R.id.recyclerView);
        float_add = findViewById(R.id.float_add);
        lArrow = findViewById(R.id.lArrow);
        db =FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        load_data();




        lArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activity(HobbiesItemActivity.this,SocializeDashboardActivity.class);

            }
        });


        float_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                 Intent intent = new Intent(HobbiesItemActivity.this,CompleteProfileActivity.class);
                 startActivity(intent);
                 finish();
            }
        });
    }


    public  void load_data(){


        if (mAuth.getUid() != null){
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

                                    HobbiesAdapter hobbiesAdapter = new HobbiesAdapter(mhobbies,HobbiesItemActivity.this);
                                    recyclerView.setAdapter(hobbiesAdapter);
                                    hobbiesAdapter.notifyDataSetChanged();
                                }
                            }
                            Log.d(TAG, "Listed hobbies are: " +mhobbies);
                        }
                    });

        }
        else{

            Toast.makeText(HobbiesItemActivity.this, "Document is null", Toast.LENGTH_SHORT).show();

        }






    }

    private static final String TAG = "HobbiesItemActivity";

    @Override
    public void onItemSelected(CategoryModel categoryModel) {

        Log.d(TAG, "onItemSelected: "+categoryModel.getServerId());


        if (mAuth.getUid() != null){

              db.collection("users")
                      .document(mAuth.getUid())
                      .collection("hobbies")
                      .document(categoryModel.getServerId())
                      .delete()
                      .addOnSuccessListener(new OnSuccessListener<Void>() {
                          @Override
                          public void onSuccess(Void aVoid) {

                              Toast.makeText(HobbiesItemActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                          }
                      })

                      .addOnFailureListener(new OnFailureListener() {
                          @Override
                          public void onFailure(@NonNull Exception e) {

                              Toast.makeText(HobbiesItemActivity.this, "Deleted unSuccessfully", Toast.LENGTH_SHORT).show();

                          }
                      });
        }

    }

    @Override
    public void onItemDeselected(CategoryModel categoryModel) {

    }


    public  void activity(Activity activityTo, Class<SocializeDashboardActivity> activityFrom){


        Intent intent = new Intent( activityTo, activityFrom);
        startActivity(intent);
        finish();

    }


}
