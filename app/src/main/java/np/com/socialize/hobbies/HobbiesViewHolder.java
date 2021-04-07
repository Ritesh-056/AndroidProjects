package np.com.socialize.hobbies;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import np.com.socialize.R;
import np.com.socialize.category.CategoryModel;
import np.com.socialize.category.OnItemCheckInterface;
import np.com.socialize.category.User;

public class HobbiesViewHolder  extends RecyclerView.ViewHolder {


    ImageView img_hobbies;
    ImageView image_delete;
    TextView title_hobbies;

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    User user;

 CategoryModel mDatas;


    OnItemCheckInterface onItemCheckInterface;


    public HobbiesViewHolder(@NonNull View itemView,OnItemCheckInterface onItemCheckInterface) {
        super(itemView);


        this.onItemCheckInterface =onItemCheckInterface;

        img_hobbies = itemView.findViewById(R.id.img_hobbies);
        title_hobbies = itemView.findViewById(R.id.title_hobbies);
        image_delete = itemView.findViewById(R.id.image_delete);




        image_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onItemCheckInterface.onItemSelected(mDatas);

            }
        });


    }

        public void binddata(CategoryModel hobbies){

            mDatas = hobbies;

            Picasso
                    .get()
                    .load(hobbies.getImage())
                    .placeholder(R.drawable.loading)
                    .into(img_hobbies);
            title_hobbies.setText(hobbies.getName());

        }


        private static final String TAG = "HobbiesViewHolder";

}
