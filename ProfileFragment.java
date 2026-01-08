package com.example.sosyalsorumluluk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileFragment extends Fragment {

    private TextView tvUserEmail;
    private Button btnResetPassword, btnLogout;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_EMAIL = "user_email";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, getActivity().MODE_PRIVATE);
        String email = sharedPreferences.getString(KEY_EMAIL, null);

        tvUserEmail = view.findViewById(R.id.tvUserEmail);
        btnResetPassword = view.findViewById(R.id.btnResetPassword);
        btnLogout = view.findViewById(R.id.btnLogout);

        // Kullanıcı e-posta adresini göster
        if (email != null) {
            tvUserEmail.setText("E-posta: " + email);
        }

        // Şifre sıfırlama butonuna tıklama olayı (demo için basit mesaj)
        btnResetPassword.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Demo uygulaması: Şifre sıfırlama özelliği mevcut değil", Toast.LENGTH_SHORT).show();
        });

        // Çıkış yap butonuna tıklama olayı
        btnLogout.setOnClickListener(v -> logout());

        return view;
    }

    private void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_EMAIL);
        editor.apply();
        
        Toast.makeText(getActivity(), "Başarıyla çıkış yapıldı", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
