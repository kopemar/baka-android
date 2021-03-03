package cz.cvut.fel.kopecm26.bakaplanner.networking.model.user

import cz.cvut.fel.kopecm26.bakaplanner.networking.model.User

enum class UserRole {
    AGREEMENT,
    ANONYMOUS,
    EMPLOYEE,
    MANAGER;

    companion object {
        fun getUserRole(user: User?) = when (user?.manager) {
            true -> MANAGER
            false -> if (user.agreement == true) AGREEMENT else EMPLOYEE
            null -> ANONYMOUS
        }
    }
}
