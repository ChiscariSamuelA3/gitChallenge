package com.example.gitchallenge.ui.repodetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.gitchallenge.databinding.FragmentRepoDetailsBinding
import com.squareup.picasso.Picasso

class RepoDetailsFragment : Fragment() {
    private lateinit var binding: FragmentRepoDetailsBinding

    private val viewModel: RepoDetailsViewModel by activityViewModels {
        RepoDetailsViewModelFactory(requireContext())
    }

    private val args: RepoDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRepoDetailsBinding.inflate(layoutInflater)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ownerName = args.owner
        val repoName = args.repo

        viewModel.loadRepoDetails(ownerName, repoName)

        viewModel.getRepoDetails().observe(viewLifecycleOwner) { repo ->
            if (repo != null) {
                val imageUrl = repo.avatarUrl
                Picasso.get().load(imageUrl).into(binding.ivAvatar)
            }
        }

        viewModel.getErrorLiveData().observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }
}