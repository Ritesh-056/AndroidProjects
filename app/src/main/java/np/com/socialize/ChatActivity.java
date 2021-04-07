package np.com.socialize;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.orhanobut.hawk.Hawk;
import com.squareup.picasso.Picasso;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import np.com.socialize.category.ChatAdapter;
import np.com.socialize.category.ChatMessage;
import np.com.socialize.category.OnChatItemClick;
import np.com.socialize.category.User;

public class ChatActivity extends AppCompatActivity {


    private static final int REQUEST_CODE_CHOOSE= 23;
    TextView textHobbies;
    ImageView group_image,send_message,send_image;
    EditText message;
    OnChatItemClick onChatItemClick;

    RelativeLayout rlSend;
    ProgressBar progress_bar;


    FirebaseFirestore db;
    FirebaseAuth auth;

    RecyclerView recyclerView;
    ChatAdapter chatAdapter;

    String server_id;


    User user;

    Map<String, Object> updateMap = new HashMap();


    String type ="categories";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init_component();



        String hobbies_name=getIntent().getStringExtra("hobbies_item");
        textHobbies.setText(hobbies_name);


        if (getIntent().hasExtra("type")){

            type=getIntent().getStringExtra("type");

        }


        server_id=getIntent().getStringExtra("server_id");

        String hobbies_img=getIntent().getStringExtra("hobbies_image");
        Picasso
                .get()
                .load(hobbies_img)
                .placeholder(R.drawable.loading)
                .into(group_image);



        load_messages();
        sendmessage();

        send_photo_message();

        onChatItemClick = new OnChatItemClick() {
            @Override
            public void onItemCliked(ChatMessage chatMessage) {


                Log.d(TAG, "onItemCliked: " +chatMessage.getMessage());

                message.setText(chatMessage.getMessage());
                send_message.setImageDrawable(ContextCompat.getDrawable(ChatActivity.this,R.drawable.b));



              /*  db.collection("categories")
                        .document(server_id)
                        .collection("messages")
                        .document()
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){

                                    Toast.makeText(ChatActivity.this, "Message deleted", Toast.LENGTH_SHORT).show();
                                }else{

                                    Toast.makeText(ChatActivity.this, "Unsuccesful to delete message", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


               */







            }
        };
    }

    private void send_photo_message() {

          send_image.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  Toast.makeText(ChatActivity.this, "Write your logic to send the image as message", Toast.LENGTH_SHORT).show();

                  choose();

              }
          });

    }

    private void load_messages()  {



        db.collection(type)
                .document(server_id)
                .collection("messages")
                .orderBy("messageTime")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        ArrayList<ChatMessage> messageArrayList = new ArrayList<>();


                        try {
                            for (DocumentSnapshot documentSnapshot:value.getDocuments()){

                                ChatMessage chatMessage= documentSnapshot.toObject(ChatMessage.class);
                                messageArrayList.add(chatMessage);
                            }


                            message_recycler(messageArrayList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });

    }

    private void init_component() {

        textHobbies = findViewById(R.id.group_name);
        group_image = findViewById(R.id.group_image);
        send_message = findViewById(R.id.send_message);
        message =  findViewById(R.id.message);
        send_image = findViewById(R.id.send_image);
        progress_bar= findViewById(R.id.progress_bar);
        rlSend = findViewById(R.id.rlSend);


        db = FirebaseFirestore.getInstance();
        auth= FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.recyclerView);


        progress_bar.setVisibility(View.INVISIBLE);


        user = Hawk.get("User");
        auth =FirebaseAuth.getInstance();

    }





    private void sendmessage() {

        send_message.setOnClickListener(new  View.OnClickListener() {

            @Override
            public void onClick(View view) {


                if(!message.getText().toString().isEmpty()){
                    //send

                    addChat(null , message.getText().toString());
                }








            }
        });

    }



    private void message_recycler(ArrayList<ChatMessage> mylist) {

           recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            chatAdapter = new ChatAdapter(mylist,onChatItemClick);
             recyclerView.setAdapter(chatAdapter);
             chatAdapter.notifyDataSetChanged();

             if (mylist.size()>0)
             recyclerView.smoothScrollToPosition(mylist.size()-1);



    }

    private static final String TAG = "ChatActivity";


    public void choose()
    {
        Matisse.from(this)
                .choose(MimeType.ofImage(), false)
                .countable(false)
                .maxSelectable(10)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(120)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new MyGlideEngine())
                .setOnSelectedListener((uriList, pathList) -> {
                    // DO SOMETHING IMMEDIATELY HERE
                    Log.e(TAG, "onSelected: pathList=" + pathList);
                    Toast.makeText(getApplicationContext(), "Clcked path "+pathList+"  size is " +uriList.size(), Toast.LENGTH_SHORT).show();

                })
                .showSingleMediaType(true)
                .originalEnable(true)
                .maxOriginalSize(30)
                .autoHideToolbarOnSingleTap(true)
                .setOnCheckedListener(isChecked -> {
                    //Do something immediately here

                    Log.e(TAG, "onCheck: isChecked=" + isChecked);
                })
                .forResult(REQUEST_CODE_CHOOSE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);




        Log.d(TAG, "onActivityResult: " +requestCode);
        Log.d(TAG, "onActivityResult: " +resultCode);



        if(requestCode == REQUEST_CODE_CHOOSE && resultCode ==  RESULT_OK){

            List<Uri> result = Matisse.obtainResult(data);

            if (result.size() >0){

                Uri uri=result.get(0);

                  uploadImage(uri);

            }


        }else{
            Log.d(TAG, "onActivityResult: lost here");
        }

    }


    private StorageReference mStorageRef;

    public void uploadImage(Uri uri){

        mStorageRef = FirebaseStorage.getInstance().getReference();


        Log.d(TAG, "uploadImage: uploading....");

        progress_bar.setVisibility(View.VISIBLE);
        rlSend.setVisibility(View.INVISIBLE);

        String fileid = UUID.randomUUID().toString();

        StorageReference riversRef = mStorageRef.child("uploads/"+fileid);

        riversRef.putFile(uri)

                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

//                                mUser.setProfile_photo(uri.toString());
//                                userDataViewModel.addData(mUser);
                                Log.d(TAG, "onSuccess: "+uri);

                                addChat(uri.toString(), null );
                              Toast.makeText(ChatActivity.this, "Uploaded Success" , Toast.LENGTH_SHORT).show();

                                progress_bar.setVisibility(View.INVISIBLE);
                                rlSend.setVisibility(View.VISIBLE);
                                Toast.makeText(ChatActivity.this, "Please wait a while to load image", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onfaield: "+e.getMessage());

                                e.printStackTrace();


                                Toast.makeText(ChatActivity.this, "Image Uploading failed", Toast.LENGTH_SHORT).show();
                                progress_bar.setVisibility(View.INVISIBLE);
                                rlSend.setVisibility(View.VISIBLE);

                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...

                        exception.printStackTrace();
                    }
                });


    }






    public void addChat(String path, String msg)
    {


        updateMap.put("messageUser",user.getName());
        updateMap.put("profile",user.getProfile_photo());
        updateMap.put("messageTime", new Date().getTime());
        updateMap.put("senderId",auth.getUid());


        if(msg !=null)
        {


            updateMap.put("message", msg);

            message.setText("");


        }

        if(path !=null )
        {

            updateMap.put("imageMessage",path);

        }




        db.collection(type)
                .document(server_id)
                .collection("messages")
                .add(updateMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(ChatActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                    }
                });

    }

    public void setBackup(String message )
    {
        db.collection("categories")
                .document(server_id)
                .collection("messages")
                .document()
                .update("message",message)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {


                        Toast.makeText(ChatActivity.this, "Updated Successful", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {


                        Toast.makeText(ChatActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }



}