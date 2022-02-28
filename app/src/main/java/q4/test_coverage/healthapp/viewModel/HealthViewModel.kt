package q4.test_coverage.healthapp.viewModel

import android.annotation.SuppressLint
import android.app.Application
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.core.component.KoinApiExtension
import q4.test_coverage.healthapp.base.BaseViewModel
import q4.test_coverage.healthapp.model.HealthData
import q4.test_coverage.healthapp.model.UserData
import q4.test_coverage.healthapp.ui.HealthAdapter
import q4.test_coverage.healthapp.utils.CommonConstants
import java.text.SimpleDateFormat

import java.util.*

@KoinApiExtension
class HealthViewModel(
    app: Application,
    private var fireStore: FirebaseFirestore
) : BaseViewModel(app) {
    private val newHealthData: MutableStateFlow<UserData?> = MutableStateFlow(null)
    val newRequest: MutableStateFlow<List<HealthData>?> = MutableStateFlow(null)
    val receivingData: MutableStateFlow<List<HealthData>?> = MutableStateFlow(null)
    val isStateException = MutableStateFlow("")
    val isStateException2 = MutableStateFlow("")

    @SuppressLint("SimpleDateFormat")
    fun updateHealthData(
        pressureFirst: String,
        pressureSecond: String,
        pulse: String,
        healthAdapter: HealthAdapter
    ) {
        modelScope.launch {
            try {
                val calendar = Calendar.getInstance()
                val dateFormat = SimpleDateFormat("dd-MM-yyyy")
                val date = dateFormat.format(calendar.time)
                val request = HealthData(
                    pressure_first = pressureFirst,
                    pressure_second = pressureSecond,
                    pulse = pulse,
                    date = date
                )
                CommonConstants.HEALTH_DATA?.healthDataList = listOf(request)
                //  createNode()
                updateDataToDB(request, healthAdapter)
                delay(1000)
            } catch (exception: TimeoutCancellationException) {
                isStateException.value = "1 - " + exception.message
                newRequest.value = null

            } catch (exception: Exception) {
                isStateException.value = "2 - " + exception.message
                newRequest.value = null
            }
        }
    }

    private fun createNode() {
        modelScope.launch {
            val data = UserData(
                uid = "myUid",
                healthDataList = null
            )
            val collection = fireStore.collection("health_data")
            val document = collection.document("myUid")
            document
                .set(
                    data
                )
                .addOnSuccessListener { newHealthData.value = data }
                .addOnFailureListener { newHealthData.value = null }
                .await()
        }
    }

    private fun updateDataToDB(request: HealthData, adapter: HealthAdapter) {
        modelScope.launch {
            val user = HealthData(
                request.date,
                request.time,
                request.pressure_first,
                request.pressure_second,
                request.pulse
            )

            val collection = fireStore.collection("health_data")
            val document = collection.document("myUid")
            document
                .update("healthDataList", FieldValue.arrayUnion(user))
                .addOnSuccessListener {
                    newRequest.value = listOf(user)
                    user.let { it1 -> adapter.appendItem(it1) }
                }
                .addOnFailureListener { newRequest.value = null }
                .await()
        }
    }

    fun getData(uid: String, adapter: HealthAdapter) {
        modelScope.launch {
            try {
                if (uid.isNotEmpty()) {
                    eventChangeListener(uid, adapter)
                    delay(1000)
                }
            } catch (exception: TimeoutCancellationException) {
                isStateException2.value = "1 - " + exception.message
                receivingData.value = null

            } catch (exception: Exception) {
                isStateException2.value = "2 - " + exception.message
                receivingData.value = null
            }
        }
    }

    private fun eventChangeListener(currentUser: String, adapter: HealthAdapter) {
        modelScope.launch {
            fireStore = FirebaseFirestore.getInstance()
            fireStore.collection("health_data").document(currentUser)
                .get()
                .addOnSuccessListener { link ->
                    val healthList = link.toObject<UserData>()
                    if (healthList?.healthDataList !== null) {
                        receivingData.value = healthList.healthDataList
                        adapter.setAllHealthData(healthList.healthDataList as MutableList<HealthData>)
                    }

                }
                .addOnFailureListener {
                    receivingData.value = null
                }
                .await()
        }
    }
}