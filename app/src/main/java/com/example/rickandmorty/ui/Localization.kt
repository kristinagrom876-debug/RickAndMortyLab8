package com.example.rickandmorty.ui

import android.content.res.Resources
import com.example.rickandmorty.R
import java.util.Locale

/** Значения из API переводятся на русский; имена персонажей и коды серий не переводятся. */
internal fun statusLabelRes(value: String): Int = when (value.trim().lowercase(Locale.ROOT)) {
    "alive" -> R.string.status_alive
    "dead" -> R.string.status_dead
    else -> R.string.status_unknown
}

internal fun speciesLabelRes(value: String): Int = when (value.trim().lowercase(Locale.ROOT)) {
    "human" -> R.string.species_human
    "alien" -> R.string.species_alien
    "robot" -> R.string.species_robot
    "animal" -> R.string.species_animal
    "humanoid" -> R.string.species_humanoid
    "mythological creature" -> R.string.species_mythological
    "cronenberg" -> R.string.species_cronenberg
    "disease" -> R.string.species_disease
    "poopybutthole" -> R.string.species_poopybutthole
    else -> R.string.species_unknown
}

internal fun genderLabelRes(value: String): Int = when (value.trim().lowercase(Locale.ROOT)) {
    "female" -> R.string.gender_female
    "male" -> R.string.gender_male
    "genderless" -> R.string.gender_genderless
    else -> R.string.gender_unknown
}

internal fun typeLabelRes(value: String): Int =
    if (value.isBlank()) R.string.type_unspecified else speciesLabelRes(value)

/** Дата из API бывает в виде "December 2, 2013" или "2013-12-02". */
internal fun localizedAirDate(raw: String, resources: Resources): String {
    val hyphenated = raw.contains('-')
    val parts = if (hyphenated) raw.split("-") else raw.split(Regex("\\s+"))
    val yearIndex = if (hyphenated) 0 else 2
    val monthIndex = if (hyphenated) 1 else 0
    val dayIndex = if (hyphenated) 2 else 1

    if (parts.size <= maxOf(yearIndex, monthIndex, dayIndex)) {
        return resources.getString(R.string.date_unspecified)
    }

    val day = parts[dayIndex].trimEnd(',').toIntOrNull()
        ?: return resources.getString(R.string.date_unspecified)
    val year = parts[yearIndex].trimEnd(',')
    val monthRes = monthLabelRes(parts[monthIndex].lowercase(Locale.ROOT))
        ?: return resources.getString(R.string.date_unspecified)

    return resources.getString(R.string.air_date_format, day, resources.getString(monthRes), year)
}

private fun monthLabelRes(value: String): Int? = when (value) {
    "january", "jan", "01", "1" -> R.string.month_01
    "february", "feb", "02", "2" -> R.string.month_02
    "march", "mar", "03", "3" -> R.string.month_03
    "april", "apr", "04", "4" -> R.string.month_04
    "may", "05", "5" -> R.string.month_05
    "june", "jun", "06", "6" -> R.string.month_06
    "july", "jul", "07", "7" -> R.string.month_07
    "august", "aug", "08", "8" -> R.string.month_08
    "september", "sep", "09", "9" -> R.string.month_09
    "october", "oct", "10" -> R.string.month_10
    "november", "nov", "11" -> R.string.month_11
    "december", "dec", "12" -> R.string.month_12
    else -> null
}
