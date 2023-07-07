package com.example.gitchallenge.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gitchallenge.databinding.ItemRepoBinding
import com.example.gitchallenge.util.safeNavigate
import com.squareup.picasso.Picasso

class HomeAdapter : PagingDataAdapter<HomeItemViewModel, HomeAdapter.HomeViewHolder>(REPO_CALLBACK) {
    private lateinit var binding: ItemRepoBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemRepoBinding.inflate(inflater, parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val repoItem = getItem(position)
        if (repoItem != null) {
            holder.bindRepo(repoItem)
        }
    }

    inner class HomeViewHolder(private val binding: ItemRepoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindRepo(repo: HomeItemViewModel) {
            binding.apply {
                tvFullName.text = repo.fullName
                tvDescription.text = repo.description
                tvStars.text = repo.stars.toString()
                Picasso.get().load(repo.avatarUrl).into(ivAvatar)

                btnViewDetails.setOnClickListener {
                    val action =
                        HomeFragmentDirections.actionHomeToRepoDetails(repo.owner, repo.name)
                    it.findNavController().safeNavigate(action)
                }
            }
        }
    }

    companion object {
        // it calculates the changes in the list and updates only necessary items
        private val REPO_CALLBACK = object : DiffUtil.ItemCallback<HomeItemViewModel>() {
            override fun areItemsTheSame(
                oldItem: HomeItemViewModel,
                newItem: HomeItemViewModel
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: HomeItemViewModel,
                newItem: HomeItemViewModel
            ): Boolean =
                oldItem == newItem
        }
    }
}