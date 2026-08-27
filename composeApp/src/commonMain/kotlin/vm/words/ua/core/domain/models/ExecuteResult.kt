package vm.words.ua.core.domain.models

data class ExecuteResult(
    val isSuccess: Boolean,
    val errorMessage: String? = null
) {

    companion object {
        fun success(): ExecuteResult = ExecuteResult(isSuccess = true)

        fun failure(errorMessage: String): ExecuteResult =
            ExecuteResult(isSuccess = false, errorMessage = errorMessage)
    }
}
