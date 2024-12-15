package com.fsacchi.schoolmate.core.features.home

import androidx.lifecycle.lifecycleScope
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.components.BottomBar
import com.fsacchi.schoolmate.core.extensions.capitalizeFirstLetter
import com.fsacchi.schoolmate.core.extensions.clickListener
import com.fsacchi.schoolmate.core.extensions.createProgressDialog
import com.fsacchi.schoolmate.core.extensions.navTo
import com.fsacchi.schoolmate.core.extensions.showMessage
import com.fsacchi.schoolmate.core.features.home.adapter.DisciplineListAdapter
import com.fsacchi.schoolmate.core.features.home.sheets.DisciplineBottomSheet
import com.fsacchi.schoolmate.core.features.home.sheets.OptionsBottomSheet
import com.fsacchi.schoolmate.core.platform.BaseFragment
import com.fsacchi.schoolmate.data.model.discipline.DisciplineModel
import com.fsacchi.schoolmate.databinding.FragmentDisciplineBinding
import com.fsacchi.schoolmate.presentation.features.DisciplineViewModel
import com.fsacchi.schoolmate.presentation.states.DefaultUiState
import com.fsacchi.schoolmate.presentation.states.DisciplineUiState
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class DisciplineFragment : BaseFragment<FragmentDisciplineBinding>() {

    private lateinit var homeActivity: HomeActivity
    private val disciplineViewModel: DisciplineViewModel by inject()
    private val dialog by lazy { createProgressDialog() }
    private val adapter by lazy { DisciplineListAdapter() }
    private lateinit var disciplineSelected: DisciplineModel

    override val layoutRes: Int
        get() = R.layout.fragment_discipline

    override fun start() {
        homeActivity = (activity as HomeActivity)
        homeActivity.showBackIcon(false)
        homeActivity.setCustomTitle(getString(R.string.discipline))
        binding.showEmptyState = true
        insertListeners()
        observe()
    }

    private fun bindAdapter(listDiscipline: List<DisciplineModel>) {
        binding.showEmptyState = listDiscipline.isEmpty()

        binding.rvDiscipline.adapter = adapter.apply {
            submitList(listDiscipline)
            rootListener = ::handleAdapter
            listenerOptions = {
                disciplineSelected = it
                showOptionsMenu()
            }
        }
    }

    private fun showOptionsMenu() {
        OptionsBottomSheet
            .newInstance(disciplineMenuItems)
            .setListener(::handleOptionsMenu)
            .show(childFragmentManager)
    }

    private fun handleOptionsMenu(optionItem: OptionItem) {
        when(optionItem.desc) {
            R.string.see_details -> {
                handleAdapter(disciplineSelected)
            }
            R.string.edit -> {
                showDisciplineBottomSheet(disciplineSelected)
            }
            R.string.delete -> {
                showMessage {
                    title(R.string.warning)
                    message(getString(R.string.confirm_delete_discipline, disciplineSelected.name.capitalizeFirstLetter()))
                    positiveListener {
                        disciplineViewModel.deleteDisciplineModel(disciplineSelected, homeActivity.user.uid)
                    }
                    negativeListener {}
                }
            }
        }
    }

    private fun handleAdapter(disciplineModel: DisciplineModel) {
        navTo(DisciplineFragmentDirections.goToDisciplineDetail(disciplineModel))
    }

    private fun observe() {
        lifecycleScope.launch {
            disciplineViewModel.uiState.disciplines.collect { disciplineUiState ->
                disciplineUiState.let {
                    when(it.screenType) {
                        DisciplineUiState.ScreenType.Await -> {
                            dialog.show()
                        }
                        is DisciplineUiState.ScreenType.Error -> {
                            dialog.dismiss()
                            homeActivity.showAlertMessage(
                                isError = true,
                                title = it.screenType.errorTitle ?: "Erro ao carregar disciplinas",
                                message = it.screenType.errorMessage.orEmpty()
                            )
                        }
                        is DisciplineUiState.ScreenType.Loaded -> {
                            dialog.dismiss()
                            bindAdapter(it.screenType.listDisciplines)
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            disciplineViewModel.uiState.deleteDiscipline.collect { deleteUiState ->
                deleteUiState?.let {
                    when(it.screenType) {
                        is DefaultUiState.ScreenType.Error -> {
                            dialog.dismiss()
                            homeActivity.showAlertMessage(
                                isError = true,
                                title = it.screenType.errorTitle ?: "Erro ao deletar disciplina",
                                message = it.screenType.errorMessage.orEmpty()
                            )
                        }
                        DefaultUiState.ScreenType.Loading -> {
                            dialog.show()
                        }
                        DefaultUiState.ScreenType.Success -> {
                            disciplineViewModel.getDisciplines(homeActivity.user.uid)
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        disciplineViewModel.getDisciplines(homeActivity.user.uid)
    }

    private fun insertListeners() {
        homeActivity.menuSelected {
            when(it) {
                BottomBar.MenuBottom.DISCIPLINE -> {}
                BottomBar.MenuBottom.AGENDA -> {
                    navTo(DisciplineFragmentDirections.goToHome())
                }
                BottomBar.MenuBottom.FILE -> {
                    navTo(DisciplineFragmentDirections.goToFile())
                }
            }
        }

        binding.btnCreateDiscipline.clickListener {
            showDisciplineBottomSheet(DisciplineModel())
        }
    }

    private fun showDisciplineBottomSheet(disciplineModel: DisciplineModel) {
        DisciplineBottomSheet.newInstance(
            disciplineModel,
            homeActivity.user.uid,
            successListener = {
                disciplineViewModel.getDisciplines(homeActivity.user.uid)
            },
            errorListener = { errorMessage ->
                homeActivity.showAlertMessage(
                    isError = true,
                    title = "Erro ao gravar disciplina",
                    message = errorMessage

                )
            }
        ).show(childFragmentManager)
    }
}
