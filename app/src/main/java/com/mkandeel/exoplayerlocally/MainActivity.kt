package com.mkandeel.exoplayerlocally

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.mkandeel.exoplayerlocally.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isFullScreen = false
    private var isPlay = true
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var playerView: PlayerView
    private lateinit var btn_play: ImageButton
    private lateinit var fullscreenIcon: ImageButton
    private lateinit var videoController: ConstraintLayout
    private lateinit var controllerLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playerView = findViewById(R.id.player_view)
        setupViews()

        val videos = listOf(
            R.raw.video,
            R.raw.video_2
        )

        exoPlayer = ExoPlayer.Builder(this).build()
        playerView.player = exoPlayer

        // play random video from list of videos
        val videoUri = Uri.parse("android.resource://" + packageName + "/" + videos.random())
        // Create a MediaItem
        val mediaItem = MediaItem.fromUri(videoUri)
        // Set the media item to be played
        exoPlayer.setMediaItem(mediaItem)
        // Prepare the player
        exoPlayer.prepare()
        // Start playing automatically
        exoPlayer.playWhenReady = true

        btn_play.setOnClickListener {
            // pause video
            if (isPlay) {
                btn_play.setImageResource(androidx.media3.ui.R.drawable.exo_icon_play)
                exoPlayer.pause()
            } else {
                btn_play.setImageResource(androidx.media3.ui.R.drawable.exo_icon_pause)
                exoPlayer.play()
            }
            isPlay = !isPlay
        }

        playerView.setControllerVisibilityListener(PlayerView.ControllerVisibilityListener { visibility ->
            if(visibility == View.VISIBLE) {
                //fullscreenIcon.visibility = View.VISIBLE
                controllerLayout.visibility = View.VISIBLE
                videoController.visibility = View.VISIBLE
            } else {
                controllerLayout.visibility = View.GONE
                videoController.visibility = View.GONE
                //fullscreenIcon.visibility = View.GONE
            }
        })

        fullscreenIcon.setOnClickListener {
            toggleScreen()
        }
    }

    private fun toggleScreen() {
        if (isFullScreen) {
            // Exit full-screen mode
            fullscreenIcon.setImageResource(R.drawable.enter_fullscreen_icon)
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            isFullScreen = false
        } else {
            // Enter full-screen mode
            fullscreenIcon.setImageResource(R.drawable.exit_fullscreen_icon)
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            isFullScreen = true
        }
    }

    private fun setupViews() {
        btn_play = playerView.findViewById(R.id.exo_play)
        fullscreenIcon = playerView.findViewById(R.id.exo_fullscreen_icon)
        videoController = playerView.findViewById(R.id.video_controller_layout)
        controllerLayout = playerView.findViewById(R.id.controller_layout)
    }

    private fun updateFullScreenIcon() {
        if (isFullScreen) {
            fullscreenIcon.setImageResource(R.drawable.exit_fullscreen_icon)
        } else {
            fullscreenIcon.setImageResource(R.drawable.enter_fullscreen_icon)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isFullScreen", isFullScreen) // Save full-screen state
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        isFullScreen = savedInstanceState.getBoolean("isFullScreen", false) // Restore full-screen state
        updateFullScreenIcon()
    }

    override fun onStop() {
        super.onStop()
        exoPlayer.release()
    }
}