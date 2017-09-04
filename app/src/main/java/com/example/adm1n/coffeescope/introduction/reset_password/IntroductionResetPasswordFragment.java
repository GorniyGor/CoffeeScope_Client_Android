package com.example.adm1n.coffeescope.introduction.reset_password;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adm1n.coffeescope.BaseFragment;
import com.example.adm1n.coffeescope.R;

/**
 * Created by adm1n on 04.09.2017.
 */

public class IntroductionResetPasswordFragment extends BaseFragment {

    public static IntroductionResetPasswordFragment newInstance() {

        Bundle args = new Bundle();

        IntroductionResetPasswordFragment fragment = new IntroductionResetPasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_introduction_reset_password, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
