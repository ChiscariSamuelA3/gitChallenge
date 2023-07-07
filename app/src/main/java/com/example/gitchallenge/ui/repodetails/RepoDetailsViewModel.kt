package com.example.gitchallenge.ui.repodetails

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gitchallenge.models.Repo
import com.example.gitchallenge.services.ReposService
import kotlinx.coroutines.launch

class RepoDetailsViewModel(context: Context) : ViewModel() {
    private val reposService: ReposService = ReposService.getInstance()

    private val errorLiveData: MutableLiveData<String?> = MutableLiveData(null)
    private val repoDetails: MutableLiveData<RepoDetailsItemViewModel> = MutableLiveData(null)

    fun getErrorLiveData() = errorLiveData
    fun getRepoDetails() = repoDetails

    fun loadRepoDetails(owner: String, repoName: String) = viewModelScope.launch {
        try {
            val repo = reposService.getRepoDetails(owner, repoName)
            repoDetails.postValue(RepoDetailsItemViewModel(repo))
        } catch (e: Exception) {
            errorLiveData.postValue(e.message)
        }
    }
}

data class RepoDetailsItemViewModel(val repo: Repo) {
    val id = repo.id
    val fullName = repo.fullName
    val username = repo.owner.login
    val description = repo.description
    val avatarUrl = repo.owner.avatarUrl
    val forks = repo.forks.toString()
    val watchers = repo.watchers.toString()
    val link = repo.htmlUrl
}

class RepoDetailsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RepoDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RepoDetailsViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}