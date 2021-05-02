package cz.cvut.fel.kopecm26.bakaplanner.util

import androidx.paging.PagingSource
import androidx.paging.PagingState
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.response.PaginatedListResponse

class BasePagingSource<T: Any, P: PaginatedListResponse<T>>(
    private val loadResource: suspend (params: LoadParams<Int>) -> ResponseModel<P>,
    private val refreshKey: Int = 1
): PagingSource<Int, T>() {

    override fun getRefreshKey(state: PagingState<Int, T>) = refreshKey

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val response = loadResource.invoke(params)

        return if (response is ResponseModel.SUCCESS) {
            LoadResult.Page(
                data = response.data?.data ?: listOf(),
                prevKey = null,
                nextKey = if (response.data?.hasNext == true) response.data?.currentPage?.plus(1) ?: 2 else null,
            )
        } else {
            LoadResult.Error(
                RuntimeException()
            )
        }
    }
}