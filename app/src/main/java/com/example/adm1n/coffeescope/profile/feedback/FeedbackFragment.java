package com.example.adm1n.coffeescope.profile.feedback;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.adm1n.coffeescope.R;
import com.example.adm1n.coffeescope.custom_view.GreatEditText;
import com.jakewharton.rxbinding2.widget.RxTextView;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class FeedbackFragment extends Fragment {

    private int limit = 300;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback, null);

        GreatEditText et_feedback = (GreatEditText) view.findViewById(R.id.et_feedback);
        limit = getResources().getInteger(R.integer.feedback_limit);

        final Button btn_feedback = (Button) view.findViewById(R.id.btn_feedback);
        final TextView tv_feedback_limit = (TextView) view.findViewById(R.id.tv_feedback_limit);

        RxTextView.textChanges(et_feedback.getEditText())
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(@NonNull CharSequence charSequence) throws Exception {
                        btn_feedback.setEnabled(charSequence.length() > 0);
                        int current = limit - charSequence.length();
                        String l = getResources().getQuantityString(
                                R.plurals.feedback_limit,
                                current, current
                        );
                        tv_feedback_limit.setText(l);
                    }
                });

        return view;
    }
}
