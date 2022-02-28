package q4.test_coverage.healthapp.ui

import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension
import q4.test_coverage.healthapp.R
import q4.test_coverage.healthapp.base.BaseFragment
import q4.test_coverage.healthapp.databinding.FragmentHealthBinding
import q4.test_coverage.healthapp.viewModel.HealthViewModel

@KoinApiExtension
class HealthFragment(override val layoutId: Int = R.layout.fragment_health) :
    BaseFragment<FragmentHealthBinding>() {

    companion object {
        fun newInstance() = HealthFragment()
    }

    private val viewModel by viewModel<HealthViewModel>()
}