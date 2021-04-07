package np.com.socialize.hobbies;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import np.com.socialize.ChatActivity;
import np.com.socialize.R;
import np.com.socialize.category.CategoryModel;

public class HobbiesViewHolderForSocialize extends RecyclerView.ViewHolder {



    ImageView img_hobbies;
    TextView title_hobbies;
    CategoryModel mCategory;


    public HobbiesViewHolderForSocialize(@NonNull View itemView) {
        super(itemView);


        img_hobbies =itemView.findViewById(R.id.img_hobbies);
        title_hobbies =itemView.findViewById(R.id.title_hobbies);




          itemView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {


                  Log.d(TAG, "onClick: "+mCategory.getServerId());

                  Intent intent= new Intent(itemView.getContext(), ChatActivity.class);
                   intent.putExtra("hobbies_item",mCategory.getName());
                   intent.putExtra("hobbies_image",mCategory.getImage());
                   intent.putExtra("server_id",mCategory.getServerId());

                  itemView.getContext().startActivity(intent);

              }
          });
    }

    public  void binddata(CategoryModel hobbies){

          mCategory=hobbies;

        Picasso
                .get()
                .load(hobbies.getImage())
                .placeholder(R.drawable.loading)
                .into(img_hobbies);
                 title_hobbies.setText(hobbies.getName());

    }


    private static final String TAG = "HobbiesViewHolderForSoc";
}
