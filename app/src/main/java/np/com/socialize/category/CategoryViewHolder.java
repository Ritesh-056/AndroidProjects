package np.com.socialize.category;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import np.com.socialize.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder {


    TextView txtTitle;
    ImageView imgLogo;
    CheckBox checkBox;
    CategoryModel mcategoryModel;
    ArrayList<String> mList;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    public CategoryViewHolder(@NonNull View itemView, OnItemCheckInterface onItemCheckInterface) {
        super(itemView);
        this.onItemCheckInterface = onItemCheckInterface;


        db=FirebaseFirestore.getInstance();
        mAuth =FirebaseAuth.getInstance();

        txtTitle =itemView.findViewById(R.id.txt_live);
        imgLogo=itemView.findViewById(R.id.img_live_logo);
        checkBox = itemView.findViewById(R.id.checkBox);



        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkBox.isChecked()){


                    onItemCheckInterface.onItemSelected(mcategoryModel);
                }

                else{
                    onItemCheckInterface.onItemDeselected(mcategoryModel);


                }

            }
        });



    }

    OnItemCheckInterface onItemCheckInterface;



    private static final String TAG = "CategoryViewHolder";


    public void bindData(CategoryModel firebaseModel){

           mcategoryModel=firebaseModel;

           txtTitle.setText(firebaseModel.getName());

     Picasso
                .get()
                .load(firebaseModel.getImage())
                .placeholder(R.drawable.loading)
                .into(imgLogo);

     }
}
