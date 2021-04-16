package np.com.socialize;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import np.com.socialize.category.CategoryAdapter;
import np.com.socialize.category.CategoryModel;
import np.com.socialize.category.CategoryViewModel;
import np.com.socialize.category.OnItemCheckInterface;

public class CompleteProfileActivity extends AppCompatActivity {

    int success=0;
    ProgressBar progress_bar;
    CategoryViewModel categoryViewModel;
    RecyclerView recyclerView;
    Button btn_save;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    ImageView lArrow;
    OnItemCheckInterface onItemCheckInterface;
    ArrayList<CategoryModel> categoryModels;



    HashMap<String,CategoryModel> selectedMap = new HashMap<>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);

        lArrow = findViewById(R.id.lArrow);

        mAuth=FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

         recyclerView = findViewById(R.id.recyclerView);
         recyclerView.setLayoutManager(new GridLayoutManager(this,2));
         btn_save = findViewById(R.id.btn_save);
        progress_bar= findViewById(R.id.progress_bar);


        progress_bar.setVisibility(View.INVISIBLE);

         lArrow.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {




                 Intent intent= new Intent(CompleteProfileActivity.this,MainActivity.class);
                 startActivity(intent);
                 finish();
             }
         });



         onItemCheckInterface =new OnItemCheckInterface() {
             @Override
             public void onItemSelected(CategoryModel categoryModel) {
                 Log.d(TAG, "onItemSelected: "+categoryModel.getName());

                selectedMap.put(categoryModel.getName(),categoryModel);

             }




             @Override
             public void onItemDeselected(CategoryModel categoryModel) {

                 if (selectedMap.containsKey(categoryModel.getName())){

                     selectedMap.remove(categoryModel.getName());
                 }
                 Log.d(TAG, "onItemDeselected: "+categoryModel.getName());
             }
         };


         btn_save.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 progress_bar.setVisibility(View.VISIBLE);

                 Log.d(TAG, "onClick: "+selectedMap);

                 categoryModels = new ArrayList<>();


                 success =0;

                 for (Map.Entry<String,CategoryModel>   currentmap:
                         selectedMap.entrySet()
                      ) {

                      categoryModels.add(currentmap.getValue());

                      db.collection("users")
                        .document(mAuth.getUid())
                        .collection("hobbies")
                        .document(currentmap.getValue().getServerId())
                              .set(currentmap.getValue(), SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                success++;

                                if (success == selectedMap.size()){

                                    Log.d(TAG, "onSuccess: Added");

                                    progress_bar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(CompleteProfileActivity.this, "Hobbies Added Successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(CompleteProfileActivity.this,CompletedProfileCategory.class);
                                        startActivity(intent);
                                        finish();

                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                e.printStackTrace();
                                progress_bar.setVisibility(View.INVISIBLE);
                                Toast.makeText(CompleteProfileActivity.this, "Hobbies added Failed", Toast.LENGTH_SHORT).show();
                            }
                        });

                 }
             }



         });


          categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
          categoryViewModel.getCategoriesLiveData().observe(this, new Observer<List<CategoryModel>>() {
              @Override
              public void onChanged(List<CategoryModel> categoryModels) {

                  Log.d(TAG, "onChanged:data "+categoryModels.size());

                  progress_bar.setVisibility(View.VISIBLE);

                  CategoryAdapter adapter = new CategoryAdapter((ArrayList<CategoryModel>) categoryModels,onItemCheckInterface);
                  recyclerView.setAdapter(adapter);
                  adapter.notifyDataSetChanged();
                  progress_bar.setVisibility(View.INVISIBLE);

              }
          });
              categoryViewModel.load_datas();


    }


    private static final String TAG = "CompleteProfileActivity";
}