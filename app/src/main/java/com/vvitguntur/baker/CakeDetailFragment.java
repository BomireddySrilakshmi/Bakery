package com.vvitguntur.baker;

import android.app.Activity;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.vvitguntur.baker.Model.Step;
import com.vvitguntur.baker.dummy.DummyContent;

/**
 * A fragment representing a single Cake detail screen.
 * This fragment is either contained in a {@link CakeListActivity}
 * in two-pane mode (on tablets) or a {@link CakeDetailActivity}
 * on handsets.
 */
public class CakeDetailFragment extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";
    public SimpleExoPlayer exoPlay;
    public SimpleExoPlayerView exoPlayer;
    public long playerPos;
    public Boolean Ready = true;
    private DummyContent.DummyItem mItem;
    Step step;
    public CakeDetailFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            step = getArguments().getParcelable(ARG_ITEM_ID);
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(step.getShortDescription());
            }
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cake_detail, container, false);
        if (step != null) {
            exoPlayer = rootView.findViewById(R.id.exoPlayer_view);
            ((TextView) rootView.findViewById(R.id.cake_detail2)).setText(step.getDescription());
            if (step.getVideoURL().isEmpty()) {
                exoPlayer.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(), "There is no video for this step", Toast.LENGTH_SHORT).show();
            }
            else{
                try {
                    BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                    TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                    exoPlay = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);
                    exoPlayer.setPlayer(exoPlay);
                    Uri videoURI = Uri.parse(step.getVideoURL());
                    DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
                    ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                    MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);
                    exoPlayer.setVisibility(View.VISIBLE);
                    exoPlay.prepare(mediaSource);
                    exoPlay.seekTo(playerPos);
                    exoPlay.setPlayWhenReady(true);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) exoPlayer.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                } catch (Exception e) {
                }
            }
            if (savedInstanceState != null) {
                playerPos = savedInstanceState.getLong("pos");
                exoPlay.seekTo(playerPos);
                Ready = savedInstanceState.getBoolean("whenReady");
                exoPlay.setPlayWhenReady(Ready);

            }
        }
        return rootView;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (exoPlayer != null) {
            outState.putLong("pos", exoPlay.getCurrentPosition());
            outState.putBoolean("Ready", exoPlay.getPlayWhenReady());
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
            exoPlayer = null;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
    private void releasePlayer() {
        if (exoPlay != null) {
            exoPlay.stop();
            exoPlay.release();
        }
    }
}
