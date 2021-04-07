package np.com.socialize.hobbies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import np.com.socialize.R;
import np.com.socialize.category.CategoryModel;
import np.com.socialize.category.OnItemCheckInterface;

public class HobbiesAdapter extends RecyclerView.Adapter<HobbiesViewHolder> {


    ArrayList<CategoryModel> hobbies;
    
    OnItemCheckInterface onItemCheckInterface;

    public HobbiesAdapter(ArrayList<CategoryModel> mhobbies,OnItemCheckInterface onItemCheckInterface) {

        this.hobbies = mhobbies;
        this.onItemCheckInterface=onItemCheckInterface;
    }

    @NonNull
    @Override
    public HobbiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_hobbies_list,parent,false);
        return new HobbiesViewHolder(view,onItemCheckInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull HobbiesViewHolder holder, int position) {

          holder.binddata(hobbies.get(position));
    }

    @Override
    public int getItemCount() {
        return hobbies.size();
    }
}
