package com.most4devmyreciepes.di

import android.app.Application
import androidx.room.Room
import com.most4devmyreciepes.data.local.RecipeDatabase
import com.most4devmyreciepes.data.remote.RecipeApi
import com.most4devmyreciepes.data.impl.RecipeRepositoryImpl
import com.most4devmyreciepes.domain.repository.RecipeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideRecipeDatabase(app: Application): RecipeDatabase {
        return Room.databaseBuilder(
            app,
            RecipeDatabase::class.java,
            "recipe_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val newRequest = originalRequest
                    .newBuilder()
                    .header("x-rapidapi-key", "0dd58eb787msh90e37260a75db4bp13fb93jsn3ed6ea499f18")
                    .header("x-rapidapi-host", "tasty.p.rapidapi.com")
                    .url(originalRequest.url)
                    .build()
                chain.proceed(newRequest)
            }.build()
    }

    @Provides
    @Singleton
    fun provideRecipeApi(client: OkHttpClient): RecipeApi {
        return Retrofit.Builder()
            .baseUrl(RecipeApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(RecipeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRecipeRepository(
        api: RecipeApi,
        db: RecipeDatabase
    ): RecipeRepository {
        return RecipeRepositoryImpl(api, db.recipeDao)
    }
} 