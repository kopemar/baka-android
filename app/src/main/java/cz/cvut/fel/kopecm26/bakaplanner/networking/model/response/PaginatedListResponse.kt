package cz.cvut.fel.kopecm26.bakaplanner.networking.model.response

abstract class PaginatedListResponse<T>(
    open val data: List<T>,
    open val hasNext: Boolean,
    open val totalPages: Int,
    open val currentPage: Int,
    open val records: Int = data.size
)
