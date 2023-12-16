package smwu.network.team1.protein9_final;

import android.app.Activity;
import android.content.Intent;
import android.os.*;
import android.view.*;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class PlaylistActivity extends AppCompatActivity {

    private Button buttonGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        String[] songs = getResources().getStringArray(R.array.song_list);
        ArrayList<String> songList = new ArrayList<>(Arrays.asList(songs));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, songList);

        ListView listView = findViewById(R.id.playlist);
        listView.setAdapter(adapter);

        buttonGo = findViewById(R.id.button_go);
        buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(PlaylistActivity.this, MainActivity.class);
//                startActivity(intent);
                EditText musicNameEditText = findViewById(R.id.music_name);
                EditText musicArtistEditText = findViewById(R.id.music_artist);
                String musicName = musicNameEditText.getText().toString();
                String musicArtist = musicArtistEditText.getText().toString();

                // 서버로 데이터를 전송하는 AsyncTask 실행
                new SendDataToServerTask().execute("SONG_REQUEST", musicName, musicArtist);
            }
        });

        new SendDataToServerTask().execute("REQUEST_PLAYLIST");
    }
}
