package com.example.adm1n.coffeescope.profile;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.adm1n.coffeescope.BaseFragment;
import com.example.adm1n.coffeescope.R;
import com.example.adm1n.coffeescope.custom_view.GreatEditText;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;

public class ProfileFragment extends BaseFragment implements IProfileView {

    private GreatEditText etFirstName;
    private GreatEditText etLastName;
    private GreatEditText etEmail;
    private Button saveProfileButton;

    private IProfilePresenter presenter = new ProfilePresenter(this);

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static ProfileFragment newInstance() {
        Bundle args = new Bundle();
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, null);

        etFirstName = (GreatEditText) view.findViewById(R.id.first_name);
        etLastName = (GreatEditText) view.findViewById(R.id.last_name);
        etEmail = (GreatEditText) view.findViewById(R.id.email);
        saveProfileButton = (Button) view.findViewById(R.id.save_profile_button);
        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.saveProfile(etFirstName.getText(), etLastName.getText(), etEmail.getText());
            }
        });

        etFirstName.setText(presenter.getCurrentFirstName());
        etLastName.setText(presenter.getCurrentLastName());
        etEmail.setText(presenter.getCurrentEmail());

        view.findViewById(R.id.logout_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.LogoutDialog);
                builder.setMessage(R.string.logout_message);
                builder.setPositiveButton(R.string.cancel, null);
                builder.setNegativeButton(R.string.logout, null);

                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#47c68a"));
            }
        });
        view.findViewById(R.id.cv_feedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Фидбэк", Toast.LENGTH_SHORT).show();
            }
        });
        view.findViewById(R.id.cv_change_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Изменяем пароль", Toast.LENGTH_SHORT).show();
            }
        });

        setRx(presenter.getCurrentFirstName(), presenter.getCurrentLastName(), presenter.getCurrentEmail());

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    public void setRx(String firstName, String lastName, String email) {
        Observable<Boolean> f = RxTextView.afterTextChangeEvents(etFirstName.getEditText()).map(new Checker(presenter.getCurrentFirstName()));
        Observable<Boolean> l = RxTextView.afterTextChangeEvents(etLastName.getEditText()).map(new Checker(presenter.getCurrentLastName()));
        Observable<Boolean> e = RxTextView.afterTextChangeEvents(etEmail.getEditText()).map(new Checker(presenter.getCurrentEmail()));

        Disposable disposable = Observable.combineLatest(f, l, e, new Function3<Boolean, Boolean, Boolean, Boolean>() {
            @Override
            public Boolean apply(@NonNull Boolean b1, @NonNull Boolean b2, @NonNull Boolean b3) throws Exception {
                return b1 || b2 || b3;
            }
        }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean o) throws Exception {
                saveProfileButton.setEnabled(o);
            }
        });
        compositeDisposable.add(disposable);
    }

    private class Checker implements Function<TextViewAfterTextChangeEvent, Boolean> {

        private String current;

        Checker(String current) {
            this.current = current;
        }

        @Override
        public Boolean apply(@NonNull TextViewAfterTextChangeEvent event) throws Exception {
            return !event.view().getText().toString().equals(current);
        }
    }
}
