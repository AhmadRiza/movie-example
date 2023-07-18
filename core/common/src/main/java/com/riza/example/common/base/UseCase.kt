package com.riza.example.common.base

import com.riza.example.common.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class NonSuspendingUseCase<ResultType : Any, in Params> {

    protected abstract fun build(params: Params?): ResultType

    open operator fun invoke(params: Params? = null): ResultType {
        return build(params)
    }
}

abstract class BaseUseCase<ResultType : Any, in Params> {

    protected abstract suspend fun build(params: Params?): ResultType

    open suspend operator fun invoke(params: Params? = null): ResultType {
        return build(params)
    }
}

abstract class UseCase<SuccessType : Any, in Params> : BaseUseCase<Result<SuccessType>, Params>() {

    suspend fun execute(params: Params? = null): Result<SuccessType> {
        return try {
            build(params)
        } catch (error: Throwable) {
            Result.Error.unknown()
        }
    }

    override suspend operator fun invoke(params: Params?): Result<SuccessType> {
        return execute(params)
    }
}

abstract class UseCaseWithContext<SuccessType : Any, in Params> :
    BaseUseCase<Result<SuccessType>, Params>() {

    suspend fun execute(params: Params? = null): Result<SuccessType> {
        return try {
            withContext(Dispatchers.IO) {
                build(params)
            }
        } catch (error: Throwable) {
            Result.Error.unknown()
        }
    }

    override suspend operator fun invoke(params: Params?): Result<SuccessType> {
        return execute(params)
    }
}
