package np.com.socialize.hobbies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import np.com.socialize.R;
import np.com.socialize.category.CategoryModel;

public class HobbiesAdapterForSocialize extends RecyclerView.Adapter<HobbiesViewHolderForSocialize> {


    ArrayList<CategoryModel> hobbies;

    public HobbiesAdapterForSocialize(ArrayList<CategoryModel> mhobbies) {

         this.hobbies=mhobbies;
    }


    @NonNull
    @Override
    public HobbiesViewHolderForSocialize onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_dashboard_hobbies,parent,false);
        return  new HobbiesViewHolderForSocialize(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HobbiesViewHolderForSocialize holder, int position) {

        holder.binddata(hobbies.get(position));
    }



    @Override
    public int getItemCount() {
        return hobbies.size();
    }
}
