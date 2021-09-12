package com.mobline.presentation.flow.main.content.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.mobline.marleyspoon.presentation.R
import com.mobline.marleyspoon.presentation.databinding.FragmentMainPageBinding
import com.mobline.presentation.base.BaseFragment
import com.mobline.presentation.common.RecyclerViewDisabler
import com.mobline.presentation.extensions.*
import com.mobline.presentation.flow.main.content.main.adapter.RecipeAdapter
import com.mobline.presentation.tools.NavigationCommand
import com.mobline.marleyspoon.presentation.databinding.FragmentDetailsPageBinding
import kotlin.reflect.KClass


class DetailsPageFragment :
    BaseFragment<Unit, Unit, DetailsPageViewModel, FragmentDetailsPageBinding>() {

    override val vmClazz: KClass<DetailsPageViewModel>
        get() = DetailsPageViewModel::class
    override val bindingCallback: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDetailsPageBinding
        get() = FragmentDetailsPageBinding::inflate
    

    private val args: DetailsPageFragmentArgs by navArgs()

    override val bindViews: FragmentDetailsPageBinding.() -> Unit = {
        initViews()
    }

    //Initiates views
    private fun initViews() = with(binding) {
        ivIconBack.setOnClickListener {
            viewModel.navigate(NavigationCommand.Back)
        }

        args.recipe.let { recipe ->
            Glide.with(imageView)
                .load(recipe.imageUrl)
                .centerCrop()
                .placeholder(R.drawable.bg_image_place_holder)
                .into(imageView)

            tvTitle.text = recipe.title
            tvDescription.text = recipe.description

            if(recipe.chef?.isNotEmpty() == true){
                tvChefName.text = String.format(resources.getString(R.string.chef_name), recipe.chef)
                tvChefName.makeVisible()
            } else {
                tvChefName.makeGone()
            }

            recipe.tags.let { tags ->
                if (tags != null) {
                    for (tag in tags) chipGroup.addChip(tag)
                    chipGroup.makeVisible()
                } else {
                    chipGroup.makeGone()
                }
            }
        }
    }
}
