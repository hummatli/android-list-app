package com.mobline.presentation.flow.main.content.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobline.presentation.base.BaseFragment
import com.mobline.presentation.common.RecyclerViewDisabler
import com.mobline.presentation.extensions.makeInvisible
import com.mobline.presentation.extensions.makeVisible
import com.mobline.presentation.flow.main.content.main.adapter.RecipeAdapter
import com.mobline.marleyspoon.presentation.R
import com.mobline.marleyspoon.presentation.databinding.FragmentMainPageBinding
import kotlin.reflect.KClass


class MainPageFragment :
    BaseFragment<State, Effect, MainPageViewModel, FragmentMainPageBinding>() {

    override val vmClazz: KClass<MainPageViewModel>
        get() = MainPageViewModel::class
    override val bindingCallback: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMainPageBinding
        get() = FragmentMainPageBinding::inflate

    private val rvDisabler = RecyclerViewDisabler()

    private val recipesListAdapter by lazy {
        RecipeAdapter(
            ArrayList(),
            onItemClicked = {
                viewModel.openDetails(it)
            }
        )
    }

    override val bindViews: FragmentMainPageBinding.() -> Unit = {
        initViews()
        initSubscribers()
    }

    override fun observeState(state: State) {
        when (state) {
            is State.FetchRecipesSuccess -> {
                state.recipes.let { recipes ->
                    recipesListAdapter.setData(recipes)
                    setScreenMode(recipes.isEmpty())
                }
            }
            is State.FetchRecipesError -> {
                setScreenMode(true)
            }
        }
    }

    //Initiates views
    private fun initViews() = with(binding) {
        lytRecipes.rvRecipes.layoutManager = LinearLayoutManager(requireContext())
        lytRecipes.rvRecipes.adapter = recipesListAdapter
    }

    //Initiates subscribers
    private fun initSubscribers() {
        viewModel.isLoading.observe(viewLifecycleOwner) { setLoading(it) }
    }

    private fun setLoading(isLoading: Boolean) = with(binding) {
        if (isLoading) {
            lytRoot.alpha = 0.4F
            pbLoading.makeVisible()
        } else {
            lytRoot.alpha = 1F
            pbLoading.makeInvisible()
        }
        pbLoading.isIndeterminate = isLoading

        if (isLoading) {
            lytRecipes.rvRecipes.addOnItemTouchListener(rvDisabler)
        } else {
            lytRecipes.rvRecipes.removeOnItemTouchListener(rvDisabler)
        }
    }

    private fun setScreenMode(isEmpty: Boolean) {
        binding.lytRecipes.root.visibility = if (isEmpty) View.GONE else View.VISIBLE
        binding.lytEmpty.root.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }
}
