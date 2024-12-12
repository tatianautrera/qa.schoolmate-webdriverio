package com.fsacchi.schoolmate.core.features.home

import androidx.navigation.fragment.navArgs
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.extensions.capitalizeFirstLetter
import com.fsacchi.schoolmate.core.platform.BaseFragment
import com.fsacchi.schoolmate.core.platform.PagerAdapter
import com.fsacchi.schoolmate.databinding.FragmentDisciplineDetailBinding
import com.fsacchi.schoolmate.databinding.FragmentDisciplineFilesBinding
import com.fsacchi.schoolmate.presentation.features.DisciplineViewModel
import org.koin.android.ext.android.inject

class DisciplineFilesFragment : BaseFragment<FragmentDisciplineFilesBinding>() {

    override val layoutRes: Int
        get() = R.layout.fragment_discipline_files

    override fun start() {}
}
