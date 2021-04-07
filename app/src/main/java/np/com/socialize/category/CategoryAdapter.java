package np.com.socialize.category;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import np.com.socialize.R;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    ArrayList<CategoryModel> firebaseModel;
    OnItemCheckInterface onItemCheckInterface;


    public CategoryAdapter(ArrayList<CategoryModel> mylist, OnItemCheckInterface onItemCheckInterface) {

        this.firebaseModel = mylist;
        this.onItemCheckInterface = onItemCheckInterface;

    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_category,parent,false);
        return new CategoryViewHolder(view,onItemCheckInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

           holder.bindData(firebaseModel.get(position));
    }

    @Override
    public int getItemCount() {
        return firebaseModel.size();
    }
}
