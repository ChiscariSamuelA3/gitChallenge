package com.example.gitchallenge.util

import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions

fun NavController.safeNavigate(
    action: NavDirections,
    options: NavOptions? = null
) {
    currentDestination?.getAction(action.actionId)?.let {
        navigate(action, options)
    }
}