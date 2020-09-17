package com.example.jinglaixue.MainTabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jinglaixue.R;


public class MainTab01 extends Fragment implements View.OnClickListener {

    private View thisView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        thisView = inflater.inflate(R.layout.activity_home,container,false);
        unitUI();
        return thisView;


    }


    private void unitUI() {


    }


    @Override
    public void onClick(View v) {

    }

    public void shop_click(View view){
        //Log.i("shop","shopclick");
        //Toast.makeText(this.getContext(),"shop",Toast.LENGTH_SHORT).show();
    }
}
