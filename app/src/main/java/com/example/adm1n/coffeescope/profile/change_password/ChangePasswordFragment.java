package com.example.adm1n.coffeescope.profile.change_password;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.adm1n.coffeescope.R;
import com.example.adm1n.coffeescope.custom_view.GreatEditText;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function4;

public class ChangePasswordFragment extends Fragment {

    private GreatEditText oldPassword;
    private GreatEditText newPassword;
    private GreatEditText newPasswordRepeat;
    private Button changePasswordButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_change_password, container);

        oldPassword = (GreatEditText) view.findViewById(R.id.old_password);
        newPassword = (GreatEditText) view.findViewById(R.id.new_password);
        newPasswordRepeat = (GreatEditText) view.findViewById(R.id.new_password_repeat);
        changePasswordButton = (Button) view.findViewById(R.id.change_password_button);

        setListener();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void setListener() {
        Observable<Boolean> x = RxTextView.afterTextChangeEvents(oldPassword.getEditText()).map(new Lenghter());
        Observable<Boolean> y = RxTextView.afterTextChangeEvents(newPassword.getEditText()).map(new Lenghter());
        Observable<Boolean> z = RxTextView.afterTextChangeEvents(newPasswordRepeat.getEditText()).map(new Lenghter());

        Observable<CharSequence> a = RxTextView.textChanges(newPassword.getEditText());
        Observable<CharSequence> b = RxTextView.textChanges(newPasswordRepeat.getEditText());

        Observable<Boolean> h = Observable.combineLatest(a, b, new BiFunction<CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean apply(@NonNull CharSequence charSequence, @NonNull CharSequence charSequence2) throws Exception {
                return TextUtils.equals(charSequence, charSequence2);
            }
        });

        Observable.combineLatest(x, y, z, h, new Function4<Boolean, Boolean, Boolean, Boolean, Boolean>() {
            @Override
            public Boolean apply(@NonNull Boolean a, @NonNull Boolean b, @NonNull Boolean c, @NonNull Boolean d) throws Exception {
                return a && b && c && d;
            }
        }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
                changePasswordButton.setEnabled(aBoolean);
            }
        });
    }

    private class Lenghter implements Function<TextViewAfterTextChangeEvent, Boolean> {

        @Override
        public Boolean apply(@NonNull TextViewAfterTextChangeEvent event) throws Exception {
            return event.view().getText().length() >= 4;
        }
    }
}
