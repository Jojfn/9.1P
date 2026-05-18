package com.example.a91;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationTokenSource;

import java.util.ArrayList;
import java.util.List;

public class ShowItemsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewItems;
    private ItemsAdapter adapter;
    private DatabaseHelper dbHelper;
    private Spinner spinnerFilter;
    private EditText etSearch, etRadius;
    private Button btnApplyRadius;
    private List<LostFoundItem> itemList;
    private FusedLocationProviderClient fusedLocationClient;
    private Location userLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_items);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dbHelper = new DatabaseHelper(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        recyclerViewItems = findViewById(R.id.recyclerViewItems);
        spinnerFilter = findViewById(R.id.spinnerFilter);
        etSearch = findViewById(R.id.etSearch);
        etRadius = findViewById(R.id.etRadius);
        btnApplyRadius = findViewById(R.id.btnApplyRadius);

        recyclerViewItems.setLayoutManager(new LinearLayoutManager(this));
        itemList = new ArrayList<>();
        adapter = new ItemsAdapter(itemList, item -> {
            Intent intent = new Intent(ShowItemsActivity.this, ItemDetailActivity.class);
            intent.putExtra("item", item);
            startActivity(intent);
        });
        recyclerViewItems.setAdapter(adapter);

        setupFilter();
        setupSearch();

        btnApplyRadius.setOnClickListener(v -> requestLocationAndFilter());
        
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    userLocation = location;
                    filterAndSearch();
                }
            });
        }
    }

    private void requestLocationAndFilter() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            return;
        }

        Toast.makeText(this, "Acquiring location...", Toast.LENGTH_SHORT).show();
        
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, new CancellationTokenSource().getToken())
            .addOnSuccessListener(this, location -> {
                if (location != null) {
                    userLocation = location;
                    Toast.makeText(this, "Location acquired", Toast.LENGTH_SHORT).show();
                    filterAndSearch();
                } else {
                    fusedLocationClient.getLastLocation().addOnSuccessListener(this, lastLocation -> {
                        if (lastLocation != null) {
                            userLocation = lastLocation;
                            filterAndSearch();
                        } else {
                            Toast.makeText(this, "Unable to get current location. Please ensure GPS is on.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            })
            .addOnFailureListener(this, e -> {
                Toast.makeText(this, "Location error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            requestLocationAndFilter();
        }
    }

    private void setupFilter() {
        String[] categories = {
                getString(R.string.all_categories),
                "Electronics", "Pets", "Wallets", "Documents", "Others"
        };
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(spinnerAdapter);

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterAndSearch();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterAndSearch();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        filterAndSearch();
    }

    private void filterAndSearch() {
        String category = spinnerFilter.getSelectedItem().toString();
        String query = etSearch.getText().toString().trim();
        String radiusStr = etRadius.getText().toString().trim();

        List<LostFoundItem> baseList;

        if (category.equals(getString(R.string.all_categories))) {
            if (query.isEmpty()) {
                baseList = dbHelper.getAllItems();
            } else {
                baseList = dbHelper.searchItems(query);
            }
        } else {
            List<LostFoundItem> allForCategory = dbHelper.getItemsByCategory(category);
            if (query.isEmpty()) {
                baseList = allForCategory;
            } else {
                baseList = new ArrayList<>();
                for (LostFoundItem item : allForCategory) {
                    if (item.getName().toLowerCase().contains(query.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(query.toLowerCase())) {
                        baseList.add(item);
                    }
                }
            }
        }

        if (!radiusStr.isEmpty() && userLocation != null) {
            try {
                double radiusKm = Double.parseDouble(radiusStr);
                List<LostFoundItem> filteredList = new ArrayList<>();
                for (LostFoundItem item : baseList) {
                    float[] results = new float[1];
                    Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(),
                            item.getLatitude(), item.getLongitude(), results);
                    float distanceInMeters = results[0];
                    if (distanceInMeters / 1000 <= radiusKm) {
                        filteredList.add(item);
                    }
                }
                itemList = filteredList;
            } catch (NumberFormatException e) {
                itemList = baseList;
            }
        } else {
            itemList = baseList;
        }

        adapter.updateList(itemList);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
