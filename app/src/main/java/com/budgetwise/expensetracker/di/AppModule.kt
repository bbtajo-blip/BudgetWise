package com.budgetwise.expensetracker.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.budgetwise.expensetracker.data.db.ExpenseDao
import com.budgetwise.expensetracker.data.db.ExpenseDatabase
import com.budgetwise.expensetracker.data.repository.BudgetRepositoryImpl
import com.budgetwise.expensetracker.data.repository.ExpenseRepositoryImpl
import com.budgetwise.expensetracker.domain.repository.BudgetRepository
import com.budgetwise.expensetracker.domain.repository.ExpenseRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindExpenseRepository(repository: ExpenseRepositoryImpl): ExpenseRepository

    @Binds
    @Singleton
    abstract fun bindBudgetRepository(repository: BudgetRepositoryImpl): BudgetRepository
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ExpenseDatabase =
        Room.databaseBuilder(context, ExpenseDatabase::class.java, "budgetwise.db").build()

    @Provides
    fun provideExpenseDao(database: ExpenseDatabase): ExpenseDao = database.expenseDao()

    @Provides
    @Singleton
    fun providePreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("budgetwise_preferences", Context.MODE_PRIVATE)
}
