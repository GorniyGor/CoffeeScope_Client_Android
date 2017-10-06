package com.example.adm1n.coffeescope.introduction.authorization;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adm1n.coffeescope.BaseFragment;
import com.example.adm1n.coffeescope.R;
import com.example.adm1n.coffeescope.custom_view.GreatEditText;
import com.example.adm1n.coffeescope.dialog.OkDialog;
import com.example.adm1n.coffeescope.introduction.presenter.IntroductionPresenter;
import com.example.adm1n.coffeescope.introduction.registration.IntroductionRegistrationActivity;
import com.example.adm1n.coffeescope.profile.ProfileActivity;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
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
    private TextView tvAuthError;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

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

        GreatEditText email = (GreatEditText) view.findViewById(R.id.cvAuthorizationEmail);
        GreatEditText pass = (GreatEditText) view.findViewById(R.id.cvAuthorizationPassword);

        textInputLayoutEmail = email.getTextInputLayout();
        textInputLayoutPass = pass.getTextInputLayout();
        etEmail = email.getEditText();
        etPassword = pass.getEditText();
        btnLogin = (Button) view.findViewById(R.id.btnAuthorizationLogin);
        btnRegistration = (Button) view.findViewById(R.id.btnAuthorizationRegistration);
        tvForgotPassword = (TextView) view.findViewById(R.id.tvForgotPassword);
        tvAuthError = (TextView) view.findViewById(R.id.tvAuthError);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRxListener();
        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), IntroductionRegistrationActivity.class);
                startActivity(intent);
            }
        });

        btnRegistration.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
                return true;
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.login(etEmail.getText().toString(), etPassword.getText().toString());
                btnLogin.setEnabled(false);
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
                        return textViewAfterTextChangeEvent.view().length() != 0;
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

        Disposable mainObs = Observable.combineLatest(checkEmailField, checkPassField, new BiFunction<Boolean, Boolean, Boolean>() {
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
        compositeDisposable.add(mainObs);
        btnLogin.setEnabled(false);
    }

    @Override
    public void onDestroy() {
        presenter.onStop();
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void showError(String s) {
        OkDialog dialog = new OkDialog(s);
        dialog.show(getFragmentManager(), "AuthError");
        btnLogin.setEnabled(true);
    }

    @Override
    public void successLogin() {
        Toast.makeText(getActivity(), "Успешный LOGIN", Toast.LENGTH_SHORT).show();
    }
}
