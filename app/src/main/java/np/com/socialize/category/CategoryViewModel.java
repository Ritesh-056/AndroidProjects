package np.com.socialize.category;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import np.com.socialize.CompleteProfileActivity;


public class CategoryViewModel extends ViewModel {


    FirebaseFirestore db;
    private static final String TAG = "CategoryViewModel";

    MutableLiveData<List<CategoryModel>> categoriesLiveData = new MutableLiveData<>();

    public CategoryViewModel() {
        db=FirebaseFirestore.getInstance();
    }

    public MutableLiveData<List<CategoryModel>> getCategoriesLiveData() {
        return categoriesLiveData;
    }


    /*
    public void realTime_load(){

        db.collection("categories")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                               value.getDocuments();
                    }
                });
    }


   */

    public void load_datas(){

        Log.d(TAG, "load_datas: ");

        db.collection("categories")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        ArrayList<CategoryModel> models= new ArrayList<>();

                        for (DocumentSnapshot snapshot: queryDocumentSnapshots.getDocuments()
                             ) {

                            CategoryModel model = snapshot.toObject(CategoryModel.class);
                            model.setServerId(snapshot.getId());
                            models.add(model);
                        }

                        categoriesLiveData.postValue(models);


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFaliure: ");
                        e.printStackTrace();
                    }
                });




    }





}
