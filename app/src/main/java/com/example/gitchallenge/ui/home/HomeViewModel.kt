package com.example.gitchallenge.ui.home

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gitchallenge.api.error.ApiException
import com.example.gitchallenge.models.Repo
import com.example.gitchallenge.services.ReposService
import kotlinx.coroutines.launch

class HomeViewModel(context: Context) : ViewModel() {
    private val reposService: ReposService = ReposService.getInstance(context)

    private val errorLiveData: MutableLiveData<String?> = MutableLiveData(null)
    private val repos: MutableLiveData<List<HomeItemViewModel>?> = MutableLiveData(null)

    fun getErrorLiveData() = errorLiveData
    fun getRepos() = repos

    fun loadRepos() = viewModelScope.launch {
        try {
            val res = reposService.getRepos().map { repo ->
                HomeItemViewModel(repo)
            }

            repos.postValue(res)
        } catch (e: ApiException) {
            errorLiveData.postValue(e.message)
        }
    }
}

data class HomeItemViewModel(val repo: Repo) {
    val fullName = repo.fullName
    val name = repo.name
    val description = repo.description
    val owner = repo.owner.login
    val stars = repo.stargazersCount
    val avatarUrl = repo.owner.avatarUrl
}

class HomeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}