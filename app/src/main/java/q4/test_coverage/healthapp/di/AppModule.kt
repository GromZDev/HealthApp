package q4.test_coverage.healthapp.di

import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module
import q4.test_coverage.healthapp.viewModel.HealthViewModel

@OptIn(KoinApiExtension::class)
val appModule = module {

    //FireBase
    single { FirebaseApp.initializeApp(androidApplication()) }
    factory { FirebaseDatabase.getInstance().reference }
    single { FirebaseFirestore.getInstance() }
    factory { FirebaseStorage.getInstance().reference }
    single { FirebaseStorage.getInstance() }
    single { FireBaseModule(androidApplication()) }


    viewModel { HealthViewModel(androidApplication(), get()) }
}