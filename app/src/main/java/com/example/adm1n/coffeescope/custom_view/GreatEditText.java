package com.example.adm1n.coffeescope.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.example.adm1n.coffeescope.R;

import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE;

public class GreatEditText extends CardView {

    final private EditText editText;
    final private TextInputLayout textInputLayout;

    final private Drawable errorBackground;
    final private Drawable fineBackground;
    final private Animation shaking;

    private int paddingTop = 0;

    private int type = 0;

    public GreatEditText(Context context) {
        this(context, null);
    }

    public GreatEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GreatEditText(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        errorBackground = ContextCompat.getDrawable(getContext(), R.drawable.great_edit_text_error_background);
        fineBackground = ContextCompat.getDrawable(getContext(), R.drawable.great_edit_text_fine_background);
        shaking = AnimationUtils.loadAnimation(context, R.anim.shaking);
        paddingTop = Math.round(12 * (getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));

        hideError();

        inflate(getContext(), R.layout.great_edit_text, this);
        textInputLayout = (TextInputLayout) findViewById(R.id.textInputLayout);
        editText = (EditText) findViewById(R.id.editText);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.GreatEditText, 0, 0
        );

        try {
            textInputLayout.setHint(a.getString(R.styleable.GreatEditText_hint));
            setText(a.getString(R.styleable.GreatEditText_text));

            type = a.getInteger(R.styleable.GreatEditText_type, 0);
            switch (type) {
                case 1: // password
                    editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    textInputLayout.setPasswordVisibilityToggleEnabled(false);
                    break;
                case 2: // email
                    editText.setInputType(TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    break;
                case 3:
                    editText.setInputType(TYPE_CLASS_TEXT | TYPE_TEXT_FLAG_MULTI_LINE);
                    editText.setLines(5);
                    editText.setMinLines(5);
                    editText.setMaxLines(5);
                    break;
                default: // text
                    editText.setInputType(TYPE_CLASS_TEXT);
                    break;
            }

            int limit = a.getInt(R.styleable.GreatEditText_limit, -1);
            if (limit > 0) {
                editText.setFilters(new InputFilter[]{
                        new InputFilter.LengthFilter(limit)
                });
            }
        } finally {
            a.recycle();
        }

        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                textInputLayout.animate()
                        .translationY(hasFocus || editText.getText().length() > 0 ? paddingTop : 0)
                        .setDuration(100)
                        .start();
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textInputLayout.setPasswordVisibilityToggleEnabled(s.length() > 0 && type == 1);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void setText(String text) {
        editText.setText(text);
        textInputLayout.setTranslationY(text == null || text.isEmpty() ? 0 : paddingTop);
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

    public void showError() {
        this.startAnimation(shaking);
        this.setBackground(errorBackground);
    }

    public void hideError() {
        this.setBackground(fineBackground);
    }
}
