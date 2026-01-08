package com.example.sosyalsorumluluk;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class AddAnimalFragment extends Fragment {

    private static final int IMAGE_PICK_REQUEST = 1;

    private DatabaseHelper dbHelper;
    private Uri imageUri;
    private ImageView animalImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_animal, container, false);

        dbHelper = new DatabaseHelper(getContext());

        EditText animalTypeInput = view.findViewById(R.id.animalTypeInput);
        EditText locationInput = view.findViewById(R.id.locationInput);
        animalImageView = view.findViewById(R.id.animalImageView);
        Button selectImageButton = view.findViewById(R.id.selectImageButton);
        Button addAnimalButton = view.findViewById(R.id.addAnimalButton);

        selectImageButton.setOnClickListener(v -> selectImage());

        addAnimalButton.setOnClickListener(v -> {
            String animalType = animalTypeInput.getText().toString().trim();
            String location = locationInput.getText().toString().trim();

            if (animalType.isEmpty() || location.isEmpty() || imageUri == null) {
                Toast.makeText(getContext(), "Lütfen tüm alanları doldurun ve bir resim seçin!", Toast.LENGTH_SHORT).show();
                return;
            }

            saveAnimal(animalType, location);
        });

        return view;
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICK_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            animalImageView.setImageURI(imageUri);
        }
    }

    private void saveAnimal(String type, String location) {
        try {
            // Convert image URI to byte array
            InputStream inputStream = getContext().getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            
            // Compress bitmap to byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
            byte[] imageBytes = outputStream.toByteArray();
            
            // Save to database
            long result = dbHelper.addAnimal(type, location, imageBytes);
            
            if (result != -1) {
                Toast.makeText(getContext(), "Hayvan eklendi!", Toast.LENGTH_SHORT).show();
                // Clear inputs
                EditText animalTypeInput = getView().findViewById(R.id.animalTypeInput);
                EditText locationInput = getView().findViewById(R.id.locationInput);
                animalTypeInput.setText("");
                locationInput.setText("");
                animalImageView.setImageResource(0);
                imageUri = null;
            } else {
                Toast.makeText(getContext(), "Hata oluştu!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Resim işlenirken hata oluştu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
