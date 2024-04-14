package com.example.ghusers.model

enum class NavigationState(var value: String) {

    MAIN( "main"),
    DETAILS("details/{user}");

    companion object {
        fun details(user: String): NavigationState {
            return DETAILS.apply {
                value = "details/$user"
            }
        }
    }
}