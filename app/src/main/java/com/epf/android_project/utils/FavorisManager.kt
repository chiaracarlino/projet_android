package com.epf.android_project.utils

import android.content.Context

object FavorisManager {
    private const val PREF_NAME = "favorites_prefs"
    private const val KEY_FAVORITES = "favorites_ids"

    fun getFavorites(context: Context): Set<Int> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet(KEY_FAVORITES, emptySet())
            ?.mapNotNull { it.toIntOrNull() }
            ?.toSet()
            ?: emptySet()
    }

    fun isFavorite(context: Context, id: Int): Boolean {
        return getFavorites(context).contains(id)
    }

    fun toggleFavorite(context: Context, id: Int) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val favorites = getFavorites(context).toMutableSet()
        if (favorites.contains(id)) {
            favorites.remove(id)
        } else {
            favorites.add(id)
        }
        val stringSet = favorites.map { it.toString() }.toSet()
        prefs.edit().putStringSet(KEY_FAVORITES, stringSet).apply()
    }
}
