package com.example.adm1n.coffeescope.introduction.registration;

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
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function5;

/**
 * Created by adm1n on 04.09.2017.
 */

public class IntroductionRegistrationFragment extends BaseFragment implements IIntroductionRegistrationView {
    private IntroductionPresenter presenter;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private GreatEditText lastName;
    private GreatEditText firstName;
    private GreatEditText email;
    private GreatEditText password;
    private GreatEditText passwordRepeat;

    private TextInputLayout textInputLayoutName;
    private EditText etName;
    private TextInputLayout textInputLayoutLastName;
    private EditText etLastName;
    private TextInputLayout textInputLayoutEmail;
    private EditText etEmail;
    private TextInputLayout textInputLayoutPassword;
    private EditText etPassword;
    private TextInputLayout textInputLayoutConfirmPassword;
    private EditText etConfirmPassword;

    private Button btnRegistration;
    private TextView tvPolicy;

    public static IntroductionRegistrationFragment newInstance() {
        Bundle args = new Bundle();
        IntroductionRegistrationFragment fragment = new IntroductionRegistrationFragment();
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
        View view = inflater.inflate(R.layout.fragment_introduction_registration, null);
        initView(view);
        return view;
    }

    private void initView(View v) {
        lastName = (GreatEditText) v.findViewById(R.id.cvRegistrationLastName);
        firstName = (GreatEditText) v.findViewById(R.id.cvRegistrationName);
        email = (GreatEditText) v.findViewById(R.id.cvRegistrationEmail);
        password = (GreatEditText) v.findViewById(R.id.cvRegistrationPassword);
        passwordRepeat = (GreatEditText) v.findViewById(R.id.cvRegistrationConfirmPassword);

        textInputLayoutLastName = lastName.getTextInputLayout();
        textInputLayoutName = firstName.getTextInputLayout();
        textInputLayoutEmail = email.getTextInputLayout();
        textInputLayoutPassword = password.getTextInputLayout();
        textInputLayoutConfirmPassword = passwordRepeat.getTextInputLayout();
        etName = firstName.getEditText();
        etLastName = lastName.getEditText();
        etEmail = email.getEditText();
        etPassword = password.getEditText();
        etConfirmPassword = passwordRepeat.getEditText();
        btnRegistration = (Button) v.findViewById(R.id.btnRegistrationFinish);
        tvPolicy = (TextView) v.findViewById(R.id.tvPolicy);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRxListener();
        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.registration(etLastName.getText().toString(),
                        etName.getText().toString(),
                        etEmail.getText().toString(),
                        etPassword.getText().toString(),
                        etConfirmPassword.getText().toString());
                btnRegistration.setEnabled(false);
            }
        });
        tvPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Открываем правила", Toast.LENGTH_SHORT).show();
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

        Observable<Boolean> checkLastNameField = RxTextView.afterTextChangeEvents(
                etLastName)
                .map(new Function<TextViewAfterTextChangeEvent, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull TextViewAfterTextChangeEvent textViewAfterTextChangeEvent) throws Exception {
                        return textViewAfterTextChangeEvent.view().length() >= 1;
                    }
                });

        Observable<Boolean> checkNameField = RxTextView.afterTextChangeEvents(
                etName)
                .map(new Function<TextViewAfterTextChangeEvent, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull TextViewAfterTextChangeEvent textViewAfterTextChangeEvent) throws Exception {
                        return textViewAfterTextChangeEvent.view().length() >= 1;
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

        Observable<Boolean> checkConfirmPassField = RxTextView.afterTextChangeEvents(
                etConfirmPassword)
                .map(new Function<TextViewAfterTextChangeEvent, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull TextViewAfterTextChangeEvent textViewAfterTextChangeEvent) throws Exception {
                        return textViewAfterTextChangeEvent.view().length() >= 4;
                    }
                });

        Disposable authObs = Observable.combineLatest(
                checkEmailField,
                checkNameField,
                checkLastNameField,
                checkPassField,
                checkConfirmPassField,
                new Function5<Boolean, Boolean, Boolean, Boolean, Boolean, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull Boolean aBoolean, @NonNull Boolean aBoolean2, @NonNull Boolean aBoolean3, @NonNull Boolean aBoolean4, @NonNull Boolean aBoolean5) throws Exception {
                        return aBoolean && aBoolean2 && aBoolean3 && aBoolean4 && aBoolean5;
                    }
                }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    btnRegistration.setEnabled(true);
                } else {
                    btnRegistration.setEnabled(false);
                }
            }
        });
        compositeDisposable.add(authObs);
        btnRegistration.setEnabled(false);
    }

    @Override
    public void onDestroy() {
        presenter.onStop();
        compositeDisposable.dispose();
        super.onDestroy();
    }

    @Override
    public void showError(String s) {
        OkDialog dialog = new OkDialog(s);
        dialog.show(getFragmentManager(), "RegError");
        btnRegistration.setEnabled(true);
    }

    @Override
    public void onRegistrationFinish() {
        Toast.makeText(getActivity(), "onRegistrationFinish", Toast.LENGTH_SHORT).show();
        btnRegistration.setEnabled(true);
    }
}
