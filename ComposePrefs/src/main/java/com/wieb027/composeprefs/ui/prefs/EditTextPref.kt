package com.wieb027.composeprefs.ui.prefs

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.DialogProperties
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.wieb027.composeprefs.ui.LocalPrefsDataStore
import com.wieb027.composeprefs.ui.ifNotNullThen
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Preference which shows a TextField in a Dialog
 *
 * @param key Key used to identify this Pref in the DataStore
 * @param title Main text which describes the Pref
 * @param modifier Modifier applied to the Text aspect of this Pref
 * @param summary Used to give some more information about what this Pref is for (in this pref not used)
 * @param dialogTitle Title shown in the dialog. No title if null.
 * @param dialogMessage Summary shown underneath [dialogTitle]. No summary if null.
 * @param defaultValue Default value that will be set in the TextField when the dialog is shown for the first time.
 * @param onValueSaved Will be called with new TextField value when the confirm button is clicked. It is NOT called every time the value changes. Use [onValueChange] for that.
 * @param onValueChange Will be called every time the TextField value is changed.
 * @param dialogBackgroundColor Color of the dropdown menu
 * @param textColor Text colour of the [title] and [summary]
 * @param enabled If false, this Pref cannot be clicked.
 */
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun EditTextPref(
    key: String,
    title: String,
    modifier: Modifier = Modifier,
    summary: String? = null,
    dialogTitle: String? = null,
    dialogMessage: String? = null,
    defaultValue: String = "",
    onValueSaved: ((String) -> Unit) = {},
    onValueChange: ((String) -> Unit) = {},
    dialogBackgroundColor: Color = MaterialTheme.colors.background,
    textColor: Color = MaterialTheme.colors.onBackground,
    enabled: Boolean = true,
) {

    var showDialog by rememberSaveable { mutableStateOf(false) }
    val selectionKey = stringPreferencesKey(key)
    val scope = rememberCoroutineScope()

    val datastore = LocalPrefsDataStore.current
    val prefs by remember { datastore.data }.collectAsState(initial = null)

    //value should only change when save button is clicked
    var value by remember { mutableStateOf(defaultValue) }
    //value of the TextField which changes every time the text is modified
    var textVal by remember { mutableStateOf(value) }

    var dialogSize by remember { mutableStateOf(Size.Zero) }

    // Set value initially if it exists in datastore
    LaunchedEffect(Unit) {
        prefs?.get(selectionKey)?.let {
            value = it
        }
    }

    LaunchedEffect(datastore.data) {
        datastore.data.collectLatest { pref ->
            pref[selectionKey]?.let {
                value = it
            }
        }
    }

    fun edit() = run {
        scope.launch {
            try {
                datastore.edit { preferences ->
                    preferences[selectionKey] = textVal
                }
                onValueSaved(textVal)
            } catch (e: Exception) {
                Log.e(
                    "EditTextPref",
                    "Could not write pref $key to database. ${e.printStackTrace()}"
                )
            }
        }
    }

    TextPref(
        title = title,
        modifier = modifier,
        summary = value, // summary is not used in this Pref
        textColor = textColor,
        enabled = enabled,
        onClick = { if (enabled) showDialog = !showDialog },
    )

    if (showDialog) {
        //reset
        LaunchedEffect(null) {
            textVal = value
        }
        AlertDialog(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .onGloballyPositioned {
                    dialogSize = it.size.toSize()
                },
            onDismissRequest = { showDialog = false },
            title = null,
            text = null,
            buttons = {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    DialogHeader(dialogTitle, dialogMessage)

                    OutlinedTextField(
                        value = textVal,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .weight(1f, fill = false),
                        onValueChange = {
                            textVal = it
                            onValueChange(it)
                        }
                    )

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        DialogIconButton(
                            modifier = Modifier.padding(start = 16.dp, bottom = 16.dp, end = 16.dp),
                            onClick = { showDialog = false },
                            icon = Icons.Rounded.Close,
                            description = "Close",
                        )

                        DialogIconButton(
                            modifier = Modifier.padding(start = 16.dp, bottom = 16.dp, end = 16.dp),
                            onClick = {
                                edit()
                                showDialog = false
                            },
                            icon = Icons.Rounded.Check,
                            description = "Save",
                        )
                    }

                }

            },
            properties = DialogProperties(usePlatformDefaultWidth = false),
            backgroundColor = dialogBackgroundColor,
        )
    }
}

@Composable
fun DialogHeader(dialogTitle: String?, dialogMessage: String?) {

    Column(modifier = Modifier.padding(16.dp)) {
        dialogTitle.ifNotNullThen {
            Text(
                text = dialogTitle!!,
                style = MaterialTheme.typography.h6
            )
        }?.invoke()

        dialogMessage.ifNotNullThen {
            Text(
                text = dialogMessage!!,
                style = MaterialTheme.typography.subtitle1
            )
        }?.invoke()
    }
}

@Composable
fun DialogIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    description: String?,
    modifier: Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors =
        ButtonDefaults.buttonColors(
            disabledBackgroundColor = Color(186, 186, 186),
            backgroundColor = Color(0xFF002749),
            contentColor = Color.White, // Text and icon color
        ),
        contentPadding = PaddingValues(vertical = 15.dp),
    ) {
        Icon(icon, description)
    }
}