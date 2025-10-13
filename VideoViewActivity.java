package finix.social.finixapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.appcompat.widget.Toolbar;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.StyledPlayerView;

import finix.social.finixapp.R;
import finix.social.finixapp.common.ActivityBase;

public class VideoViewActivity extends ActivityBase {

    Toolbar toolbar;

    private StyledPlayerView mVideoView;
    private GestureDetector gestureDetector;
    private ProgressBar mProgressBar;
    private ImageButton mCloseButton;
    private ExoPlayer mPlayer;

    String videoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video_view);

        getSupportActionBar().hide();

        Intent i = getIntent();

        videoUrl = i.getStringExtra("videoUrl");

        mCloseButton = findViewById(R.id.close_button);
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mPlayer != null) {
                    mPlayer.setPlayWhenReady(false);
                    mPlayer.stop();
                }

                finish();
            }
        });

        mProgressBar = findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        mVideoView = findViewById(R.id.video_view);

        mPlayer = new ExoPlayer.Builder(this).build();
        // mPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);

        mVideoView.setUseController(true);
        mVideoView.requestFocus();
        mVideoView.setPlayer(mPlayer);
        mVideoView.setControllerAutoShow(false);
        mVideoView.setShowNextButton(false);
        mVideoView.setShowPreviousButton(false);
        mVideoView.setShowFastForwardButton(false);
        mVideoView.setShowRewindButton(false);
        mVideoView.setShowMultiWindowTimeBar(false);
        mVideoView.setShowBuffering(StyledPlayerView.SHOW_BUFFERING_NEVER);

        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(videoUrl));

        if (mPlayer != null) {
            mPlayer.addMediaItem(mediaItem);
            mPlayer.prepare();
            mPlayer.setPlayWhenReady(true);

            mPlayer.addListener(new Player.Listener() {

                @Override
                public void onPlaybackStateChanged(@Player.State int state) {

                    if (state == Player.STATE_BUFFERING) {
                        mProgressBar.setVisibility(View.VISIBLE);
                    } else {
                        mProgressBar.setVisibility(View.GONE);
                    }
                }
            });
        }

        // ---- Double Tap and Single Tap GestureDetector Implementation ----
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                // Example: show a heart or like
                Toast.makeText(VideoViewActivity.this, "Double tap!", Toast.LENGTH_SHORT).show();
                // Optionally, call a showHeartAnimation() method here
                return true;
            }
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                // Optional: Show/hide controls, pause/play, etc.
                if (mVideoView != null) {
                    mVideoView.showController();
                }
                return true;
            }
        });

        mVideoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                // Return true so gestureDetector exclusively handles the event
                return true;
            }
        });
        // ---- End GestureDetector Implementation ----
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (mPlayer != null) {
            mPlayer.setPlayWhenReady(false);
            mPlayer.stop();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home: {

                if (mPlayer != null) {
                    mPlayer.setPlayWhenReady(false);
                    mPlayer.stop();
                }

                finish();

                return true;
            }

            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }
}