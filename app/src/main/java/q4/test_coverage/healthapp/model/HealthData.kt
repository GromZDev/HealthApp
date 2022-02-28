package q4.test_coverage.healthapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HealthData(
    var uid: String = "",
    var name: String = "",
    var lastName: String = "",
    var nickName: String = "",
    var email: String = "",
): Parcelable