package com.fsacchi.schoolmate.core.features.home

import androidx.navigation.fragment.navArgs
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.extensions.capitalizeFirstLetter
import com.fsacchi.schoolmate.core.extensions.clickListener
import com.fsacchi.schoolmate.core.features.home.sheets.FileBottomSheet
import com.fsacchi.schoolmate.core.features.home.sheets.JobBottomSheet
import com.fsacchi.schoolmate.core.features.home.sheets.UploadFileBottomSheet
import com.fsacchi.schoolmate.core.platform.BaseFragment
import com.fsacchi.schoolmate.core.platform.PagerAdapter
import com.fsacchi.schoolmate.data.model.discipline.DisciplineModel
import com.fsacchi.schoolmate.data.model.file.FileUserModel
import com.fsacchi.schoolmate.databinding.FragmentDisciplineDetailBinding
import com.fsacchi.schoolmate.databinding.FragmentDisciplineFilesBinding
import com.fsacchi.schoolmate.presentation.features.DisciplineViewModel
import org.koin.android.ext.android.inject

class DisciplineFilesFragment : BaseFragment<FragmentDisciplineFilesBinding>() {

    private lateinit var disciplineSelected: DisciplineModel
    private lateinit var homeActivity: HomeActivity

    override val layoutRes: Int
        get() = R.layout.fragment_discipline_files

    override fun start() {
        homeActivity = (activity as HomeActivity)
        binding.showEmptyState = true
        insertListeners()
    }

    private fun insertListeners() {
        binding.btnCreateFile.clickListener{
            UploadFileBottomSheet.newInstance().setListener {
                FileBottomSheet.newInstance(
                    disciplineSelected,
                    fileUserModel = FileUserModel(),
                    fileModel = it,
                    homeActivity.user.uid,
                    successListener = {
                        // TODO: carrega os arquivos da disciplina
                    },
                    errorListener = { errorMessage ->
                        homeActivity.showAlertMessage(
                            isError = true,
                            title = "Erro ao salvar arquivo",
                            message = errorMessage

                        )
                    }
                ).show(childFragmentManager)
            }.show(childFragmentManager)
        }
    }

    fun setDisciplineSelected(disciplineModel: DisciplineModel) {
        disciplineSelected = disciplineModel
    }
}
