package com.fsacchi.schoolmate.core.features.home

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.compose.runtime.snapshots.Snapshot.Companion.observe
import androidx.lifecycle.lifecycleScope
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.components.BottomBar
import com.fsacchi.schoolmate.core.extensions.clickListener
import com.fsacchi.schoolmate.core.extensions.createProgressDialog
import com.fsacchi.schoolmate.core.extensions.navTo
import com.fsacchi.schoolmate.core.extensions.startActivity
import com.fsacchi.schoolmate.core.features.home.adapter.DisciplineListAdapter
import com.fsacchi.schoolmate.core.features.home.sheets.DisciplineBottomSheet
import com.fsacchi.schoolmate.core.features.login.LoginActivity
import com.fsacchi.schoolmate.core.features.splash.SplashActivity
import com.fsacchi.schoolmate.core.platform.BaseFragment
import com.fsacchi.schoolmate.data.model.discipline.DisciplineModel
import com.fsacchi.schoolmate.databinding.FragmentDisciplineBinding
import com.fsacchi.schoolmate.databinding.FragmentHomeBinding
import com.fsacchi.schoolmate.databinding.FragmentLoginBinding
import com.fsacchi.schoolmate.presentation.features.DisciplineViewModel
import com.fsacchi.schoolmate.presentation.features.HomeViewModel
import com.fsacchi.schoolmate.presentation.states.DefaultUiState
import com.fsacchi.schoolmate.presentation.states.DisciplineUiState
import com.fsacchi.schoolmate.presentation.states.UserUiState
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class DisciplineFragment : BaseFragment<FragmentDisciplineBinding>() {

    private lateinit var homeActivity: HomeActivity
    private val disciplineViewModel: DisciplineViewModel by inject()
    private val dialog by lazy { createProgressDialog() }
    private val adapter by lazy { DisciplineListAdapter() }

    override val layoutRes: Int
        get() = R.layout.fragment_discipline

    override fun start() {
        homeActivity = (activity as HomeActivity)
        binding.showEmptyState = true
        insertListeners()
        observe()
    }

    private fun bindAdapter(listDiscipline: List<DisciplineModel>) {
        binding.showEmptyState = listDiscipline.isEmpty()

        binding.rvDiscipline.adapter = adapter.apply {
            submitList(listDiscipline)
            rootListener = ::handleAdapter
        }
    }

    private fun handleAdapter(disciplineModel: DisciplineModel) {
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
            DisciplineBottomSheet.newInstance(
                DisciplineModel(),
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
}
