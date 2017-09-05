package com.example.adm1n.coffeescope.introduction.authorization;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adm1n.coffeescope.BaseFragment;
import com.example.adm1n.coffeescope.R;
import com.example.adm1n.coffeescope.introduction.presenter.IntroductionPresenter;
import com.example.adm1n.coffeescope.introduction.registration.IntroductionRegistrationActivity;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by adm1n on 04.09.2017.
 */

public class IntroductionAuthorizationFragment extends BaseFragment implements IIntroductionAuthorizationView {

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPass;
    private EditText etEmail;
    private EditText etPassword;

    private Button btnRegistration;
    private Button btnLogin;
    private TextView tvForgotPassword;

    private IntroductionPresenter presenter;

    public static IntroductionAuthorizationFragment newInstance() {
        Bundle args = new Bundle();
        IntroductionAuthorizationFragment fragment = new IntroductionAuthorizationFragment();
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
        View view = inflater.inflate(R.layout.fragment_introduction_authorization, null);
        textInputLayoutEmail = (TextInputLayout) view.findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPass = (TextInputLayout) view.findViewById(R.id.textInputLayoutPass);
        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etPassword = (EditText) view.findViewById(R.id.etPassword);
        btnLogin = (Button) view.findViewById(R.id.btnAuthorizationLogin);
        btnRegistration = (Button) view.findViewById(R.id.btnAuthorizationRegistration);
        tvForgotPassword = (TextView) view.findViewById(R.id.tvForgotPassword);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRxListener();
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

        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), IntroductionRegistrationActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.login(etEmail.getText().toString(), etPassword.getText().toString());
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Восстанавливаем пароль", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setRxListener() {
        Observable<Boolean> checkEmailField = RxTextView.afterTextChangeEvents(
                etEmail)
                .map(new Function<TextViewAfterTextChangeEvent, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull TextViewAfterTextChangeEvent textViewAfterTextChangeEvent) throws Exception {
                        String text = textViewAfterTextChangeEvent.view().getText().toString();
                        if (textViewAfterTextChangeEvent.view().length() == 0) {
                            return false;
                        } else {
                            return text.contains("@") && text.contains(".");
                        }
                    }
                });

        Observable<Boolean> checkPassField = RxTextView.afterTextChangeEvents(
                etPassword)
                .map(new Function<TextViewAfterTextChangeEvent, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull TextViewAfterTextChangeEvent textViewAfterTextChangeEvent) throws Exception {
                        return textViewAfterTextChangeEvent.view().length() >= 4;
                    }
                });

        Observable.combineLatest(checkEmailField, checkPassField, new BiFunction<Boolean, Boolean, Boolean>() {
            @Override
            public Boolean apply(@NonNull Boolean aBoolean, @NonNull Boolean aBoolean2) throws Exception {
                return aBoolean && aBoolean2;
            }
        }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    btnLogin.setEnabled(true);
                } else {
                    btnLogin.setEnabled(false);
                }
            }
        });
        btnLogin.setEnabled(false);
    }

    @Override
    public void onStop() {
        presenter.onStop();
        super.onStop();
    }

    @Override
    public void showError(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void successLogin() {
        Toast.makeText(getActivity(), "Успешный LOGIN", Toast.LENGTH_SHORT).show();
    }
}
