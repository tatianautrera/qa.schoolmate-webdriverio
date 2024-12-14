package com.fsacchi.schoolmate.core.features.home

import androidx.navigation.fragment.navArgs
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.extensions.capitalizeFirstLetter
import com.fsacchi.schoolmate.core.extensions.clickListener
import com.fsacchi.schoolmate.core.features.home.sheets.DisciplineBottomSheet
import com.fsacchi.schoolmate.core.features.home.sheets.JobBottomSheet
import com.fsacchi.schoolmate.core.platform.BaseFragment
import com.fsacchi.schoolmate.core.platform.PagerAdapter
import com.fsacchi.schoolmate.data.model.discipline.DisciplineModel
import com.fsacchi.schoolmate.data.model.job.JobModel
import com.fsacchi.schoolmate.databinding.FragmentDisciplineDetailBinding
import com.fsacchi.schoolmate.databinding.FragmentDisciplineJobsBinding
import com.fsacchi.schoolmate.presentation.features.DisciplineViewModel
import org.koin.android.ext.android.inject

class DisciplineJobsFragment : BaseFragment<FragmentDisciplineJobsBinding>() {

    private lateinit var disciplineSelected: DisciplineModel
    private lateinit var homeActivity: HomeActivity

    override val layoutRes: Int
        get() = R.layout.fragment_discipline_jobs

    override fun start() {
        homeActivity = (activity as HomeActivity)
        insertListeners()
    }

    fun setDisciplineSelected(disciplineModel: DisciplineModel) {
        disciplineSelected = disciplineModel
    }

    private fun insertListeners() {
        binding.btnCreateJob.clickListener {
            JobBottomSheet.newInstance(
                disciplineSelected,
                jobModel = JobModel(),
                homeActivity.user.uid,
                successListener = {
                    val jobModel = it
                },
                errorListener = { errorMessage ->
                    homeActivity.showAlertMessage(
                        isError = true,
                        title = "Erro ao cadastrar atividade",
                        message = errorMessage

                    )
                }
            ).show(childFragmentManager)
        }
    }
}
