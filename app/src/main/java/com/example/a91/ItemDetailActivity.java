package com.example.a91;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ItemDetailActivity extends AppCompatActivity {

    private ImageView ivDetailImage;
    private TextView tvDetailTypeAndName, tvDetailTimestamp, tvDetailLocation, tvDetailDescription, tvDetailPhone;
    private Button btnRemove;
    private DatabaseHelper dbHelper;
    private LostFoundItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dbHelper = new DatabaseHelper(this);
        item = (LostFoundItem) getIntent().getSerializableExtra("item");

        ivDetailImage = findViewById(R.id.ivDetailImage);
        tvDetailTypeAndName = findViewById(R.id.tvDetailTypeAndName);
        tvDetailTimestamp = findViewById(R.id.tvDetailTimestamp);
        tvDetailLocation = findViewById(R.id.tvDetailLocation);
        tvDetailDescription = findViewById(R.id.tvDetailDescription);
        tvDetailPhone = findViewById(R.id.tvDetailPhone);
        btnRemove = findViewById(R.id.btnRemove);

        if (item != null) {
            tvDetailTypeAndName.setText(getString(R.string.item_format, item.getType(), item.getName()));
            tvDetailLocation.setText(getString(R.string.location_detail_format, item.getLocation()));
            tvDetailDescription.setText(item.getDescription());
            tvDetailPhone.setText(getString(R.string.contact_format, item.getPhone()));
            
            long diff = System.currentTimeMillis() - item.getTimestamp();
            tvDetailTimestamp.setText(getTimeString(diff));

            if (item.getImage() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(item.getImage(), 0, item.getImage().length);
                ivDetailImage.setImageBitmap(bitmap);
            }
        }

        btnRemove.setOnClickListener(v -> {
            if (item != null) {
                dbHelper.deleteItem(item.getId());
                Toast.makeText(this, "Item removed", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private String getTimeString(long diffMillis) {
        long seconds = diffMillis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (days > 0) return getString(R.string.days_ago, (int) days);
        if (hours > 0) return getString(R.string.hours_ago, (int) hours);
        if (minutes > 0) return getString(R.string.minutes_ago, (int) minutes);
        return getString(R.string.just_now);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
