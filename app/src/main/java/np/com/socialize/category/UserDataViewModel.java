package np.com.socialize.category;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import np.com.socialize.PrivateChat;

public class UserDataViewModel extends ViewModel {

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    public UserDataViewModel() {

        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
    }



    MutableLiveData<List<User>> allUser = new MutableLiveData<>();


    public MutableLiveData<List<User>> getAllUser() {
        return allUser;
    }




    MutableLiveData<User> currentUser = new MutableLiveData<>();

    public MutableLiveData<User> getCurrentUser() {
        return currentUser;
    }

    MutableLiveData<State> currentState = new MutableLiveData<>();

    public MutableLiveData<State> getCurrentState() {
        return currentState;
    }



    public void getData() {


        if (mAuth.getUid() != null){

            db.collection("users")
                    .document(mAuth.getUid())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {



                            try {
                                User user= documentSnapshot.toObject(User.class);
                                user.setId(documentSnapshot.getId());
                                Hawk.put("User", user);

                                currentUser.postValue(user);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });



        }



    }

    public void addData(User user) {


        if (mAuth.getUid() != null){

            currentState.postValue(State.LOADING);

            db.collection("users")
                    .document(mAuth.getUid())
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            currentUser.postValue(user);
                            user.setId(mAuth.getUid());
                            Hawk.put("User", user);

                            currentState.postValue(State.SUCCESS);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {


                            currentState.postValue(State.FAILED);
                        }
                    });



        }



    }



    public  void fetchAllUser(){

          db.collection("users")
                  .get()
                  .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                      @Override
                      public void onSuccess(QuerySnapshot queryDocumentSnapshots) {



                          ArrayList<User> users = new ArrayList<>();

                          for (DocumentSnapshot documentSnapshot:queryDocumentSnapshots.getDocuments()){

                              User user= documentSnapshot.toObject(User.class);
                              user.setId(documentSnapshot.getId());  // setting the users id from document.
                              users.add(user);
                          }


                          allUser.postValue(users);


                          Log.d(TAG, "onSuccess: "+queryDocumentSnapshots.getDocuments().size());




                      }
                  })
                  .addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {

                          Log.d(TAG, "onFailure: "+e.getMessage());
                          e.printStackTrace();
                      }
                  });

    }


    public  void createNewChat(User user){

        if (mAuth.getUid() != null){

                    PrivateChat privateChat = new PrivateChat();




                    ArrayList<String> members= new ArrayList<>();


                    members.add(user.getId());
                    members.add(mAuth.getUid());


                    HashMap<String, Boolean> chatMembers = new HashMap<>();

                    chatMembers.put(user.getId(),true);
                    chatMembers.put(mAuth.getUid(), true);

                    privateChat.setChatMembers(chatMembers);

                    User myUser= Hawk.get("User");
                    privateChat.setSender(myUser);
                    privateChat.setReceiver(user);
                    privateChat.setAccepted(false);
                    privateChat.setMembers(members);
                    privateChat.setLastMessage("New Message request");



                    db.collection("privateChat")
                       .add(privateChat)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {

                                Log.d(TAG, "onSuccess: "+documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {


                                Log.d(TAG, "onFailure: "+e.getMessage());
                                e.printStackTrace();
                            }
                        }) ;


        }

    }



    MutableLiveData<List<PrivateChat>> allChat= new MutableLiveData<>();


    public MutableLiveData<List<PrivateChat>> getAllChat() {
        return allChat;
    }



    public  void fetchAllChat(){



        if (mAuth.getUid() == null){
            return;
        }
        db.collection("privateChat")
                .whereArrayContains("members",mAuth.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {



                        ArrayList<PrivateChat> users = new ArrayList<>();

                        for (DocumentSnapshot documentSnapshot:queryDocumentSnapshots.getDocuments()){

                            PrivateChat user= documentSnapshot.toObject(PrivateChat.class);
                            user.setPrivate_id(documentSnapshot.getId());  // setting the users id from document.
                            users.add(user);
                        }


                        allChat.postValue(users);


                        Log.d(TAG, "onSuccess: "+queryDocumentSnapshots.getDocuments().size());




                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d(TAG, "onFailure: "+e.getMessage());
                        e.printStackTrace();
                    }
                });

    }




    private static final String TAG = "UserDataViewModel";
}
