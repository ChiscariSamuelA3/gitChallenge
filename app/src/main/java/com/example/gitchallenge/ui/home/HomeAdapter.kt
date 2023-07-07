package com.example.gitchallenge.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.gitchallenge.databinding.ItemRepoBinding
import com.example.gitchallenge.util.safeNavigate
import com.squareup.picasso.Picasso

class HomeAdapter() : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    private var repos: List<HomeItemViewModel> = ArrayList()
    private lateinit var binding: ItemRepoBinding

    fun setRepos(repos: List<HomeItemViewModel>) {
        this.repos = repos

        // refresh recycler view for new data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemRepoBinding.inflate(inflater, parent, false)
        return HomeViewHolder()
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bindRepo(repos[position])
    }

    override fun getItemCount(): Int = repos.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class HomeViewHolder() : RecyclerView.ViewHolder(binding.root) {
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
}