package com.example.sosyalsorumluluk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AnimalsViewFragment extends Fragment {

    private RecyclerView animalsRecyclerView;
    private DatabaseHelper dbHelper;
    private AnimalsAdapter adapter;
    private ArrayList<Animal> animalsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_animals_view, container, false);

        animalsRecyclerView = view.findViewById(R.id.animalsRecyclerView);
        animalsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new DatabaseHelper(getContext());
        animalsList = new ArrayList<>();
        adapter = new AnimalsAdapter(animalsList, dbHelper);
        animalsRecyclerView.setAdapter(adapter);

        loadAnimals();

        return view;
    }

    private void loadAnimals() {
        animalsList.clear();
        animalsList.addAll(dbHelper.getAllAnimals());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAnimals(); // Refresh when fragment is resumed
    }
}

class AnimalsAdapter extends RecyclerView.Adapter<AnimalsAdapter.AnimalsViewHolder> {

    private ArrayList<Animal> animalsList;
    private DatabaseHelper dbHelper;

    public AnimalsAdapter(ArrayList<Animal> animalsList, DatabaseHelper dbHelper) {
        this.animalsList = animalsList;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public AnimalsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_animal, parent, false);
        return new AnimalsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimalsViewHolder holder, int position) {
        Animal animal = animalsList.get(position);

        holder.typeTextView.setText("Tür: " + animal.getType());
        holder.locationTextView.setText("Yer: " + animal.getLocation());
        holder.visitCountTextView.setText("Ziyaret Sayısı: " + animal.getVisitCount());

        // Load image from byte array
        byte[] imageBytes = animal.getImageBytes();
        if (imageBytes != null && imageBytes.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            holder.animalImageView.setImageBitmap(bitmap);
        } else {
            // Use placeholder if no image
            holder.animalImageView.setImageResource(R.drawable.placeholder_image);
        }

        holder.visitButton.setOnClickListener(v -> {
            long newVisitCount = animal.getVisitCount() + 1;
            if (dbHelper.updateVisitCount(animal.getId(), newVisitCount)) {
                animal.setVisitCount(newVisitCount);
                notifyItemChanged(position);
                Toast.makeText(holder.itemView.getContext(), "Ziyaret sayısı artırıldı!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(holder.itemView.getContext(), "Hata oluştu!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return animalsList.size();
    }

    static class AnimalsViewHolder extends RecyclerView.ViewHolder {
        TextView typeTextView, locationTextView, visitCountTextView;
        ImageView animalImageView;
        Button visitButton;

        public AnimalsViewHolder(@NonNull View itemView) {
            super(itemView);
            typeTextView = itemView.findViewById(R.id.typeTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            visitCountTextView = itemView.findViewById(R.id.visitCountTextView);
            animalImageView = itemView.findViewById(R.id.animalImageView);
            visitButton = itemView.findViewById(R.id.visitButton);
        }
    }
}

class Animal {
    private String id, type, location, imageUrl;
    private Long visitCount;
    private byte[] imageBytes;

    public Animal(String id, String type, String location, Long visitCount, String imageUrl) {
        this.id = id;
        this.type = type;
        this.location = location;
        this.visitCount = visitCount;
        this.imageUrl = imageUrl;
    }

    public Animal(String id, String type, String location, Long visitCount, String imageUrl, byte[] imageBytes) {
        this.id = id;
        this.type = type;
        this.location = location;
        this.visitCount = visitCount;
        this.imageUrl = imageUrl;
        this.imageBytes = imageBytes;
    }

    public String getId() { return id; }
    public String getType() { return type; }
    public String getLocation() { return location; }
    public Long getVisitCount() { return visitCount; }
    public String getImageUrl() { return imageUrl; }
    public byte[] getImageBytes() { return imageBytes; }

    public void setVisitCount(Long visitCount) { this.visitCount = visitCount; }
}
