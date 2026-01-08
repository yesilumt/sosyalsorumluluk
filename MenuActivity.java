package com.example.sosyalsorumluluk;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import com.example.sosyalsorumluluk.databinding.ActivityMenuBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuActivity extends AppCompatActivity {
    public BottomNavigationView bottomNavigationView;
    private ActivityMenuBinding binding;
    public FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        bottomNavigationView = findViewById(R.id.bottomNavView);
        frameLayout = findViewById(R.id.frame_layout);
        binding.bottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if(itemId==R.id.navhome){
                    loadfragment(new HomeFragment(),false);
                }
                else if(itemId==R.id.navhayvanlar){
                    loadfragment(new AnimalsViewFragment(),false);
                }
                else if(itemId==R.id.navekle){

                    loadfragment(new AddAnimalFragment(),false);
                }
                else if(itemId==R.id.navprofil){
                    loadfragment(new ProfileFragment(),false);
                }

                return true;
            }
        });
        loadfragment(new HomeFragment(),true);
    }
    public void loadfragment(Fragment fragment, boolean isAppInitalizer){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(isAppInitalizer){
            fragmentTransaction.add(R.id.frame_layout,fragment);
        }

        else {
            fragmentTransaction.replace(R.id.frame_layout,fragment);
        }

        fragmentTransaction.commit();


    }
}