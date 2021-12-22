package np.com.socialize;

import android.app.Application;

import com.facebook.FacebookActivity;
import com.facebook.appevents.AppEvent;
import com.google.firebase.FirebaseApp;
import com.orhanobut.hawk.Hawk;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;



public class MainApplication extends Application {

    public MainApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        Hawk.init(this).build();

    }
}
