package com.example.moviebrowserapp.hilt

import android.content.Context
import androidx.room.Room
import com.example.moviebrowserapp.database.MovieEntity
import com.example.moviebrowserapp.database.MovieFirstListDao
import com.example.moviebrowserapp.database.MovieFirstPageDataBase
import com.example.moviebrowserapp.detailsscreen.domain.DetailsApiServices
import com.example.moviebrowserapp.detailsscreen.domain.DetailsRepo
import com.example.moviebrowserapp.detailsscreen.domain.DetailsUseCase
import com.example.moviebrowserapp.mainscreen.domain.movielistlogic.MovieListApiServices
import com.example.moviebrowserapp.mainscreen.domain.movielistlogic.MovieListRepository
import com.example.moviebrowserapp.mainscreen.domain.movielistlogic.MovieListUseCase
import com.example.moviebrowserapp.mainscreen.domain.searchquerylogic.SearchQueryApiServices
import com.example.moviebrowserapp.mainscreen.domain.searchquerylogic.SearchQueryRepo
import com.example.moviebrowserapp.mainscreen.domain.searchquerylogic.SearchQueryUseCase
import com.example.moviebrowserapp.network.CheckNetworkConnection
import com.example.moviebrowserapp.network.Network
import com.example.moviebrowserapp.network.Network.baseUrl
import com.example.moviebrowserapp.network.Network.provideLogIntercept
import com.example.moviebrowserapp.network.Network.token
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton

    fun provideDataBase(@ApplicationContext context: Context): MovieFirstPageDataBase {
        return Room.databaseBuilder(context,MovieFirstPageDataBase::class.java,"MovieDataBase").fallbackToDestructiveMigration().build()
   }

    @Provides
    @Singleton
    fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient())
            .build()
    }
    @Provides
    @Singleton
    fun okHttpClient(): OkHttpClient {

        return OkHttpClient.Builder().apply {
            addInterceptor{
                val request = it.request().newBuilder().addHeader("Authorization" , "Bearer $token" ).build()

                it.proceed(request)
            }
            addInterceptor(provideLogIntercept())
        }.build()

    }
    @Provides
    fun provideSearchQueryApiServices(retrofit: Retrofit): SearchQueryApiServices {
        return retrofit.create(SearchQueryApiServices::class.java)
    }

    @Provides
    fun provideMovieListApiServices(retrofit: Retrofit): MovieListApiServices {
        return retrofit.create(MovieListApiServices::class.java)
    }
    @Provides

    fun provideDetailsApiServices(retrofit: Retrofit): DetailsApiServices {
        return retrofit.create(DetailsApiServices::class.java)
    }

    @Provides
    fun provideSearchQueryRepo(api: SearchQueryApiServices): SearchQueryRepo {
        return SearchQueryRepo(api)
    }
    @Provides
    fun provideMovieListRepo(api: MovieListApiServices, dao: MovieFirstListDao): MovieListRepository {
        return MovieListRepository(dao,api)
    }
    @Provides

    fun provideDetailsRepo(api: DetailsApiServices): DetailsRepo {
        return DetailsRepo(api)
    }


    @Provides
    fun provideMovieListUseCase(repo: MovieListRepository): MovieListUseCase {
        return MovieListUseCase(repo)
    }

    @Provides
    fun provideDeteilsUseCase(repo: DetailsRepo):DetailsUseCase{
        return DetailsUseCase(repo)
    }

    @Provides
    fun provideSearchQueryUseCase(repo: SearchQueryRepo): SearchQueryUseCase {
        return SearchQueryUseCase(repo)
    }

    @Provides
    fun movieDao(movieDataBase: MovieFirstPageDataBase): MovieFirstListDao{
        return movieDataBase.getMovieFirstListDao()
    }

    @Provides
    fun provideCheckNetworkConnection(
        @ApplicationContext context: Context
    ): CheckNetworkConnection {
        return CheckNetworkConnection(context)
    }
}
