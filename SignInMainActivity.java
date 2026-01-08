package com.example.sosyalsorumluluk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.sosyalsorumluluk.databinding.SignInMainBinding;

public class SignInMainActivity extends AppCompatActivity {
    private SignInMainBinding binding;
    private DatabaseHelper dbHelper;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_EMAIL = "user_email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SignInMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        dbHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
    }

    public void signuponclick(View view) {
        String email = binding.etEmail.getText().toString();
        String password = binding.etPassword.getText().toString();
        String confirmPassword = binding.etConfirmPassword.getText().toString();
        
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(SignInMainActivity.this, "Şifre veya e-posta girilmedi", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 6) {
            Toast.makeText(SignInMainActivity.this, "Şifre minimum 6 karakter olmalıdır", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(SignInMainActivity.this, "Girdiğiniz şifreler aynı değil", Toast.LENGTH_SHORT).show();
        } else {
            create_user(email, password);
        }
    }
    
    public void create_user(String email, String password){
        if (dbHelper.userExists(email)) {
            Toast.makeText(SignInMainActivity.this, "Bu e-posta adresi zaten kayıtlı", Toast.LENGTH_SHORT).show();
            return;
        }
        
        long result = dbHelper.addUser(email, password);
        if (result != -1) {
            // Save login state
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_EMAIL, email);
            editor.apply();
            
            Toast.makeText(SignInMainActivity.this, "Başarıyla kaydedildi", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignInMainActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(SignInMainActivity.this, "Kaydedilemedi, Yeniden deneyiniz", Toast.LENGTH_SHORT).show();
        }
    }
}
