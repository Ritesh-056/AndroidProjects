package np.com.socialize;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
            recyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));

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

    private static final String TAG = "SocializeFragment";
}
