package com.example.gitchallenge.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.gitchallenge.services.ReposService

class HomePagingSource(
    private val reposService: ReposService,
    private val isLoading: MutableLiveData<Boolean>,
    private val errorLiveData: MutableLiveData<String?>
) : PagingSource<Int, HomeItemViewModel>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HomeItemViewModel> {
        val page = params.key ?: 1

        return try {
            isLoading.postValue(true)
            errorLiveData.postValue(null)

            val repos = reposService.getRepos(page).map { repo ->
                HomeItemViewModel(repo)
            }

            LoadResult.Page(
                data = repos,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (repos.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            errorLiveData.postValue(e.message)
            LoadResult.Error(e)
        } finally {
            isLoading.postValue(false)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, HomeItemViewModel>): Int? {
        return state.anchorPosition
    }
}