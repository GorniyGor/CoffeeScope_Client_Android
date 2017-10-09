package com.example.adm1n.coffeescope.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;

import com.example.adm1n.coffeescope.R;

import static android.text.InputType.TYPE_CLASS_TEXT;

public class GreatEditText extends CardView {

    final private EditText editText;
    final private TextInputLayout textInputLayout;

    private int paddintTop = 0;

    public GreatEditText(Context context) {
        this(context, null);
    }

    public GreatEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GreatEditText(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        paddintTop = Math.round(12 * (getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));

        inflate(getContext(), R.layout.great_edit_text, this);
        textInputLayout = (TextInputLayout) findViewById(R.id.textInputLayout);
        editText = (EditText) findViewById(R.id.editText);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.GreatEditText, 0, 0
        );

        try {
            textInputLayout.setHint(a.getString(R.styleable.GreatEditText_hint));
            int type = a.getInteger(R.styleable.GreatEditText_type, 0);
            switch (type) {
                case 1: // password
                    editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    textInputLayout.setPasswordVisibilityToggleEnabled(true);
                    break;
                case 2: // email
                    editText.setInputType(TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    break;
                default: // text
                    editText.setInputType(TYPE_CLASS_TEXT);
                    break;
            }
        } finally {
            a.recycle();
        }

        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                int moveTo;
                if (hasFocus || editText.getText().length() > 0) {
                    moveTo = paddintTop;
                } else {
                    moveTo = 0;
                }
                textInputLayout.animate().translationY(moveTo).setDuration(100).start();
            }
        });

    }

    public void setText(String text) {
        editText.setText(text);
        textInputLayout.setTranslationY(text.isEmpty() ? 0 : paddintTop);
    }

    public String getText() {
        return editText.getText().toString();
    }

    public EditText getEditText() {
        return editText;
    }

    public TextInputLayout getTextInputLayout() {
        return textInputLayout;
    }
}
