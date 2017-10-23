package com.example.adm1n.coffeescope.profile.feedback;

public class FeedbackPresenter implements IFeedbackPresenter {

    private final IFeedbackView view;

    FeedbackPresenter(IFeedbackView v) {
        view = v;
    }

    @Override
    public void sendFeedback() {
        // send feedback
        view.feedbackSent();
    }
}
