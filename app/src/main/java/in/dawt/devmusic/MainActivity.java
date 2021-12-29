package in.dawt.devmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    MediaPlayer mp;
    ImageView  poster, previous, playpause, next, fastforward, rewind;
    TextView songTitleView, singersView, upnext, duration, currenttime;
    SeekBar seekBar;
    int currentSongIndex;


    Song[] songs = {
        new Song(
            "In Da Getto",
                new String[]{"J Balvin", "Skrillex"},
                R.drawable.in_da_getto,
                R.raw.in_da_getto
        ),

        new Song(
                "Manike Mage Hithe",
                new String[]{"Yohani De Silva", "Satheesan"},
                R.drawable.manike_mage_hithe,
                R.raw.manike_mage_hithe
        ),

        new Song(
                "Awidan Yanawa",
                new String[]{"Yohani De Silva", "Funky Dirt"},
                R.drawable.awidan_yanawa,
                R.raw.awidan_yanawa
        ),

        new Song(
                "Temperature",
                new String[]{"Sean Paul"},
                R.drawable.temperature,
                R.raw.temperature
        ),

        new Song(
                "On and On",
                new String[]{"Cartoon", "Daniel Levi"},
                R.drawable.on_and_on,
                R.raw.on_and_on
        )
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get the views and init the state
        currentSongIndex = 0;
        getViews();
        setAppState();

        // handler and runnable for updation
        Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mp.getCurrentPosition());
                currenttime.setText(getDurationTimestamp(mp.getCurrentPosition()));
                handler.postDelayed(this, 1000);
            }
        };

        runnable.run();

        setOnClickListeners();
    }

    protected void getViews() {
        // get the views
        poster = findViewById(R.id.poster);
        songTitleView = findViewById(R.id.songtitle);
        singersView = findViewById(R.id.singers);
        upnext = findViewById(R.id.upnext);
        playpause = findViewById(R.id.playpause);
        duration = findViewById(R.id.duration);
        currenttime = findViewById(R.id.currenttime);
        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);
        fastforward = findViewById(R.id.fastforawrd);
        rewind = findViewById(R.id.rewind);
        seekBar = findViewById(R.id.seekBar);
    }

    protected void setAppState() {

        // set poster
        poster.setImageResource(songs[currentSongIndex].posterImage);

        // set title and singers
        songTitleView.setText(songs[currentSongIndex].title);
        singersView.setText(songs[currentSongIndex].getSingersAsString());

        // set media player
        mp = MediaPlayer.create(getApplicationContext(), songs[currentSongIndex].audioFile);

        // set seekbar max val
        seekBar.setMax(mp.getDuration());

        // set duration
        duration.setText(getDurationTimestamp(mp.getDuration()));

        // start the song and set playpause button to pause
        playpause.setImageResource(R.drawable.pause);
        mp.start();

        // on complete listener
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

                if (currentSongIndex == songs.length - 1) {
                    currentSongIndex = 0;
                } else {
                    currentSongIndex = currentSongIndex + 1;
                }

                setAppState();
            }
        });


        // set the next song
        if (currentSongIndex == songs.length - 1) {
            upnext.setText("Up Next: " + songs[0].title);
        } else {
            upnext.setText("Up Next: " + songs[currentSongIndex + 1].title);
        }


    }

    protected void setOnClickListeners() {

        // on click listener for playpause button
        playpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mp.isPlaying()) {
                    mp.pause();
                    playpause.setImageResource(R.drawable.play);
                } else {
                    mp.start();
                    playpause.setImageResource(R.drawable.pause);
                }
            }
        });

        // on click listener for next button
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // stop the current song
                mp.stop();
                if (currentSongIndex == songs.length - 1) {
                    currentSongIndex = 0;
                } else {
                    currentSongIndex = currentSongIndex + 1;
                }

                setAppState();

            }
        });

        // on click listener for previous button
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // stop the current song
                mp.stop();

                if (currentSongIndex == 0) {
                    currentSongIndex = songs.length - 1;
                } else {
                    currentSongIndex = currentSongIndex - 1;
                }

                setAppState();

            }
        });

        // fastforward onclick
        fastforward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.seekTo(mp.getCurrentPosition() + 5000);
            }
        });

        // rewind onclick
        rewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.seekTo(mp.getCurrentPosition() - 5000);
            }
        });


        // change time of media if seekbar changed by user
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) { // i=progress b=fromUser
                if (b) {
                    mp.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



    }

    protected String getDurationTimestamp(int millis) {
        int minutes = (millis/1000) / 60;
        int seconds = (millis/1000) % 60;
        return (minutes < 10 ? "0" + minutes : minutes) + ":" + (seconds < 10 ? "0" + seconds : seconds);
    }

}