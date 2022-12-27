package com.socialsirius.messenger.base.dagger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.socialsirius.messenger.ui.activities.auth.AuthActivityModel
import com.socialsirius.messenger.ui.activities.base.SimpleActivityModel
import com.socialsirius.messenger.ui.activities.credentials.CredentialsActivityModel
import com.socialsirius.messenger.ui.activities.groupCreate.GroupCreateActivityModel
import com.socialsirius.messenger.ui.activities.invitations.InvitationsActivityModel
import com.socialsirius.messenger.ui.activities.invite.HandleWebInviteActivity
import com.socialsirius.messenger.ui.activities.invite.HandleWebInviteActivityModel
import com.socialsirius.messenger.ui.activities.invite.InviteActivityModel
import com.socialsirius.messenger.ui.activities.loader.LoaderActivityModel
import com.socialsirius.messenger.ui.activities.main.MainActivityModel
import com.socialsirius.messenger.ui.activities.message.MessageActivityModel
import com.socialsirius.messenger.ui.activities.scan.ScanActivityModel
import com.socialsirius.messenger.ui.activities.settings.SettingsActivityModel

import com.socialsirius.messenger.ui.activities.splash.SplashActivityModel

import com.socialsirius.messenger.ui.activities.tutorial.TutorialActivityModel
import com.socialsirius.messenger.ui.auth.AuthSecurityViewModel
import com.socialsirius.messenger.ui.auth.AuthViewModel
import com.socialsirius.messenger.ui.auth.createPhrase.CreatePhraseFirstViewModel
import com.socialsirius.messenger.ui.auth.createPhrase.CreatePhraseSecondViewModel
import com.socialsirius.messenger.ui.auth.createPhrase.CreatePhraseThirdViewModel
import com.socialsirius.messenger.ui.chats.allChats.AllChatsViewModel
import com.socialsirius.messenger.ui.chats.chat.ChatViewModel
import com.socialsirius.messenger.ui.chats.groupChatCreate.GroupChatCreateFragment
import com.socialsirius.messenger.ui.chats.groupChatCreate.GroupChatCreateViewModel
import com.socialsirius.messenger.ui.chats.invitations.InvitationsListViewModel
import com.socialsirius.messenger.ui.chats.userProfile.UserProfileViewModel
import com.socialsirius.messenger.ui.connections.connectionCard.ConnectionCardViewModel
import com.socialsirius.messenger.ui.connections.connectionCard.ConnectionRequestDetailViewModel
import com.socialsirius.messenger.ui.credentials.CredentialsViewModel
import com.socialsirius.messenger.ui.inviteUser.HandleWebInviteViewModel
import com.socialsirius.messenger.ui.inviteUser.InviteUserViewModel
import com.socialsirius.messenger.ui.main.MainViewModel
import com.socialsirius.messenger.ui.more.MenuMoreViewModel
import com.socialsirius.messenger.ui.pinCreate.CreatePinViewModel
import com.socialsirius.messenger.ui.pinEnter.EnterPinViewModel

import com.socialsirius.messenger.ui.scan.MenuScanQrViewModel
import com.socialsirius.messenger.ui.tutorial.SplashViewModel
import com.socialsirius.messenger.ui.userSettings.UserSettingsViewModel
/*import com.socialsirius.messenger.ui.auth.auth_first.AuthFirstViewModel
import com.socialsirius.messenger.ui.auth.auth_fourth.AuthFourthViewModel
import com.socialsirius.messenger.ui.auth.auth_second.AuthSecondViewModel
import com.socialsirius.messenger.ui.auth.auth_second_second.AuthSecondSecondViewModel
import com.socialsirius.messenger.ui.auth.auth_third.AuthThirdViewModel
import com.socialsirius.messenger.ui.auth.auth_third_identity.AuthThirdChooseIdViewModel
import com.socialsirius.messenger.ui.auth.auth_third_identity.AuthThirdIdentityViewModel
import com.socialsirius.messenger.ui.auth.auth_third_third.AuthThirdThirdViewModel
import com.socialsirius.messenger.ui.auth.auth_zero.AuthZeroViewModel

import com.socialsirius.messenger.ui.main.MainDriverViewModel*/



import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass

@Singleton
class ViewModelFactory @Inject constructor(private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        viewModels[modelClass]?.get() as T
}

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    /**
     * Add all viewModel Here
     */


    /**
     * Activity viewModel Here
     */

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityModel::class)
    internal abstract fun bindMainActivityModel(viewModel: MainActivityModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MessageActivityModel::class)
    internal abstract fun bindMessageActivityModel(viewModel: MessageActivityModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SimpleActivityModel::class)
    internal abstract fun bindSimpleActivityModel(viewModel: SimpleActivityModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InvitationsActivityModel::class)
    internal abstract fun bindInvitationsActivityModel(viewModel: InvitationsActivityModel): ViewModel






     @Binds
     @IntoMap
     @ViewModelKey(SplashActivityModel::class)
     internal abstract fun bindSplashActivityModel(viewModel: SplashActivityModel): ViewModel


     @Binds
     @IntoMap
     @ViewModelKey(LoaderActivityModel::class)
     internal abstract fun bindLoaderActivityModel(viewModel: LoaderActivityModel): ViewModel



     @Binds
     @IntoMap
     @ViewModelKey(TutorialActivityModel::class)
     internal abstract fun bindTutorialActivityModel(viewModel: TutorialActivityModel): ViewModel

     @Binds
     @IntoMap
     @ViewModelKey(AuthActivityModel::class)
     internal abstract fun bindAuthActivityModel(viewModel: AuthActivityModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(InviteActivityModel::class)
    internal abstract fun bindInviteActivityModel(viewModel: InviteActivityModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(HandleWebInviteActivityModel::class)
    internal abstract fun bindHandleWebInviteActivityModel(viewModel: HandleWebInviteActivityModel): ViewModel



    @Binds
    @IntoMap
    @ViewModelKey(SettingsActivityModel::class)
    internal abstract fun bindSettingsActivityModel(viewModel: SettingsActivityModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ScanActivityModel::class)
    internal abstract fun bindScanActivityModel(viewModel: ScanActivityModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GroupCreateActivityModel::class)
    internal abstract fun bindGroupCreateActivityModel(viewModel: GroupCreateActivityModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CredentialsActivityModel::class)
    internal abstract fun bindCredentialsActivityModel(viewModel: CredentialsActivityModel): ViewModel


    /**
     * Fragments viewModel Here
     */


    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    internal abstract fun bindSplashViewModel(viewModel: SplashViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    internal abstract fun bindAuthViewModel(viewModel: AuthViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AuthSecurityViewModel::class)
    internal abstract fun bindAuthSecurityViewModel(viewModel: AuthSecurityViewModel): ViewModel




    @Binds
    @IntoMap
    @ViewModelKey(CreatePhraseFirstViewModel::class)
    internal abstract fun bindCreatePhraseViewModel(viewModel: CreatePhraseFirstViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreatePhraseSecondViewModel::class)
    internal abstract fun bindCreatePhraseSecondViewModel(viewModel: CreatePhraseSecondViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreatePhraseThirdViewModel::class)
    internal abstract fun bindCreatePhraseThirdViewModel(viewModel: CreatePhraseThirdViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AllChatsViewModel::class)
    internal abstract fun bindAllChatsViewModel(viewModel: AllChatsViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(MenuMoreViewModel::class)
    internal abstract fun bindMenuMoreViewModel(viewModel: MenuMoreViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserProfileViewModel::class)
    internal abstract fun bindUserProfileViewModel(viewModel: UserProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChatViewModel::class)
    internal abstract fun bindChatViewModel(viewModel: ChatViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(UserSettingsViewModel::class)
    internal abstract fun bindUserSettingsViewModel(viewModel: UserSettingsViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(MenuScanQrViewModel::class)
    internal abstract fun bindMenuScanQrViewModel(viewModel: MenuScanQrViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InviteUserViewModel::class)
    internal abstract fun bindInviteUserViewModel(viewModel: InviteUserViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HandleWebInviteViewModel::class)
    internal abstract fun bindHandleWebInviteViewModel(viewModel: HandleWebInviteViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreatePinViewModel::class)
    internal abstract fun bindCreatePinViewModel(viewModel: CreatePinViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EnterPinViewModel::class)
    internal abstract fun bindEnterPinViewModel(viewModel: EnterPinViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(GroupChatCreateViewModel::class)
    internal abstract fun bindGroupChatCreateViewModel(viewModel: GroupChatCreateViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ConnectionCardViewModel::class)
    internal abstract fun bindConnectionCardViewModel(viewModel: ConnectionCardViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(ConnectionRequestDetailViewModel::class)
    internal abstract fun bindConnectionRequestDetailViewModel(viewModel: ConnectionRequestDetailViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(CredentialsViewModel::class)
    internal abstract fun bindCredentialsViewModel(viewModel: CredentialsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InvitationsListViewModel::class)
    internal abstract fun bindInvitationsListViewModel(viewModel: InvitationsListViewModel): ViewModel





    /*


       @Binds
       @IntoMap
       @ViewModelKey(AuthFirstViewModel::class)
       internal abstract fun bindAuthFirstViewModel(viewModel: AuthFirstViewModel): ViewModel

       @Binds
       @IntoMap
       @ViewModelKey(AuthSecondViewModel::class)
       internal abstract fun bindAuthSecondViewModel(viewModel: AuthSecondViewModel): ViewModel

       @Binds
       @IntoMap
       @ViewModelKey(AuthSecondSecondViewModel::class)
       internal abstract fun bindAuthSecondSecondViewModel(viewModel: AuthSecondSecondViewModel): ViewModel



       @Binds
       @IntoMap
       @ViewModelKey(AuthThirdViewModel::class)
       internal abstract fun bindAuthThirdViewModel(viewModel: AuthThirdViewModel): ViewModel

       @Binds
       @IntoMap
       @ViewModelKey(AuthThirdThirdViewModel::class)
       internal abstract fun bindAuthThirdThirdViewModel(viewModel: AuthThirdThirdViewModel): ViewModel




       @Binds
       @IntoMap
       @ViewModelKey(AuthThirdIdentityViewModel::class)
       internal abstract fun bindAuthThirdIdentityViewModel(viewModel: AuthThirdIdentityViewModel): ViewModel

       @Binds
       @IntoMap
       @ViewModelKey(AuthThirdChooseIdViewModel::class)
       internal abstract fun bindAuthThirdChooseIdViewModel(viewModel: AuthThirdChooseIdViewModel): ViewModel



       @Binds
       @IntoMap
       @ViewModelKey(AuthFourthViewModel::class)
       internal abstract fun bindAuthFourthViewModel(viewModel: AuthFourthViewModel): ViewModel



   */



}
