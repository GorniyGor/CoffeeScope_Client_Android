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
import com.example.adm1n.coffeescope.dialog.OkDialog;
import com.jakewharton.rxbinding2.widget.RxTextView;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class FeedbackFragment extends Fragment implements IFeedbackView {

    private int limit = 300;
    private IFeedbackPresenter presenter = new FeedbackPresenter(this);
    private GreatEditText etFeedback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback, null);

        etFeedback = (GreatEditText) view.findViewById(R.id.et_feedback);
        limit = getResources().getInteger(R.integer.feedback_limit);

        final Button btnFeedback = (Button) view.findViewById(R.id.btn_feedback);
        final TextView tvFeedbackLimit = (TextView) view.findViewById(R.id.tv_feedback_limit);

        RxTextView.textChanges(etFeedback.getEditText())
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(@NonNull CharSequence charSequence) throws Exception {
                        btnFeedback.setEnabled(charSequence.length() > 0);
                        int current = limit - charSequence.length();
                        String l = getResources().getQuantityString(
                                R.plurals.feedback_limit,
                                current, current
                        );
                        tvFeedbackLimit.setText(l);
                    }
                });

        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.sendFeedback(etFeedback.getText());
            }
        });

        return view;
    }

    @Override
    public void showProgress(boolean show) {

    }

    @Override
    public void feedbackSent() {
        etFeedback.setText("");
        OkDialog dialog = new OkDialog(getString(R.string.feedback_sent));
        dialog.show(getFragmentManager(), "AuthError");
    }
}
