package smwu.network.team1.protein9_final;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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
                // 사용자가 입력한 값을 가져옵니다.
                String artist = ((EditText) findViewById(R.id.music_artist)).getText().toString();
                String songName = ((EditText) findViewById(R.id.music_name)).getText().toString();

                // music_artist와 music_name 내용을 추가합니다.
                String newSong = artist + " - " + songName;

                // 기존의 songList에 새로운 노래를 추가합니다.
                songList.add(newSong);

                // ArrayAdapter에 데이터 변경을 알립니다.
                adapter.notifyDataSetChanged();

                // EditText의 내용을 비워줍니다.
                ((EditText) findViewById(R.id.music_artist)).setText("");
                ((EditText) findViewById(R.id.music_name)).setText("");
            }
        });
    }
}