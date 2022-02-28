package q4.test_coverage.healthapp.di

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class FireBaseModule(private val context: Context) {

    val options = FirebaseOptions.Builder()
        .build()

    var firebaseApp = FirebaseApp.initializeApp(context, options, "CarApp")
    var provideFirebaseFirestore = FirebaseFirestore.getInstance()
    var provideFirebaseStorageReference = FirebaseStorage.getInstance().reference
}