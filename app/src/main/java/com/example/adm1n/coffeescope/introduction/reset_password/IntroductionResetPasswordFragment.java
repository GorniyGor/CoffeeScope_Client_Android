package com.example.adm1n.coffeescope.introduction.reset_password;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.adm1n.coffeescope.BaseFragment;
import com.example.adm1n.coffeescope.R;
import com.example.adm1n.coffeescope.introduction.presenter.IntroductionPresenter;

/**
 * Created by adm1n on 04.09.2017.
 */

public class IntroductionResetPasswordFragment extends BaseFragment implements IIntroductionResetPasswordView {

    private Button btnResetPassword;
    private EditText etResetEmail;
    private IntroductionPresenter presenter;

    public static IntroductionResetPasswordFragment newInstance() {
        Bundle args = new Bundle();
        IntroductionResetPasswordFragment fragment = new IntroductionResetPasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new IntroductionPresenter(getActivity(), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_introduction_reset_password, null);
        btnResetPassword = (Button) view.findViewById(R.id.btnResetPassword);
        etResetEmail = (EditText) view.findViewById(R.id.etResetEmail);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                presenter.resetPass();
            }
        });
    }

    @Override
    public void showError(String s) {

    }
}
