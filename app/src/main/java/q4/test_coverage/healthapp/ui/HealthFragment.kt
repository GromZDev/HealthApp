package q4.test_coverage.healthapp.ui

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension
import q4.test_coverage.healthapp.R
import q4.test_coverage.healthapp.base.BaseFragment
import q4.test_coverage.healthapp.databinding.FragmentHealthBinding
import q4.test_coverage.healthapp.model.HealthData
import q4.test_coverage.healthapp.utils.ItemTouchHelperCallback
import q4.test_coverage.healthapp.utils.SharedPreferencesHelper
import q4.test_coverage.healthapp.utils.showToast
import q4.test_coverage.healthapp.viewModel.HealthViewModel
import java.text.SimpleDateFormat
import java.util.*

@InternalCoroutinesApi
@KoinApiExtension
class HealthFragment(override val layoutId: Int = R.layout.fragment_health) :
    BaseFragment<FragmentHealthBinding>() {

    companion object {
        fun newInstance() = HealthFragment()
    }

    private val viewModel by viewModel<HealthViewModel>()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var healthAdapter: HealthAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var receivingDataFromDB: MutableList<HealthData>
    private var userRequest: HealthData = HealthData()

    override fun initViews() {
        super.initViews()
        setBottomSheetBehavior(binding.includedBottomSheetLayout.bottomSheetContainerConstraint)

        /** ======= Инитим и Сетим ItemTouchHelper в наш ресайклер для смахивания и таскания ======== */
        recyclerView = binding.healthRv
        recyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerView.setHasFixedSize(true)
        receivingDataFromDB = arrayListOf()
        healthAdapter = HealthAdapter()
        recyclerView.adapter = healthAdapter

        val swipeToDelete = object : ItemTouchHelperCallback(healthAdapter) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {

                healthAdapter.onItemDismiss(viewHolder.adapterPosition)
                showToast("DELETED!!!!!")
            }
        }
        val itemTH = ItemTouchHelper(swipeToDelete)
        itemTH.attachToRecyclerView(recyclerView)
        /** ================================================================================ */

        binding.healthAddingFab.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            binding.constraintContainer.alpha = 0.2f
        }

        binding.includedBottomSheetLayout.addButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            saveDataToDB()
        }

        getHealthDataFromDB()
        setHeader()
    }


    override fun initViewModel() {
        super.initViewModel()
        doInScope {
            viewModel.newRequest.collect {
                if (it != null) {
                    binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                    showToast("Запись сохранена!")
                    binding.includedBottomSheetLayout.pressureOneIt.text?.clear()
                    binding.includedBottomSheetLayout.pressureTwoIt.text?.clear()
                    binding.includedBottomSheetLayout.pulseIt.text?.clear()
                } else if (it == null && userRequest.pressure_first?.isNotEmpty() == true) {
                    showToast("Запись не создана, что-то пошло не так (")
                    binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                }
            }
            viewModel.isStateException.collect { isStateException ->
                if (isStateException != "") {
                    showToast("Запрос не создан, что-то пошло не так (")
                    binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                }
            }
        }
        doInScopeResume {
            viewModel.isStateException.collect { isStateException ->
                if (isStateException != "" && userRequest.pressure_first?.isNotEmpty() == true
                    && userRequest.pressure_second?.isNotEmpty() == true
                    && userRequest.pulse != "-1" && userRequest.date?.isNotEmpty() == true
                ) {
                    view?.showToast("Failed")
                    binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                }
            }
        }

        getListData()
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> binding.constraintContainer.alpha = 1.0f
                    BottomSheetBehavior.STATE_COLLAPSED -> binding.constraintContainer.alpha = 1.0f
                    BottomSheetBehavior.STATE_DRAGGING -> {}
                    BottomSheetBehavior.STATE_EXPANDED -> {}
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {}
                    BottomSheetBehavior.STATE_SETTLING -> {}
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
    }

    private fun getHealthDataFromDB() {
        viewModel.getData("myUid", healthAdapter)
        binding.includedLoadingLayout.loadingLayout.visibility = View.VISIBLE
    }

    private fun saveDataToDB() {
        val pressureFirst = binding.includedBottomSheetLayout.pressureOneIt.text?.toString()?.trim()
        val pressureSecond =
            binding.includedBottomSheetLayout.pressureTwoIt.text?.toString()?.trim()
        val pulse = binding.includedBottomSheetLayout.pulseIt.text?.toString()?.trim()
        if (pressureFirst.isNullOrEmpty() || pressureSecond.isNullOrEmpty() || pulse.isNullOrEmpty()) {
            showToast("Какое-то поле не заполнено")
        }
        if (pressureFirst?.isNotEmpty() == true && pressureSecond?.isNotEmpty() == true && pulse?.isNotEmpty() == true) {
            viewModel.updateHealthData(pressureFirst, pressureSecond, pulse, healthAdapter)
            binding.includedLoadingLayout.loadingLayout.visibility = View.VISIBLE
        }

    }

    private fun getListData() {
        doInScope {
            viewModel.receivingData.collect {
                if (it != null) {
                    binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                } else if (it == null && receivingDataFromDB.isNotEmpty()) {
                    showToast("что-то пошло не так (")
                    binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                } else if (it == null || receivingDataFromDB.isEmpty()) {
                    binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                }
            }
            viewModel.isStateException2.collect { isStateException ->
                if (isStateException != "") {
                    showToast("то-то пошло не так (")
                    binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                }
            }
        }
        doInScopeResume {
            viewModel.isStateException2.collect { isStateException ->
                if (isStateException != "" && receivingDataFromDB.isNotEmpty()
                ) {
                    view?.showToast("Ошибка")
                    binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                }
            }
        }
    }

    /** Проверка для вставки Даты в ресайклере */
    private fun setHeader() {
        val preferences = SharedPreferencesHelper(requireContext())
        if (preferences.isFirstOpen || preferences.currentDate != getCurrentDate()) {
            viewModel.createNodeForDate()
            preferences.isFirstOpen = false
            preferences.currentDate = getCurrentDate()
        }
    }

    private fun getCurrentDate(): String {
        val time = Date()
        val timeFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return timeFormat.format(time)
    }
}