package cz.cvut.fel.kopecm26.bakaplanner.ui.fragments

import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentHomeBinding
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.HomeViewModel

class HomeFragment: ViewModelFragment<HomeViewModel, FragmentHomeBinding>(R.layout.fragment_home, HomeViewModel::class) {
}