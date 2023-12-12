package smwu.network.team1.protein9_final

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import smwu.network.team1.protein9_final.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
        setContentView(R.layout.activity_main)
    }
}