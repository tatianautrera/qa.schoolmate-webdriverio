package com.fsacchi.schoolmate.core.features.home

import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.components.BottomBar
import com.fsacchi.schoolmate.core.extensions.addVerticalDivider
import com.fsacchi.schoolmate.core.extensions.capitalizeFirstLetter
import com.fsacchi.schoolmate.core.extensions.clickListener
import com.fsacchi.schoolmate.core.extensions.createProgressDialog
import com.fsacchi.schoolmate.core.extensions.navTo
import com.fsacchi.schoolmate.core.features.home.adapter.DisciplineListAdapter
import com.fsacchi.schoolmate.core.features.home.sheets.DisciplineBottomSheet
import com.fsacchi.schoolmate.core.features.home.sheets.OptionsBottomSheet
import com.fsacchi.schoolmate.core.platform.BaseFragment
import com.fsacchi.schoolmate.data.model.discipline.DisciplineModel
import com.fsacchi.schoolmate.databinding.FragmentDisciplineBinding
import com.fsacchi.schoolmate.databinding.FragmentDisciplineDetailBinding
import com.fsacchi.schoolmate.presentation.features.DisciplineViewModel
import com.fsacchi.schoolmate.presentation.states.DisciplineUiState
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class DisciplineDetailFragment : BaseFragment<FragmentDisciplineDetailBinding>() {

    private lateinit var homeActivity: HomeActivity
    private val disciplineViewModel: DisciplineViewModel by inject()
    private val args by navArgs<DisciplineDetailFragmentArgs>()

    override val layoutRes: Int
        get() = R.layout.fragment_discipline_detail

    override fun start() {
        homeActivity = (activity as HomeActivity)
        binding.disciplineModel = args.disciplineModel
        homeActivity.setCustomTitle(args.disciplineModel.name.capitalizeFirstLetter())
        homeActivity.showBackIcon(true)
        insertListeners()
    }

    private fun insertListeners() {
        homeActivity.listenerBack = {
            popBackStack()
        }
    }
}
