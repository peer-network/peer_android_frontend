package eu.peernetwork.wallet.domain.usecase

import eu.peernetwork.core.common.paging.Page
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.wallet.domain.model.Filter
import eu.peernetwork.wallet.domain.model.Sort
import eu.peernetwork.wallet.domain.model.Transaction
import eu.peernetwork.wallet.domain.repository.TransactionRepository
import javax.inject.Inject

class TransactionsUsecase @Inject constructor(
    private val repository: TransactionRepository
) : ParameterizedSuspendableUseCase<TransactionsUsecase.Parameter, Page<Transaction>>{
    override suspend fun invoke(param: Parameter): Page<Transaction> {
        return repository.getAll(param.filter, param.sort, param.page)
    }

    data class Parameter(
        val filter: Filter = Filter.None,
        val sort: Sort = Sort.NEWEST,
        val page: Pageable
    )
}
