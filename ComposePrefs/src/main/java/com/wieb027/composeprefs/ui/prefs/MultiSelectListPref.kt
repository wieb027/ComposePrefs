package com.wieb027.composeprefs.ui.prefs

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.AlertDialog
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.wieb027.composeprefs.ui.LocalPrefsDataStore
import kotlinx.coroutines.launch

/**
 * Preference that shows a list of entries in a Dialog where multiple entries can be selected at one time.
 *
 * @param key Key used to identify this Pref in the DataStore
 * @param title Main text which describes the Pref. Shown above the summary and in the Dialog.
 * @param modifier Modifier applied to the Text aspect of this Pref
 * @param infoText Used to give some more information about what this Pref is for
 * @param defaultValue Default selected key if this Pref hasn't been saved already. Otherwise the value from the dataStore is used.
 * @param onValuesChange Will be called with the [Set] of selected keys when an item is selected/unselected
 * @param dialogBackgroundColor Background color of the Dialog
 * @param textColor Text colour of the [title] and [infoText]
 * @param enabled If false, this Pref cannot be clicked and the Dialog cannot be shown.
 * @param entries Map of keys to values for entries that should be shown in the Dialog.
 * @param confirmButton Composable for the confirm button that receives a function to close the dialog
 */
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun MultiSelectListPref(
    key: String,
    title: String,
    modifier: Modifier = Modifier,
    infoText: String? = null,
    defaultValue: Set<String> = setOf(),
    onValuesChange: ((Set<String>) -> Unit)? = null,
    dialogBackgroundColor: Color = MaterialTheme.colors.surface,
    textColor: Color = MaterialTheme.colors.onBackground,
    enabled: Boolean = true,
    entries: Map<String, String> = mapOf(),
    confirmButton: @Composable ((onDismiss: () -> Unit) -> Unit)? = null,
) {
    val entryList = entries.toList()
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val selectionKey = stringSetPreferencesKey(key)
    val scope = rememberCoroutineScope()

    val datastore = LocalPrefsDataStore.current
    val prefs by remember { datastore.data }.collectAsState(initial = null)

    var selected = defaultValue
    prefs?.get(selectionKey)?.also { selected = it } // starting value if it exists in datastore

    fun edit(isSelected: Boolean, current: Pair<String, String>) = run {
        scope.launch {
            try {
                val result = when (!isSelected) {
                    true -> selected + current.first
                    false -> selected - current.first
                }
                datastore.edit { preferences ->
                    preferences[selectionKey] = result
                }
                onValuesChange?.invoke(result)
                selected = result
            } catch (e: Exception) {
                Log.e(
                    "MultiSelectListPref",
                    "Could not write pref $key to database. ${e.printStackTrace()}"
                )
            }
        }
    }

    val summary = selected.sortedBy { entry -> entryList.indexOfFirst { it.first == entry } }
        .joinToString(", ")

    TextPref(
        title = title,
        modifier = modifier,
        summary = summary.ifEmpty { infoText },
        textColor = textColor,
        enabled = true,
        onClick = { if (enabled) showDialog = !showDialog },
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            text = {
                Column {
                    Text(modifier = Modifier.padding(vertical = 8.dp), text = title, style = MaterialTheme.typography.h6)
                    Box(modifier = Modifier.heightIn(max = 360.dp)) {
                        LazyColumn {
                            items(entryList) { current ->
                                val isSelected = selected.contains(current.first)
                                val onSelectionChanged = {
                                    edit(isSelected, current)
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .selectable(
                                            selected = isSelected,
                                            onClick = { onSelectionChanged() }
                                        ),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Checkbox(
                                        checked = isSelected,
                                        onCheckedChange = { onSelectionChanged() },
                                        colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colors.primary)
                                    )
                                    Text(
                                        text = current.second,
                                        style = MaterialTheme.typography.body2,
                                        color = textColor
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                confirmButton?.invoke { showDialog = false } ?: run {
                    TextButton(
                        onClick = { showDialog = false },
                    ) {
                        Text(text = "Select", style = MaterialTheme.typography.button)
                    }
                }
            },
            backgroundColor = dialogBackgroundColor,
            properties = DialogProperties(
                usePlatformDefaultWidth = true
            )
        )
    }
}