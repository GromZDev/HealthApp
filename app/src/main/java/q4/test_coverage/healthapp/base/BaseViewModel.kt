package q4.test_coverage.healthapp.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent

@KoinApiExtension
open class BaseViewModel(app: Application) : AndroidViewModel(app), KoinComponent {

    private val uiDispatcher = Dispatchers.Main.immediate
    private val job = CoroutineExceptionHandler { _, _ -> }
    val modelScope = CoroutineScope(uiDispatcher + job)
}