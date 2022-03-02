package q4.test_coverage.healthapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserData(
    var uid: String = "",
    var healthDataList: List<HealthData>? = null,
) : Parcelable

@Parcelize
data class HealthData(
    var date: String? = "",
    var time: String? = "",
    var pressure_first: String? = "",
    var pressure_second: String? = "",
    var pulse: String? = "",
    val type: HEADER? = null
): Parcelable {

    enum class HEADER {
        HEADER
    }
}