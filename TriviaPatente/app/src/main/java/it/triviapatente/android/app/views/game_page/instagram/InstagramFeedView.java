package it.triviapatente.android.app.views.game_page.instagram;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.xml.datatype.Duration;

import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.SharedTPPreferences;
import it.triviapatente.android.http.modules.base.HTTPBaseEndpoint;
import it.triviapatente.android.http.utils.RetrofitManager;
import it.triviapatente.android.models.base.IGImage;
import it.triviapatente.android.models.game.Quiz;
import it.triviapatente.android.models.responses.InstagramImages;
import it.triviapatente.android.models.responses.Success;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by gabrielciulei on 02/06/2018.
 */



public class InstagramFeedView extends Fragment {

    @BindView(R.id.instaFeedButtonNext) ImageButton nextButton;
    @BindView(R.id.instaFeedButtonPrev) ImageButton prevButton;
    @BindView(R.id.postsViewPager) ViewPager postsViewPager;
    @BindString(R.string.instagram_url) String instagramUrl;
    @BindInt(R.integer.instagramGalleryDelay) int instagramGalleryDelay;
    @BindInt(R.integer.instagramGalleryRepeatDelay) int instagramGalleryRepeatDelay;

    @OnClick(R.id.profileLayout)
    public void goToProfile() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(instagramUrl));
        startActivity(browserIntent);
    }

    private PagerAdapter postsAdapter;

    @OnClick(R.id.instaFeedButtonClose)
    public void hide() {
        hide(getView());
    }
    private void hide(View v) {
        v.setVisibility(View.GONE);
    }
    public void show() {
        SharedTPPreferences.saveInstaLastShow(new Date());
        getView().setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.instaFeedButtonPrev)
    public void goToPreviuosPostClick() {
        int prevIndex = postsViewPager.getCurrentItem() - 1;
        if (prevIndex >= 0)
            postsViewPager.setCurrentItem(prevIndex);
        else
            postsViewPager.setCurrentItem(postsAdapter.getCount() - 1);
    }

    @OnClick(R.id.instaFeedButtonNext)
    public void goToNextPostClick() {
        int nextIndex = postsViewPager.getCurrentItem() + 1;
        if (nextIndex < postsAdapter.getCount())
            postsViewPager.setCurrentItem(nextIndex);
        else
            postsViewPager.setCurrentItem(0);

    }


    private Timer timer;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(timer != null) timer.cancel();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(timer != null) timer.cancel();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(postsAdapter != null && postsAdapter.getCount() > 0)  scheduleAutoScroll();

        // if more than 8 hours have passed
        Date now = new Date();
        Date last = SharedTPPreferences.getInstaLastShow();

        if (last == null || getDateDiff(last, now, TimeUnit.HOURS) >= 8) {
            scheduleLoad();
        }
    }
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        if(date1 == null || date2 == null) return 0;
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }
    private void scheduleLoad() {
        if(timer != null) timer.cancel();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadPosts();
                    }
                });
            }
        }, instagramGalleryDelay);
    }
    private void scheduleAutoScroll() {
        if(timer != null) timer.cancel();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(postsViewPager != null) goToNextPostClick();
                    }
                });
            }
        }, 0, instagramGalleryRepeatDelay);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_instagram_feed, container, false);
        ButterKnife.bind(this, view);
        hide(view);
        return view;
    }
    private void loadPosts() {
        RetrofitManager.getHTTPBaseEndpoint().getInstagramPhotos().enqueue(new Callback<InstagramImages>() {
            @Override
            public void onResponse(Call<InstagramImages> call, Response<InstagramImages> response) {
                if(response.isSuccessful() && response.body() != null) {
                    postsAdapter = new InstagramFeedPagerAdapter(getActivity().getSupportFragmentManager(), response.body().images);
                    postsViewPager.setAdapter(postsAdapter);
                    show();
                    scheduleAutoScroll();
                }
            }

            @Override
            public void onFailure(Call<InstagramImages> call, Throwable t) {

            }
        });
    }

    private class InstagramFeedPagerAdapter extends FragmentPagerAdapter {
        public List<IGImage> imageList;

        public InstagramFeedPagerAdapter(android.support.v4.app.FragmentManager fm, List<IGImage> imageList) {
            super(fm);
            this.imageList = imageList;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            IGImage image = imageList.get(position);
            return InstagramFeedItemFragment.newInstance(image);
        }

        @Override
        public int getCount() {
            return imageList.size();
        }
    }

}
