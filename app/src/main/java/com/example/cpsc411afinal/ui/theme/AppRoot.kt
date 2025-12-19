package com.example.cpsc411afinal.ui.theme

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.cpsc411afinal.AppContainer
import com.example.cpsc411afinal.data.local.NoteEntity
import com.example.cpsc411afinal.data.repo.AuthRepository
import com.example.cpsc411afinal.data.repo.NotesRepository
import com.example.cpsc411afinal.viewmodel.*

private class AuthFactory(private val repo: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return AuthViewModel(repo) as T
    }
}

private class HomeFactory(
    private val authRepo: AuthRepository,
    private val prefsRepo: com.example.cpsc411afinal.data.repo.UserPrefsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return HomeViewModel(authRepo, prefsRepo) as T
    }
}

private class FolderFactory(private val repo: NotesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return FolderListViewModel(repo) as T
    }
}

private class NotesFactory(private val repo: NotesRepository, private val folderId: Long) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return NotesListViewModel(repo, folderId) as T
    }
}

private class NoteEditFactory(
    private val repo: NotesRepository,
    private val folderId: Long,
    private val noteId: Long?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return NoteEditViewModel(repo, folderId, noteId) as T
    }
}


private class ProfileFactory(
    private val prefsRepo: com.example.cpsc411afinal.data.repo.UserPrefsRepository,
    private val emailProvider: () -> String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ProfileViewModel(prefsRepo, emailProvider) as T
    }
}

@Composable
fun AppRoot(container: AppContainer) {
    val nav = rememberNavController()

    val authVm: AuthViewModel = viewModel(factory = AuthFactory(container.authRepository))
    val loggedIn by authVm.loggedIn.collectAsState()

    // Pick start based on persisted login
    val start = if (loggedIn) Routes.HOME else Routes.LOGIN

    // If auth state changes, kick user to correct graph entry
    LaunchedEffect(loggedIn) {
        if (loggedIn) {
            nav.navigate(Routes.HOME) {
                popUpTo(0) { inclusive = true }
            }
        } else {
            nav.navigate(Routes.LOGIN) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    NavHost(navController = nav, startDestination = start) {

        composable(Routes.LOGIN) {
            LoginScreen(
                vm = authVm,
                onGoSignup = { nav.navigate(Routes.SIGNUP) }
            )
        }

        composable(Routes.SIGNUP) {
            SignupScreen(
                vm = authVm,
                onGoLogin = { nav.popBackStack() }
            )
        }

        composable(Routes.HOME) {
            val homeVm: HomeViewModel = viewModel(
                factory = HomeFactory(container.authRepository, container.userPrefsRepository)
            )

            HomeScreen(
                vm = homeVm,
                onGoProfile = { nav.navigate(Routes.PROFILE) },
                onGoFolders = { nav.navigate(Routes.FOLDERS) },
                onSignOut = {
                    authVm.signOut()
                    // LaunchedEffect(loggedIn) will route to login
                }
            )
        }

        composable(Routes.PROFILE) {
            val profileVm: ProfileViewModel =
                viewModel(
                    factory = ProfileFactory(container.userPrefsRepository) {
                        com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.email ?: "Unknown"
                    }
                )

            ProfileScreen(
                vm = profileVm,
                onBack = { nav.popBackStack() },
                onSignOut = {
                    authVm.signOut()
                    // LaunchedEffect(loggedIn) will route to login
                }
            )
        }

        composable(Routes.FOLDERS) {
            val folderVm: FolderListViewModel = viewModel(factory = FolderFactory(container.notesRepository))

            FolderListScreen(
                vm = folderVm,
                onBack = { nav.popBackStack() },
                onOpenFolder = { fid ->
                    val f = folderVm.visibleFolders.value.firstOrNull { it.id == fid }
                    val name = f?.name ?: "Notes"
                    nav.navigate(Routes.notes(fid, name))
                }
            )
        }

        composable(
            route = Routes.NOTES,
            arguments = listOf(
                navArgument("folderId") { type = NavType.LongType },
                navArgument("folderName") { type = NavType.StringType }
            )
        ) { backStack ->
            val folderId = backStack.arguments?.getLong("folderId") ?: return@composable
            val folderName = backStack.arguments?.getString("folderName") ?: "Notes"

            val notesVm: NotesListViewModel = viewModel(factory = NotesFactory(container.notesRepository, folderId))

            NotesListScreen(
                vm = notesVm,
                folderName = folderName,
                onBack = { nav.popBackStack() },
                onAdd = { nav.navigate(Routes.editNote(folderId, folderName, null)) },
                onEdit = { note -> nav.navigate(Routes.editNote(folderId, folderName, note.id)) }
            )
        }

        composable(
            route = Routes.EDIT_NOTE,
            arguments = listOf(
                navArgument("folderId") { type = NavType.LongType },
                navArgument("folderName") { type = NavType.StringType },
                navArgument("noteId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) { backStack ->
            val folderId = backStack.arguments?.getLong("folderId") ?: return@composable
            val folderName = backStack.arguments?.getString("folderName") ?: "Notes"
            val noteId = backStack.arguments?.getLong("noteId") ?: -1L

            // Find the existing note (simple approach: read from current Notes VM state)
            val actualNoteId: Long? = if (noteId == -1L) null else noteId

            val editVm: NoteEditViewModel =
                viewModel(factory = NoteEditFactory(container.notesRepository, folderId, actualNoteId))

            NoteEditScreen(
                title = if (actualNoteId == null) "Add Note" else "Edit Note",
                vm = editVm,
                onBack = { nav.popBackStack() }
            )

        }
    }
}



