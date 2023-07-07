package com.example.gitchallenge.ui.home

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.gitchallenge.services.ReposService

class ReposPagingSource(
    private val reposService: ReposService
) : PagingSource<Int, HomeItemViewModel>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HomeItemViewModel> {
        val page = params.key ?: 1

        Log.d("ReposPagingSource", "load: page = $page")

        return try {
            val repos = reposService.getRepos(page).map { repo ->
                HomeItemViewModel(repo)
            }

            LoadResult.Page(
                data = repos,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (repos.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, HomeItemViewModel>): Int? {
        return state.anchorPosition
    }
}