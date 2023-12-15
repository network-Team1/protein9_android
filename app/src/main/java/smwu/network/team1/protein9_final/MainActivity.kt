package smwu.network.team1.protein9_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import smwu.network.team1.protein9_final.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var m_intent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainChatBtn.setOnClickListener {
            m_intent = Intent(this, ChatActivity::class.java)
            startActivity(m_intent)
        }

        binding.mainListenPlayListBtn.setOnClickListener {
            m_intent = Intent(this, PlaylistActivity::class.java)
            startActivity(m_intent)
        }
    }
}

