package com.vidswift.downloader.allvideosaver.main;

import static android.view.View.INVISIBLE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vidswift.downloader.allvideosaver.R;
import com.vidswift.downloader.allvideosaver.databinding.ActivityLanguagesBinding;
import com.vidswift.downloader.allvideosaver.databinding.ItemLanguageBinding;
import com.vidswift.downloader.allvideosaver.nexts.LanguageModel;

import java.util.ArrayList;
import java.util.List;

public class LanguageActivity extends BaseActivity {
    private final List<LanguageModel> languageList = new ArrayList<>();
    private ActivityLanguagesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        toolbarHeaderText = "Select Language";
        super.onCreate(savedInstanceState);


        binding = ActivityLanguagesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbarLayout.headerTitle.setText("Select Language");
        binding.toolbarLayout.btnBack.setOnClickListener(v -> onBackPressed());
        showSettings = true;

        binding.toolbarLayout.btnBack.setVisibility(INVISIBLE);
        setupLanguageList();
        LanguageAdapter adapter = new LanguageAdapter(languageList);


        binding.languageRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        binding.languageRecyclerView.setAdapter(adapter);


        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedLanguage = languageList.get(adapter.selectedPosition).getLanguageName();

                SharedPreferences prefs = getSharedPreferences("app_settings", MODE_PRIVATE);
                prefs.edit().putString("selected_language", selectedLanguage).apply();
                Intent intent = new Intent(LanguageActivity.this, How1Activity.class);
                startActivity(intent);
            }
        });
    }

    private void setupLanguageList() {
        languageList.add(new LanguageModel("English", R.mipmap.ic_launcher, true));
        languageList.add(new LanguageModel("Hindi", R.mipmap.ic_launcher, false));
        languageList.add(new LanguageModel("Spanish  ", R.mipmap.ic_launcher, false));
        languageList.add(new LanguageModel("French", R.mipmap.ic_launcher, false));
        languageList.add(new LanguageModel("Bengali", R.mipmap.ic_launcher, false));
        languageList.add(new LanguageModel("Arabic", R.mipmap.ic_launcher, false));
        languageList.add(new LanguageModel("Urdu", R.mipmap.ic_launcher, false));
        languageList.add(new LanguageModel("Telugu", R.mipmap.ic_launcher, false));


    }

    @Override
    public void onBackPressed() {
        myBackActivity();
    }

    public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder> {

        private final List<LanguageModel> languageList;
        private int selectedPosition = 0;

        public LanguageAdapter(List<LanguageModel> languageList) {
            this.languageList = languageList;
            if (!languageList.isEmpty()) languageList.get(0).setSelected(true);
        }

        @NonNull
        @Override
        public LanguageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            ItemLanguageBinding binding = ItemLanguageBinding.inflate(inflater, parent, false);
            return new LanguageViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull LanguageViewHolder holder, int position) {
            LanguageModel model = languageList.get(position);
            holder.binding.languageName.setText(model.getLanguageName());
            holder.binding.languageFlag.setImageResource(model.getFlagResId());
            holder.binding.languageName.setSelected(true);
            if (model.isSelected()) {
                holder.binding.getRoot().setBackgroundResource(R.drawable.bg_country_select);
                holder.binding.imgSelect.setImageResource(R.drawable.ic_selected);


            } else {
                holder.binding.getRoot().setBackgroundResource(R.drawable.bg_country_unselect);
                holder.binding.imgSelect.setImageResource(R.drawable.ic_unselected);

            }


            holder.binding.getRoot().setOnClickListener(v -> {
                if (selectedPosition != position) {
                    languageList.get(selectedPosition).setSelected(false);
                    model.setSelected(true);
                    notifyItemChanged(selectedPosition);
                    notifyItemChanged(position);
                    selectedPosition = position;
                }
            });
        }


        @Override
        public int getItemCount() {
            return languageList.size();
        }

        class LanguageViewHolder extends RecyclerView.ViewHolder {
            ItemLanguageBinding binding;

            LanguageViewHolder(ItemLanguageBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}