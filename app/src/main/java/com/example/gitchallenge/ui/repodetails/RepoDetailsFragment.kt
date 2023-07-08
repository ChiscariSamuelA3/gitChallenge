package com.example.gitchallenge.ui.repodetails

import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.gitchallenge.R
import com.example.gitchallenge.databinding.FragmentRepoDetailsBinding
import com.squareup.picasso.Picasso
import io.noties.markwon.Markwon
import io.noties.markwon.image.ImagesPlugin

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
        viewModel.loadReadme(ownerName, repoName)

        binding.ivExpand.setOnClickListener {
            if (binding.cvReadmeContent.visibility == View.VISIBLE) {
                binding.cvReadmeContent.visibility = View.GONE
                binding.ivExpand.setImageResource(R.drawable.ic_more)
            } else {
                binding.cvReadmeContent.visibility = View.VISIBLE
                binding.ivExpand.setImageResource(R.drawable.ic_less)
            }
        }

        viewModel.getRepoDetails().observe(viewLifecycleOwner) { repo ->
            if (repo != null) {
                val imageUrl = repo.avatarUrl
                Picasso.get().load(imageUrl).into(binding.ivAvatar)
            }
        }

        viewModel.getReadmeDetails().observe(viewLifecycleOwner) { readme ->
            if (readme != null) {
                val decodedReadme = String(Base64.decode(readme.content, Base64.DEFAULT))

                val markdownContent = Markwon.builder(requireContext())
                    .usePlugin(ImagesPlugin.create())
                    .build()

                markdownContent.setMarkdown(binding.tvReadmeContent, decodedReadme)
            }
        }

        viewModel.getIsLoading().observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.llRepoDetails.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.llRepoDetails.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }
        }

        viewModel.getErrorLiveData().observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }
}