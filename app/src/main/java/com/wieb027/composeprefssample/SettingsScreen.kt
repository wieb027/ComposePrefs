package com.wieb027.composeprefssample

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wieb027.composeprefs.ui.GroupHeader
import com.wieb027.composeprefs.ui.PrefsScreen
import com.wieb027.composeprefs.ui.prefs.CheckBoxPref
import com.wieb027.composeprefs.ui.prefs.DropDownPref
import com.wieb027.composeprefs.ui.prefs.EditTextPref
import com.wieb027.composeprefs.ui.prefs.ListPref
import com.wieb027.composeprefs.ui.prefs.MultiSelectListPref
import com.wieb027.composeprefs.ui.prefs.SliderPref
import com.wieb027.composeprefs.ui.prefs.SwitchPref
import com.wieb027.composeprefs.ui.prefs.TextPref

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun SettingsScreen() {
    Scaffold(topBar = { SettingsTopBar() }) { it ->

        PrefsScreen(dataStore = LocalContext.current.dataStore) {

            prefsGroup({
                GroupHeader(
                    title = "Kunde",
                    fontSize = MaterialTheme.typography.h5.fontSize,
                    modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
                    contentAlignment = Alignment.TopCenter,
                )
            }) {
                prefsGroup({
                    GroupHeader(
                        title = " 2 Kunde 2 platbricks",
                        fontSize = MaterialTheme.typography.h6.fontSize,
                        modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
                        contentAlignment = Alignment.TopCenter,
                    )
                }) {
                    prefsItem {
                        TextPref(
                            title = "Zeige Dialog",
                            summary = "But enabled this time",
                            enabled = true
                        )
                    }
                    prefsItem {
                        TextPref(
                            title = "Andere Einstellung",
                            summary = "But with lower opacity text",
                            darkenOnDisable = true
                        )
                    }
                }
                prefsItem {
                    TextPref(
                        title = "Just some text",
                        summary = "But with a leading icon",
                        leadingIcon = { Icon(Icons.Filled.Info, "Info") })
                }

                prefsGroup({
                    GroupHeader(
                        title = "Wareneingang",
                        fontSize = MaterialTheme.typography.h6.fontSize,
                        modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
                        contentAlignment = Alignment.TopCenter
                    )
                }) {
                    prefsItem { EditTextPref(key = "pt-sub-et1", title = "EditText example") }
                    prefsItem {
                        TextPref(
                            title = "Just some text",
                            summary = "But enabled this time",
                            enabled = true
                        )
                    }
                    prefsItem {
                        TextPref(
                            title = "Just some text",
                            summary = "But with lower opacity text",
                            darkenOnDisable = true
                        )
                    }
                }

                prefsGroup({
                    GroupHeader(
                        title = "Einlagerung",
                        fontSize = MaterialTheme.typography.h6.fontSize,
                        modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
                        contentAlignment = Alignment.TopCenter
                    )
                }) {
                    prefsItem { EditTextPref(key = "pt-sub3-et1", title = "EditText example") }
                    prefsItem {
                        TextPref(
                            title = "Just some text",
                            summary = "But enabled this time",
                            enabled = true
                        )
                    }
                    prefsItem {
                        TextPref(
                            title = "Just some text",
                            summary = "But with lower opacity text",
                            darkenOnDisable = true
                        )
                    }
                }
            }

            prefsGroup({
                GroupHeader(
                    title = "Logging",
                    fontSize = MaterialTheme.typography.h6.fontSize,
                    modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
                    contentAlignment = Alignment.TopCenter
                )
            }) {
                prefsItem { EditTextPref(key = "pt-et2", title = "EditText example") }
                prefsItem {
                    TextPref(
                        title = "Just some text",
                        summary = "But enabled this time",
                        enabled = true
                    )
                }
                prefsItem {
                    TextPref(
                        title = "Just some text",
                        summary = "But with lower opacity text",
                        darkenOnDisable = true
                    )
                }
            }


            prefsGroup({
                GroupHeader(
                    title = "TextPref",
                    fontSize = MaterialTheme.typography.subtitle2.fontSize,
                    modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
                    contentAlignment = Alignment.TopCenter,
                )
            }) {
                prefsItem { TextPref(title = "Just some text") }
                prefsItem { TextPref(title = "Just some text", summary = "But now with a summary") }
                prefsItem {
                    TextPref(
                        title = "Just some text",
                        summary = "But enabled this time",
                        enabled = true
                    )
                }
                prefsItem {
                    TextPref(
                        title = "Just some text",
                        summary = "But with lower opacity text",
                        darkenOnDisable = true
                    )
                }
                prefsItem {
                    TextPref(
                        title = "Just some text",
                        summary = "But with a leading icon",
                        leadingIcon = { Icon(Icons.Filled.Info, "Info") })
                }
            }

            prefsGroup("CheckBoxPref") {
                prefsItem { CheckBoxPref(key = "cb1", title = "Simple checkbox") }
                prefsItem {
                    CheckBoxPref(
                        key = "cb2",
                        title = "Simple checkbox",
                        summary = "But it has some summary text"
                    )
                }
                prefsItem {
                    CheckBoxPref(
                        key = "cb3",
                        title = "Simple checkbox",
                        summary = "But it's disabled",
                        enabled = false
                    )
                }
                prefsItem {
                    CheckBoxPref(
                        key = "cb4",
                        title = "Simple checkbox",
                        summary = "But with a leading icon",
                        leadingIcon = { Icon(Icons.Filled.Person, "Person") })
                }
            }

            prefsGroup("SwitchPref") {
                prefsItem { SwitchPref(key = "sw1", title = "Simple switch") }
                prefsItem {
                    SwitchPref(
                        key = "sw2",
                        title = "Simple switch",
                        summary = "But it has some summary text"
                    )
                }
                prefsItem {
                    SwitchPref(
                        key = "sw3",
                        title = "Simple switch",
                        summary = "But it's disabled",
                        enabled = false
                    )
                }
                prefsItem {
                    SwitchPref(
                        key = "sw4",
                        title = "Simple switch",
                        summary = "But with a leading icon",
                        leadingIcon = { Icon(Icons.Filled.Home, "Home") })
                }
            }

            prefsGroup("EditTextPref") {
                prefsItem { EditTextPref(key = "et1", title = "EditText example") }
                prefsItem {
                    EditTextPref(
                        key = "et2",
                        title = "EditText example",
                        summary = "But it has some summary text"
                    )
                }
                prefsItem {
                    EditTextPref(
                        key = "et4",
                        title = "EditText example",
                        summary = "But it has a dialog title and message",
                        dialogTitle = "Dialog Title",
                        dialogMessage = "Dialog Message"
                    )
                }
                prefsItem {
                    EditTextPref(
                        key = "et5",
                        title = "EditText example",
                        summary = "But it has a custom dialog color",
                        dialogBackgroundColor = MaterialTheme.colors.secondary
                    )
                }
            }

            prefsGroup("SliderPref") {
                prefsItem { SliderPref(key = "sp1", title = "Slider example") }
                prefsItem {
                    SliderPref(
                        key = "sp3",
                        title = "Slider example with custom range and value shown on side",
                        valueRange = 50f..200f,
                        showValue = true
                    )
                }
                prefsItem {
                    SliderPref(
                        key = "sp4",
                        title = "Slider example with 4 steps and value shown",
                        steps = 4,
                        showValue = true
                    )
                }
            }

            prefsGroup("DropDownPref") {
                prefsItem {
                    DropDownPref(
                        key = "dd1",
                        title = "Dropdown example",
                        summary = "With custom summary",
                        entries = mapOf(
                            "0" to "Entry 1",
                            "1" to "Entry 2",
                            "2" to "Entry 3"
                        )
                    )
                }
                prefsItem {
                    DropDownPref(
                        key = "dd2",
                        title = "Dropdown with selected as summary",
                        useSelectedAsSummary = true,
                        entries = mapOf(
                            "0" to "Entry 1",
                            "1" to "Entry 2",
                            "2" to "Entry 3"
                        )
                    )
                }
            }

            prefsGroup("ListPref") {
                prefsItem {
                    ListPref(
                        key = "l1",
                        title = "ListPref example",
                        summary = "Opens up a dialog of options",
                        entries = mapOf(
                            "0" to "Entry 1",
                            "1" to "Entry 2",
                            "2" to "Entry 3",
                            "3" to "Entry 4",
                            "4" to "Entry 5",
                            "5" to "Entry 6",
                            "6" to "Entry 7",
                            "7" to "Entry 8",
                            "8" to "Entry 9",
                            "9" to "Entry 10",
                            "10" to "Entry 11",
                            "11" to "Entry 12",
                            "12" to "Entry 13",
                            "13" to "Entry 14",
                            "14" to "Entry 15",
                            "15" to "Entry 16",
                            "16" to "Entry 17",
                            "17" to "Entry 18"
                        )
                    )
                }
                prefsItem {
                    ListPref(
                        key = "l2",
                        title = "ListPref example",
                        summary = "Summary is the currently selected item",
                        useSelectedAsSummary = true,
                        entries = mapOf(
                            "0" to "Entry 1",
                            "1" to "Entry 2",
                            "2" to "Entry 3",
                            "3" to "Entry 4",
                            "4" to "Entry 5"
                        )
                    )
                }
            }

            prefsGroup("MultiSelectListPref") {
                prefsItem {
                    MultiSelectListPref(
                        key = "msl1",
                        title = "MultiSelectListPref",
                        summary = "Pick multiple entries at once",
                        entries = mapOf(
                            "0" to "Entry 1",
                            "1" to "Entry 2",
                            "2" to "Entry 3",
                            "3" to "Entry 4",
                            "4" to "Entry 5",
                            "5" to "Entry 6"
                        )
                    )
                }

                prefsItem {
                    MultiSelectListPref(
                        key = "msl2",
                        title = "MultiSelectListPref",
                        summary = "Pick multiple entries at once from a long list",
                        entries = mapOf(
                            "0" to "Entry 1",
                            "1" to "Entry 2",
                            "2" to "Entry 3",
                            "3" to "Entry 4",
                            "4" to "Entry 5",
                            "5" to "Entry 6",
                            "6" to "Entry 7",
                            "7" to "Entry 8",
                            "8" to "Entry 9",
                            "9" to "Entry 10",
                            "10" to "Entry 11",
                            "11" to "Entry 12",
                            "12" to "Entry 13",
                            "13" to "Entry 14",
                            "14" to "Entry 15",
                            "15" to "Entry 16",
                            "16" to "Entry 17",
                            "17" to "Entry 18"
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsTopBar() {
    TopAppBar(
        title = {
            Text(
                text = "Settings",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    )
}