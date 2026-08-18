package ir.kitgroup.partnerManagement.feature.login.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.kitgroup.partnerManagement.feature.login.data.MockAuthRepository
import ir.kitgroup.partnerManagement.feature.login.domain.AuthRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: MockAuthRepository
    ): AuthRepository
}
