package np.com.socialize;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import np.com.socialize.category.FindFriendsAdapter;
import np.com.socialize.category.User;
import np.com.socialize.category.UserDataViewModel;

public class FindFriendFragment extends Fragment  implements FindFriendsAdapter.CheckedMessageIconInterface {



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

    //    return super.onCreateViewg(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


       recyclerView = view.findViewById(R.id.recyclerView);
       recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



        userDataViewModel.getAllUser().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {

                Log.d(TAG, "onChanged: OnFirstFragment" +users.size());



                FindFriendsAdapter  adapter = new FindFriendsAdapter((ArrayList<User>) users, FindFriendFragment.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();



            }
        });




    }


    private static final String TAG = "FirstFragment";



    @Override
    public void OnItemClicked(User user) {


        //check if alreay got the chat with this user, open chat acitivity
        //esle create new chat


        String receiverId = user.getId();
        String senderId = FirebaseAuth.getInstance().getUid();




//
//
//        CollectionReference reference = FirebaseFirestore.getInstance().collection("privateChat");
//
//
//        reference.whereIn("members", Arrays.asList(receiverId,senderId));
//
//        reference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//
//                Log.d(TAG, "onSuccess: reverse:" + queryDocumentSnapshots.getDocuments().size());
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//                Log.d(TAG, "onFailure: ");
//            }
//        });



        userDataViewModel.createNewChat(user);

    }
}
