package np.com.socialize;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashScreen extends AppCompatActivity {


    FirebaseAuth auth;
    FirebaseUser mUser;
    FirebaseFirestore mDocument;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        auth =FirebaseAuth.getInstance();
        mUser = auth.getCurrentUser();
       mDocument=FirebaseFirestore.getInstance();

        Thread mythread = new Thread(){


            @Override
            public void run() {

                document_check();
                   }
        };

        mythread.start();


        printHashKey(this);

    }





    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }
    public  void document_check(){


       if(mUser != null){

           DocumentReference docRef = mDocument.collection("users").document(mUser.getUid());
           docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
               @Override
               public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                   if (task.isSuccessful()) {
                       DocumentSnapshot document = task.getResult();
                       if (document != null && document.exists()) {
                           // Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                           Intent intent= new Intent(SplashScreen.this,SocializeDashboardActivity.class);
                           startActivity(intent);
                           finish();

                       } else {
                           // Log.d(TAG, "No such document");

                           Intent intent= new Intent(SplashScreen.this,MainActivity.class);
                           startActivity(intent);
                           finish();
                       }
                   }
               }
           });

       } else{

           Intent intent = new Intent(SplashScreen.this,LoginActivity.class);
           startActivity(intent);
           finish();
       }


    }

    private static final String TAG = "SplashScreen";
}