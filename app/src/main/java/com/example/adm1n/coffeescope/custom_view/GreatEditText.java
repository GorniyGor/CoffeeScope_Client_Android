package com.example.adm1n.coffeescope.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.EditText;

import com.example.adm1n.coffeescope.R;

import static android.text.InputType.TYPE_CLASS_TEXT;

public class GreatEditText extends CardView {

    final private EditText editText;
    final private TextInputLayout textInputLayout;

    public GreatEditText(Context context) {
        this(context, null);
    }

    public GreatEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GreatEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

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
    }

    public EditText getEditText() {
        return editText;
    }

    public TextInputLayout getTextInputLayout() {
        return textInputLayout;
    }
}
