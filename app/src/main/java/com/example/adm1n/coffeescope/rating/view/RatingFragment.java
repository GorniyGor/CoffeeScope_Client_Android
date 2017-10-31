package com.example.adm1n.coffeescope.rating.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.adm1n.coffeescope.BaseFragment;
import com.example.adm1n.coffeescope.R;
import com.example.adm1n.coffeescope.dialog.OkDialog;
import com.example.adm1n.coffeescope.rating.presenter.RatingPresenter;

/**
 * Created by adm1n on 31.10.2017.
 */

public class RatingFragment extends BaseFragment implements IRatingView {

    private Integer placeId;
    private EditText etCommentText;
    private Integer ratingCount = null;
    private Button btnSendComment;
    private ImageView ivStars1;
    private ImageView ivStars2;
    private ImageView ivStars3;
    private ImageView ivStars4;
    private ImageView ivStars5;
    private RatingPresenter presenter;


    public static RatingFragment newInstance() {
        //// TODO: 31.10.2017 ПЕРЕДАВАТЬ ВО ФРАГМЕНТ PLACEID ДЛЯ ПЕРЕДАЧИ НА СЕРВЕР
        Bundle args = new Bundle();
        RatingFragment fragment = new RatingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new RatingPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rating, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        etCommentText = (EditText) view.findViewById(R.id.etRatingComment);
        btnSendComment = (Button) view.findViewById(R.id.btn_rating_send);
        ivStars1 = (ImageView) view.findViewById(R.id.iv_rating_icon1);
        ivStars2 = (ImageView) view.findViewById(R.id.iv_rating_icon2);
        ivStars3 = (ImageView) view.findViewById(R.id.iv_rating_icon3);
        ivStars4 = (ImageView) view.findViewById(R.id.iv_rating_icon4);
        ivStars5 = (ImageView) view.findViewById(R.id.iv_rating_icon5);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ratingCount != null) {
                    presenter.sendRating(etCommentText.getText().toString(), 10, ratingCount.toString());
                    Toast.makeText(getActivity(), "Rating :" + ratingCount + " text: " + etCommentText.getText().toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Ничего не выделено", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnSendComment.setEnabled(false);
        initStars();
    }

    private void initStars() {
        ivStars1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStars(1);
            }
        });
        ivStars2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStars(2);
            }
        });
        ivStars3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStars(3);
            }
        });
        ivStars4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStars(4);
            }
        });
        ivStars5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStars(5);
            }
        });
    }

    void setStars(int i) {
        switch (i) {
            case 1:
                ivStars1.setImageResource(R.drawable.raiting_icon_active);
                ivStars2.setImageResource(R.drawable.raiting_icon);
                ivStars3.setImageResource(R.drawable.raiting_icon);
                ivStars4.setImageResource(R.drawable.raiting_icon);
                ivStars5.setImageResource(R.drawable.raiting_icon);
                ratingCount = 1;
                break;
            case 2:
                ivStars1.setImageResource(R.drawable.raiting_icon_active);
                ivStars2.setImageResource(R.drawable.raiting_icon_active);
                ivStars3.setImageResource(R.drawable.raiting_icon);
                ivStars4.setImageResource(R.drawable.raiting_icon);
                ivStars5.setImageResource(R.drawable.raiting_icon);
                ratingCount = 2;
                break;
            case 3:
                ivStars1.setImageResource(R.drawable.raiting_icon_active);
                ivStars2.setImageResource(R.drawable.raiting_icon_active);
                ivStars3.setImageResource(R.drawable.raiting_icon_active);
                ivStars4.setImageResource(R.drawable.raiting_icon);
                ivStars5.setImageResource(R.drawable.raiting_icon);
                ratingCount = 3;
                break;
            case 4:
                ivStars1.setImageResource(R.drawable.raiting_icon_active);
                ivStars2.setImageResource(R.drawable.raiting_icon_active);
                ivStars3.setImageResource(R.drawable.raiting_icon_active);
                ivStars4.setImageResource(R.drawable.raiting_icon_active);
                ivStars5.setImageResource(R.drawable.raiting_icon);
                ratingCount = 4;
                break;
            case 5:
                ivStars1.setImageResource(R.drawable.raiting_icon_active);
                ivStars2.setImageResource(R.drawable.raiting_icon_active);
                ivStars3.setImageResource(R.drawable.raiting_icon_active);
                ivStars4.setImageResource(R.drawable.raiting_icon_active);
                ivStars5.setImageResource(R.drawable.raiting_icon_active);
                ratingCount = 5;
                break;
        }
        btnSendComment.setEnabled(true);
    }

    @Override
    public void showOk() {
        Toast.makeText(getActivity(), "OK", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String s) {
        OkDialog dialog = new OkDialog(s);
        dialog.show(getFragmentManager(), "RatingError");
    }
}