package com.fsacchi.schoolmate.core.features.home

import androidx.navigation.fragment.navArgs
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.extensions.capitalizeFirstLetter
import com.fsacchi.schoolmate.core.platform.BaseFragment
import com.fsacchi.schoolmate.core.platform.PagerAdapter
import com.fsacchi.schoolmate.databinding.FragmentDisciplineDetailBinding
import com.fsacchi.schoolmate.presentation.features.DisciplineViewModel
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
        startTabAdapter()
    }

    private fun startTabAdapter() {
        val adapter = PagerAdapter(childFragmentManager)

        val jobFragment = DisciplineJobsFragment()
        jobFragment.setDisciplineSelected(args.disciplineModel)
        val filesFragment = DisciplineFilesFragment()
        filesFragment.setDisciplineSelected(args.disciplineModel)


        adapter.addTitle(listOf(getString(R.string.jobs), getString(R.string.files)))
        adapter.addFragment(jobFragment)
        adapter.addFragment(filesFragment)

        binding.viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    private fun insertListeners() {
        homeActivity.listenerBack = {
            popBackStack()
        }
    }
}
