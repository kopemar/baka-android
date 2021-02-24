package cz.cvut.fel.kopecm26.bakaplanner.util.ext

import com.squareup.moshi.Moshi

/**
 * Make JSON from any valid class
 */
inline fun <reified T> T.toJson(): String = Moshi.Builder().build().adapter(T::class.java).toJson(this)

inline fun <reified T> String.fromJson(): T? = Moshi.Builder().build().adapter(T::class.java).fromJson(this)
