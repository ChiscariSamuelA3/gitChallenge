package com.example.gitchallenge.ui.home

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.*
import com.example.gitchallenge.models.Repo
import com.example.gitchallenge.services.ReposService

private const val PAGE_SIZE = 30

class HomeViewModel(context: Context) : ViewModel() {
    private val reposService: ReposService = ReposService.getInstance()

    private val isLoading = MutableLiveData<Boolean>()

    private val errorLiveData: MutableLiveData<String?> = MutableLiveData(null)
    private val repos: LiveData<PagingData<HomeItemViewModel>> =
        Pager(PagingConfig(pageSize = PAGE_SIZE)) {
            HomePagingSource(reposService, isLoading, errorLiveData)
        }.liveData.cachedIn(viewModelScope)

    fun getErrorLiveData() = errorLiveData
    fun getRepos() = repos
    fun getIsLoading() = isLoading
}

data class HomeItemViewModel(val repo: Repo) {
    val id = repo.id
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