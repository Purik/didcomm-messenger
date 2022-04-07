package com.socialsirius.messenger.base.dagger;

import android.app.Application

import com.socialsirius.messenger.ui.activities.auth.AuthActivity
import com.socialsirius.messenger.ui.activities.loader.LoaderActivity
import com.socialsirius.messenger.ui.activities.main.MainActivity

import com.socialsirius.messenger.ui.activities.splash.SplashActivity

import com.socialsirius.messenger.ui.activities.tutorial.TutorialActivity
import com.socialsirius.messenger.ui.auth.AuthFragment
import com.socialsirius.messenger.ui.auth.createPhrase.CreatePhraseFirstFragment
import com.socialsirius.messenger.ui.auth.createPhrase.CreatePhraseSecondFragment
import com.socialsirius.messenger.ui.auth.createPhrase.CreatePhraseThirdFragment
import com.socialsirius.messenger.ui.chats.allChats.AllChatsFragment
import com.socialsirius.messenger.ui.chats.chat.ChatFragment
import com.socialsirius.messenger.ui.chats.userProfile.UserProfileFragment
import com.socialsirius.messenger.ui.inviteUser.InviteUserFragment
import com.socialsirius.messenger.ui.main.MainFragment
import com.socialsirius.messenger.ui.more.MenuMoreFragment
import com.socialsirius.messenger.ui.scan.HandleWebInviteFragment
import com.socialsirius.messenger.ui.scan.MenuScanQrFragment
import com.socialsirius.messenger.ui.tutorial.SplashFragment
import com.socialsirius.messenger.ui.userSettings.UserSettingsFragment
/*import com.socialsirius.messenger.ui.auth.auth_first.AuthFirstFragment
import com.socialsirius.messenger.ui.auth.auth_fourth.AuthFourthFragment
import com.socialsirius.messenger.ui.auth.auth_second.AuthSecondFragment
import com.socialsirius.messenger.ui.auth.auth_second_second.AuthSecondSecondFragment
import com.socialsirius.messenger.ui.auth.auth_third.AuthThirdFragment
import com.socialsirius.messenger.ui.auth.auth_third_identity.AuthThirdIChooseIdFragment
import com.socialsirius.messenger.ui.auth.auth_third_identity.AuthThirdIdentityFragment
import com.socialsirius.messenger.ui.auth.auth_third_third.AuthThirdThirdFragment
import com.socialsirius.messenger.ui.auth.auth_zero.AuthZeroFragment

import com.socialsirius.messenger.ui.main.MainDriverFragment*/



import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    ViewModelModule::class])
interface AppComponent {

    @dagger.Component.Builder
    interface Builder {
        @BindsInstance
        fun withApplication(application: Application?): Builder?
        fun build(): AppComponent?
    }

    fun inject(activity: MainActivity)
 //   fun provideMessageRepository(): MessageRepository?
    /**
     * Add all fragment with ViewModel Here
     */
    //Activities

    fun inject(activity: AuthActivity)
    fun inject(activity: TutorialActivity)
    fun inject(activity: SplashActivity)
    fun inject(activity: LoaderActivity)


    //Fragments
    fun inject(fragment: MainFragment)
    fun inject(fragment: SplashFragment)
    fun inject(fragment: AuthFragment)
    fun inject(fragment: CreatePhraseFirstFragment)
    fun inject(fragment: CreatePhraseSecondFragment)
    fun inject(fragment: CreatePhraseThirdFragment)

    fun inject(fragment: AllChatsFragment)
    fun inject(fragment: ChatFragment)
    fun inject(fragment: MenuMoreFragment)
    fun inject(fragment: UserProfileFragment)
    fun inject(fragment: UserSettingsFragment)
    fun inject(fragment: MenuScanQrFragment)
    fun inject(fragment: InviteUserFragment)
    fun inject(fragment: HandleWebInviteFragment)


/*    fun inject(fragment: AuthZeroFragment)
    fun inject(fragment: AuthFirstFragment)
    fun inject(fragment: AuthSecondFragment)
    fun inject(fragment: AuthSecondSecondFragment)
    fun inject(fragment: AuthThirdFragment)
    fun inject(fragment: AuthThirdThirdFragment)
    fun inject(fragment: AuthThirdIdentityFragment)
    fun inject(fragment: AuthThirdIChooseIdFragment)
    fun inject(fragment: AuthFourthFragment)


   */



}