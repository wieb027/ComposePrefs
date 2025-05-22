package com.wieb027.composeprefs.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

lateinit var LocalPrefsDataStore: ProvidableCompositionLocal<DataStore<Preferences>>

/**
 * Main preference screen which holds [PrefsListItem]s
 *
 * @param dataStore DataStore which will be used to save all the preferences
 * @param modifier Modifier applied to the [LazyColumn] holding the list of Prefs
 *
 */
@Composable
fun PrefsScreen(
    dataStore: DataStore<Preferences>,
    modifier: Modifier = Modifier,
    dividerThickness: Dp = 1.dp, // 0 for no divider
    dividerIndent: Dp = 0.dp, // indents on both sides
    content: PrefsScope.() -> Unit
) {
    LocalPrefsDataStore = staticCompositionLocalOf { dataStore }
    val prefsScope = PrefsScopeImpl().apply(content)

    // Now the dataStore can be accessed by calling LocalPrefsDataStore.current from any child Pref
    CompositionLocalProvider(LocalPrefsDataStore provides dataStore) {
        Column {
            LazyColumn(modifier = modifier.fillMaxSize()) {

                items(prefsScope.prefsItems.size) { index ->

                    val paintTopDivider = dividerThickness != 0.dp
                            && !prefsScope.headerIndexes.contains(index)

                    val paintBottomDivider = dividerThickness != 0.dp
                            && prefsScope.headerIndexes.contains(index + 1)
                            && !prefsScope.headerIndexes.contains(index)

                    if (paintTopDivider) {
                        Divider(
                            thickness = dividerThickness,
                            indent = dividerIndent
                        )
                    }

                    prefsScope.getPrefsItem(index)()

                    if (paintBottomDivider) {
                        Divider(
                            thickness = dividerThickness,
                            indent = dividerIndent
                        )
                    }
                }
            }
        }
    }
}
