package com.example.sosyalsorumluluk;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.sosyalsorumluluk.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private DatabaseHelper dbHelper;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_EMAIL = "user_email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        
        dbHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        
        // Create test user if it doesn't exist
        createTestUserIfNeeded();
        
        // Check if user is already logged in
        String savedEmail = sharedPreferences.getString(KEY_EMAIL, null);
        if (savedEmail != null) {
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        }
    }
    
    private void createTestUserIfNeeded() {
        String testEmail = "test@test.com";
        if (!dbHelper.userExists(testEmail)) {
            dbHelper.addUser(testEmail, "123456");
        }
    }
    
    public void btnsignup(View view){
        Intent intent = new Intent(MainActivity.this, SignInMainActivity.class);
        startActivity(intent);
    }
    
    public void btnlogin(View view){
        String email = binding.etMail.getText().toString();
        String password = binding.etPassword.getText().toString();
        
        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(MainActivity.this, "Şifre veya e-posta girilmedi", Toast.LENGTH_SHORT).show();
        } else {
            log_in(email, password);
        }
    }
    
    public void log_in(String email, String password){
        if (dbHelper.checkUser(email, password)) {
            // Save login state
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_EMAIL, email);
            editor.apply();
            
            Toast.makeText(MainActivity.this, "Başarıyla giriş yapıldı", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(MainActivity.this, "Şifre veya e-postanız yanlış", Toast.LENGTH_SHORT).show();
        }
    }
}