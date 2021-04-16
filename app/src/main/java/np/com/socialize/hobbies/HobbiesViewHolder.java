package np.com.socialize.hobbies;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

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
