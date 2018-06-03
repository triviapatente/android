package it.triviapatente.android.app.views.game_page;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.custom_classes.listViews.listElements.normal.QuizHolder;
import it.triviapatente.android.app.views.game_page.play_round.PlayRoundActivity;
import it.triviapatente.android.models.game.Quiz;

/**
 * Created by gabrielciulei on 02/06/2018.
 */



public class InstagramFeedView extends Fragment {

    @BindView(R.id.instaFeedButtonNext) Button nextButton;
    @BindView(R.id.instaFeedButtonPrev) Button prevButton;
    @BindView(R.id.postsViewPager) ViewPager postsViewPager;

    private PagerAdapter postsAdapter;

    @OnClick(R.id.instaFeedButtonPrev)
    public void goToPreviuosPostClick() {
        int prevIndex = postsViewPager.getCurrentItem() - 1;
        if (prevIndex >= 0)
            postsViewPager.setCurrentItem(prevIndex);
    }

    @OnClick(R.id.instaFeedButtonNext)
    public void goToNextPostClick() {
        int nextIndex = postsViewPager.getCurrentItem() + 1;
        if (nextIndex < postsAdapter.getCount())
            postsViewPager.setCurrentItem(nextIndex);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_instagram_feed, container, false);
        ButterKnife.bind(this, view);
        loadPosts();
        return view;
    }

    private void loadPosts() {
        // TODO: load posts from server
        postsAdapter = new InstagramFeedPagerAdapter();
        postsViewPager.setAdapter(postsAdapter);
    }

    private class InstagramFeedPagerAdapter extends PagerAdapter {
        public List<Quiz> postsList;

        public InstagramFeedPagerAdapter() {
            // postsList=posts;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            InstagramFeedItemFragment postItem = new InstagramFeedItemFragment();

            return postItem.getView();
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return object == view;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

}
