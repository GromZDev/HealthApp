package q4.test_coverage.healthapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import q4.test_coverage.healthapp.ui.HealthFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.fragment_container,
                    HealthFragment.newInstance()
                )
                .commit()
        }
    }
}