package eu.peernetwork.user.ui.usecase

import eu.peernetwork.core.common.usecase.ParameterizedBlockingUseCase
import javax.inject.Inject

class EmailMaskUsecase @Inject constructor() : ParameterizedBlockingUseCase<String, String> {
    override fun invoke(param: String): String {
        val atIndex = param.indexOf('@')
        if (atIndex <= 0) return param
        val local = param.substring(0, atIndex)
        val domain = param.substring(atIndex)
        val indices = local.mapIndexedNotNull { idx, ch ->
            if (ch.isLetterOrDigit()) idx else null
        }
        if (indices.isEmpty()) return "*$domain"
        val revealLast = indices.last()
        val maskedLocal = buildString {
            for (i in local.indices) {
                val ch = local[i]
                if (!ch.isLetterOrDigit()) {
                    append(ch)
                } else if (i == revealLast) {
                    append(ch)
                } else {
                    append('*')
                }
            }
        }
        return maskedLocal + domain
    }
}
