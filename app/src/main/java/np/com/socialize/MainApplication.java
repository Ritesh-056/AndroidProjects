package np.com.socialize;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.orhanobut.hawk.Hawk;

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
