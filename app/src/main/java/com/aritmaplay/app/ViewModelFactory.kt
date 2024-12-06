package com.aritmaplay.app

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aritmaplay.app.repository.UserRepository
import com.aritmaplay.app.di.Injection
import com.aritmaplay.app.repository.LeaderboardRepository
import com.aritmaplay.app.repository.ProfileRepository
import com.aritmaplay.app.repository.QuizRepository
import com.aritmaplay.app.ui.login.LoginViewModel
import com.aritmaplay.app.ui.profile.ProfileViewModel
import com.aritmaplay.app.ui.quiz.QuizViewModel
import com.aritmaplay.app.ui.rank.RankViewModel
import com.aritmaplay.app.ui.signup.SignUpViewModel

class ViewModelFactory(
    private val userRepository: UserRepository,
    private val quizRepository: QuizRepository,
    private val profileRepository: ProfileRepository,
    private val leaderboardRepository: LeaderboardRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userRepository, profileRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(QuizViewModel::class.java) -> {
                QuizViewModel(quizRepository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(profileRepository) as T
            }
            modelClass.isAssignableFrom(RankViewModel::class.java) -> {
                RankViewModel(leaderboardRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(
                        Injection.provideUserRepository(context),
                        Injection.provideQuizRepository(),
                        Injection.provideProfileRepository(context),
                        Injection.provideLeaderboardRepository(context)
                    )
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

}