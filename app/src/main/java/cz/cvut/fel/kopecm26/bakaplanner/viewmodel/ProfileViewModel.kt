package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

class ProfileViewModel: BaseViewModel() {

    suspend fun signOut() = userRepository.signOut()

}