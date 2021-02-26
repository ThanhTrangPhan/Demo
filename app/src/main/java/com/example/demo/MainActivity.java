package com.example.demo;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<modelAudio> list;
    MediaPlayer mediaPlayer;
    double current_pos, total_duration;
    TextView current, total,audio_name;
    Button prev, next;
    Button listSong;
    ToggleButton pause,like;
    SeekBar seekBar;
    int ind = 0;
    Fragment myFragment;
    public static final int PERMISSION_READ = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (checkPermission()) {
            setAudio();
        }

    }



    public void setAudio() {
        current = (TextView) findViewById(R.id.current);
        total = (TextView) findViewById(R.id.total);
        audio_name = (TextView) findViewById(R.id.titleSong);
        prev = findViewById(R.id.skip_prev);
        next = findViewById(R.id.skip_next);
        pause =  findViewById(R.id.play);
        like = findViewById(R.id.like);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        listSong = findViewById(R.id.show_list);

        list = new ArrayList<>();
        mediaPlayer = new MediaPlayer();

        getAudioFiles();


        //seekbar change listner
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                current_pos = seekBar.getProgress();
                mediaPlayer.seekTo((int) current_pos);
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                ind++;
                if (ind < (list.size())) {
                    playAudio(ind);
                } else {
                    ind = 0;
                    playAudio(ind);
                }

            }
        });

        if (!list.isEmpty()) {
            playAudio(ind);
            prevAudio();
            nextAudio();
            setPause();
        }

        listSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment k = new List_Song();

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                //ft.replace(R.id.main_screen,k);
                ft.add(R.id.main_screen,k);
                ft.addToBackStack(null);
                ft.commit();

            }
        });
    }

    private void setPause() {
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();

                } else {
                    mediaPlayer.start();
                }
            }
        });
    }

    private void nextAudio() {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ind < (list.size()-1)) {
                    ind++;
                    playAudio(ind);
                } else {
                    ind = 0;
                    playAudio(ind);
                }
            }
        });
    }

    private void prevAudio() {
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ind > 0) {
                    ind--;
                    playAudio(ind);
                } else {
                    ind = list.size() - 1;
                    playAudio(ind);
                }
            }
        });
    }

    public void playAudio(int audio_index) {

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(this, list.get(audio_index).getaudioUri());
            mediaPlayer.prepare();
            mediaPlayer.start();
            pause.setChecked(false);
            String t = list.get(audio_index).getaudioTitle()+" - "+list.get(audio_index).getaudioArtist();
            audio_name.setText(t);
            ind=audio_index;
        } catch (IOException e) {
            e.printStackTrace();
        }
        setAudioProgress();
    }
    public void setAudioProgress() {
        //get the audio duration
        current_pos = mediaPlayer.getCurrentPosition();
        total_duration = mediaPlayer.getDuration();

        //display the audio duration
        total.setText(timerConversion((long) total_duration));
        current.setText(timerConversion((long) current_pos));
        seekBar.setMax((int) total_duration);
        final Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    current_pos = mediaPlayer.getCurrentPosition();
                    current.setText(timerConversion((long) current_pos));
                    seekBar.setProgress((int) current_pos);
                    handler.postDelayed(this, 1000);
                } catch (IllegalStateException ed){
                    ed.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1000);
    }
    public String timerConversion(long value) {
        String audioTime;
        int dur = (int) value;
        int h = (dur / 3600000);
        int m = (dur / 60000) % 60000;
        int s = dur % 60000 / 1000;

        if (h > 0) {
            audioTime = String.format("%02d:%02d:%02d", h, m, s);
        } else {
            audioTime = String.format("%02d:%02d", m, s);
        }
        return audioTime;
    }

    private void getAudioFiles() {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        //looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {

            do {

                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                modelAudio modelAudio = new modelAudio();
                modelAudio.setaudioTitle(title);
                modelAudio.setaudioArtist(artist);
                modelAudio.setaudioUri(Uri.parse(url));
                modelAudio.setaudioDuration(duration);
                list.add(modelAudio);

            } while (cursor.moveToNext());

        }
        myFragment = new List_Song();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("listSong",list);
        myFragment.setArguments(bundle);

    }
        // cho phep truy nhap bo nho
        public boolean checkPermission() {
            int READ_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if((READ_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ);
                return false;
            }
            return true;
        }

        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            switch (requestCode) {
                case  PERMISSION_READ: {
                    if (grantResults.length > 0 && permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                            Toast.makeText(getApplicationContext(), "Please allow storage permission", Toast.LENGTH_LONG).show();
                        } else {
                            setAudio();
                        }
                    }
                }
            }
        }
     @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer!=null){
            mediaPlayer.stop();
        }
    }
}