package com.mobline.presentation.flow.main

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.mobline.marleyspoon.presentation.R
import com.mobline.presentation.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity(R.layout.activity_main) {

    private val viewModel by viewModel<MainViewModel>()

    private val navController: NavController by lazy {
        findNavController(R.id.main_nav_fragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController.setGraph(R.navigation.main_graph)
    }
}