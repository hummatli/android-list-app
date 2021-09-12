package com.mobline.domain.di

import com.mobline.domain.exceptions.ErrorConverter
import com.mobline.domain.exceptions.ErrorConverterImpl
import com.mobline.domain.usecase.recipe.FetchRecipesUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module


const val IO_CONTEXT = "IO_CONTEXT"
const val ERROR_MAPPER_GENERAL = "ERROR_MAPPER_NETWORK"

val domainModule = module {
    single<ErrorConverter> {
        ErrorConverterImpl(
            setOf(
                get(named(ERROR_MAPPER_GENERAL))
            )
        )
    }

    factory {
        FetchRecipesUseCase(
            context = get(named(IO_CONTEXT)),
            converter = get(),
            repository = get()
        )
    }
}
