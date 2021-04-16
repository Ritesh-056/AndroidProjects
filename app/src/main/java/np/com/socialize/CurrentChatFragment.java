package np.com.socialize;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import np.com.socialize.category.CurrentChatAdapter;
import np.com.socialize.category.UserDataViewModel;

public class CurrentChatFragment extends Fragment  implements CurrentChatAdapter.CheckedMessageIconInterface {



    UserDataViewModel userDataViewModel;

    RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


      //  model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        userDataViewModel = new ViewModelProvider(getActivity()).get(UserDataViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



         return  inflater.inflate(R.layout.fragment_second,container,false);

    //    return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


       recyclerView = view.findViewById(R.id.recyclerView);
       recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        userDataViewModel.getAllChat().observe(getViewLifecycleOwner(), new Observer<List<PrivateChat>>() {
            @Override
            public void onChanged(List<PrivateChat> users) {

                Log.d(TAG, "onChanged: OnFirstFragment" +users.size());



                CurrentChatAdapter adapter = new CurrentChatAdapter((ArrayList<PrivateChat>) users, CurrentChatFragment.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();



            }
        });



        userDataViewModel.fetchAllChat();


    }

    private static final String TAG = "FirstFragment";





    @Override
    public void OnItemClicked(PrivateChat privateChat) {

        Log.d(TAG, "OnItemClicked: "+privateChat.getPrivate_id());


        Intent intent= new Intent(getContext(), ChatActivity.class);
        intent.putExtra("hobbies_item",privateChat.getSender().getName()+ " ==> " +privateChat.getReceiver().getName());
        intent.putExtra("hobbies_image", "https://picsum.photos/200/300");
        intent.putExtra("server_id",privateChat.getPrivate_id());
        intent.putExtra("type", "privateChat");
        getContext().startActivity(intent);
    }
}
