package q4.test_coverage.healthapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.core.component.KoinApiExtension
import q4.test_coverage.healthapp.ui.HealthFragment

@KoinApiExtension
@InternalCoroutinesApi
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