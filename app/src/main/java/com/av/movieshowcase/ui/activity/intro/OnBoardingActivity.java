package com.av.movieshowcase.ui.activity.intro;

import androidx.databinding.DataBindingUtil;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import com.av.movieshowcase.R;
import com.av.movieshowcase.databinding.IntroActivityBinding;
import com.av.movieshowcase.ui.activity.main.MainActivity;
import com.av.movieshowcase.ui.base.BaseActivity;
import com.av.movieshowcase.utils.SaveData;
import com.google.android.material.textfield.TextInputLayout;

public class OnBoardingActivity extends BaseActivity {

    private IntroActivityBinding binding;
    private String name;
    private SaveData saveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_on_boarding);
        initSplash();

        if(saveData.isFirstTimeSplash()){
            binding.fabDone.setOnClickListener(v->{
                if(isNameValid(binding.textFieldName)) {
                    saveData.setFirstTimeSplash(false);
                    goToMain();
                }
            });
        } else goToMain();



    }

    private void initSplash(){
        getSupportActionBar().hide();
        setTransparentStatusBar(true);
        setLightNavigationBar();
        saveData = new SaveData(getApplicationContext());
    }


    private boolean isNameValid(TextInputLayout textInputLayout){
        EditText editText = textInputLayout.getEditText();
        name = editText.getText().toString();
        if(name.isEmpty() || name.equalsIgnoreCase("")){
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError(getString(R.string.enter_name_warning));
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
            saveData.saveName(name);
            return true;
        }
    }

    private void goToMain(){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
