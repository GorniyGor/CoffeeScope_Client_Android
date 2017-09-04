package com.example.adm1n.coffeescope.introduction.authorization;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.adm1n.coffeescope.BaseFragment;
import com.example.adm1n.coffeescope.R;

/**
 * Created by adm1n on 04.09.2017.
 */

public class IntroductionAuthorizationFragment extends BaseFragment {

    private TextInputLayout textInputLayoutEmail;
    private EditText etEmail;

    public static IntroductionAuthorizationFragment newInstance() {

        Bundle args = new Bundle();

        IntroductionAuthorizationFragment fragment = new IntroductionAuthorizationFragment();
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
        View view = inflater.inflate(R.layout.fragment_introduction_authorization, null);
        textInputLayoutEmail = (TextInputLayout) view.findViewById(R.id.textInputLayoutEmail);
        etEmail = (EditText) view.findViewById(R.id.etEmail);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    textInputLayoutEmail.setGravity(Gravity.BOTTOM);
                } else {
                    if (etEmail.getText().length() == 0) {
                        textInputLayoutEmail.setGravity(Gravity.CENTER);
                    }
                }
                textInputLayoutEmail.postInvalidate();
            }
        });
    }
}
