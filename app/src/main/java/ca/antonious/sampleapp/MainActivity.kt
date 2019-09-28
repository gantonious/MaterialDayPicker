package ca.antonious.sampleapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.open_java_sample_button
import kotlinx.android.synthetic.main.activity_main.open_kotlin_sample_button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        open_java_sample_button.setOnClickListener {
            startActivity(Intent(this@MainActivity, JavaSampleActivity::class.java))
        }

        open_kotlin_sample_button.setOnClickListener {
            startActivity(Intent(this@MainActivity, KotlinSampleActivity::class.java))
        }
    }
}
