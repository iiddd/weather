package com.iiddd.weather.search.di

import com.iiddd.weather.core.utils.coroutines.DefaultDispatcherProvider
import com.iiddd.weather.core.utils.coroutines.DispatcherProvider
import com.iiddd.weather.search.data.SearchRepositoryImpl
import com.iiddd.weather.search.domain.SearchRepository
import com.iiddd.weather.search.presentation.viewmodel.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

object SearchModule {

    val module = module {
        single<DispatcherProvider> {
            DefaultDispatcherProvider()
        }

        single<SearchRepository> {
            SearchRepositoryImpl(
                geocodingService = get(),
                dispatcherProvider = get<DispatcherProvider>(),
            )
        }

        viewModel {
            SearchViewModel(
                searchRepository = get<SearchRepository>(),
                dispatcherProvider = get<DispatcherProvider>(),
            )
        }
    }
}