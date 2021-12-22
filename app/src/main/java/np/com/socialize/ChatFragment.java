package np.com.socialize;

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
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

import np.com.socialize.category.User;
import np.com.socialize.category.UserDataViewModel;

public class ChatFragment extends Fragment {

    UserDataViewModel userDataViewModel ;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userDataViewModel = new ViewModelProvider(getActivity()).get(UserDataViewModel.class);



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return  inflater.inflate(R.layout.chat_fragment,container,false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userDataViewModel.getAllUser().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {

                Log.d(TAG, "onChanged: " +users.size());

            }
        });


        userDataViewModel.fetchAllUser();






        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getContext(), getChildFragmentManager());

        ViewPager viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs =view.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


    }

    private static final String TAG = "ChatFragment";
}