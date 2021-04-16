package np.com.socialize.category;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import np.com.socialize.PrivateChat;
import np.com.socialize.R;

public class CurrentChatAdapter extends RecyclerView.Adapter<CurrentChatAdapter.FindFriendsViewHolder> {



      ArrayList<PrivateChat> users = new ArrayList<>();
      CheckedMessageIconInterface checkInterface;



    public CurrentChatAdapter(ArrayList<PrivateChat> user , CheckedMessageIconInterface checkedMessageIconInterface) {

        this.users=user;
        this.checkInterface =checkedMessageIconInterface;


    }


    @NonNull
    @Override
    public FindFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_find_friends_list,parent,false);
        return new FindFriendsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FindFriendsViewHolder holder, int position) {


         holder.bind(users.get(position));

    }

    @Override
    public int getItemCount() {
        return users.size();
    }





    public class FindFriendsViewHolder extends RecyclerView.ViewHolder{


        TextView txtName;
        ImageView imgMsgIcon;

        PrivateChat mUser;


        public FindFriendsViewHolder(@NonNull View itemView) {
            super(itemView);


            txtName =itemView.findViewById(R.id.txtName);
            imgMsgIcon=itemView.findViewById(R.id.imgMsgIcon);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if(mUser !=null){

                        checkInterface.OnItemClicked(mUser);

                    }

                }
            });
        }






        public  void bind(PrivateChat user){


            mUser=user;

            txtName.setText(user.getSender().getName()+ "==> "+user.getReceiver().getName());




        }

    }



    public  interface  CheckedMessageIconInterface{

        public  void OnItemClicked(PrivateChat user);
    }



}



