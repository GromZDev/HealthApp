package q4.test_coverage.healthapp.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension
import q4.test_coverage.healthapp.base.BaseViewModel
import q4.test_coverage.healthapp.model.HealthData
import java.text.SimpleDateFormat
import java.util.*

@KoinApiExtension
class HealthViewModel(
    app: Application,
    private var fireStore: FirebaseFirestore
) : BaseViewModel(app) {
    val newRequest: MutableStateFlow<List<HealthData>?> = MutableStateFlow(null)
    val servicePersonalRequest: MutableStateFlow<List<HealthData>?> =
        MutableStateFlow(null)
    val isStateException = MutableStateFlow("")
    private val isStateException2 = MutableStateFlow("")

//    @SuppressLint("SimpleDateFormat")
//    fun updateProfileUser(
//        serviceTheme: String,
//        serviceOverview: String,
//        servicePrice: String,
//        adapter: RequestPersonalServicesAdapter
//    ) {
//        modelScope.launch {
//            try {
//                val calendar = Calendar.getInstance()
//                val dateFormat = SimpleDateFormat("dd-MM-yyyy")
//                val date = dateFormat.format(calendar.time)
//                val request = HealthData(
//                    serviceTheme,
//                    serviceOverview,
//                    servicePrice,
//                    date
//                )
//                CommonConstants.USER?.personalServices = listOf(request)
//
//                updateUserProfileRequests(adapter)
//                delay(1000)
//            } catch (exception: TimeoutCancellationException) {
//                isStateException.value = "1 - " + exception.message
//                newRequest.value = null
//
//            } catch (exception: Exception) {
//                isStateException.value = "2 - " + exception.message
//                newRequest.value = null
//            }
//        }
//    }
//
//    private fun updateUserProfileRequests(adapter: RequestPersonalServicesAdapter) {
//        modelScope.launch {
//            val user = HealthData(
//                uid = CommonConstants.USER?.uid.toString(),
//                personalServices = CommonConstants.USER?.personalServices
//            )
//
//            val service = user.personalServices?.get(0)?.let {
//                HealthData(
//                    user.personalServices!![0].theme, user.personalServices!![0].overview,
//                    user.personalServices!![0].price
//                )
//            }
//            Log.d("RequestServicesFragment", user.toString())
//            val collection = fireStore.collection("users")
//            val document = collection.document(user.uid)
//            document
//                .update("personalServices", FieldValue.arrayUnion(service))
//                .addOnSuccessListener {
//                    newRequest.value = user.personalServices
//                    if (service != null) {
//                        adapter.appendItem(service)
//                    }
//                }
//                .addOnFailureListener { newRequest.value = null }
//                .await()
//        }
//    }
//
//    fun getUserServiceRequests(uid: String, adapter: RequestPersonalServicesAdapter) {
//        modelScope.launch {
//            try {
//                if (uid.isNotEmpty()) {
//                    eventChangeListener(uid, adapter)
//                    delay(1000)
//                }
//            } catch (exception: TimeoutCancellationException) {
//                isStateException2.value = "1 - " + exception.message
//                servicePersonalRequest.value = null
//
//            } catch (exception: Exception) {
//                isStateException2.value = "2 - " + exception.message
//                servicePersonalRequest.value = null
//            }
//        }
//    }
//
//    private fun eventChangeListener(currentUser: String, adapter: RequestPersonalServicesAdapter) {
//        modelScope.launch {
//            fireStore = FirebaseFirestore.getInstance()
//            fireStore.collection("users").document(currentUser)
//                .get()
//                .addOnSuccessListener { link ->
//                    val user = link.toObject<HealthData>()
//                    if (user?.personalServices !== null) {
//                        servicePersonalRequest.value = user?.personalServices!!
//                        adapter.setAllRequests(user.personalServices!! as MutableList<PersonalServicesRequests>)
//                    }
//
//                }
//                .addOnFailureListener {
//                    servicePersonalRequest.value = null
//                }
//                .await()
//        }
//    }
}