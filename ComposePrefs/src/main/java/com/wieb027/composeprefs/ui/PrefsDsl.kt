package com.wieb027.composeprefs.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.wieb027.composeprefs.ui.GroupHeader
import kotlin.invoke

/**
 * Receiver scope which is used by [PrefsScreen].
 */
interface PrefsScope {
    /**
     * Adds a single Pref
     *
     * @param content the content of the item
     */
    fun prefsItem(content: @Composable PrefsScope.() -> Unit)

    /**
     * Adds a group of Prefs with a title.
     *
     * @param title Group header text. Will be shown above the list of Prefs
     * @param headerFontSize Font size of the header Text
     * @param modifier Modifier for the header
     * @param contentAlignment Alignment of the text
     * @param items All the prefs in this group
     */
    fun prefsGroup(title: String, headerFontSize: TextUnit = TextUnit.Unspecified, modifier: Modifier = Modifier.fillMaxWidth().padding(start = 16.dp), contentAlignment: Alignment = Alignment.TopCenter, items: PrefsScope.() -> Unit)

    /**
     * Adds a group of Prefs. This overload is intended for passing in a [GroupHeader] if you want more control over the header.
     *
     * @param header Group header. Will be shown above the list of Prefs
     * @param items All the prefs in this group
     */
    fun prefsGroup(header: @Composable PrefsScope.() -> Unit, items: PrefsScope.() -> Unit)

    fun prefsSubGroup(header: @Composable PrefsScope.() -> Unit, items: PrefsScope.() -> Unit)
}

internal class PrefsScopeImpl : PrefsScope {

    private var _headerIndexes: MutableList<Int> = mutableListOf()
    val headerIndexes: List<Int> get() = _headerIndexes

    private var _footerIndexes: MutableList<Int> = mutableListOf()
    val footerIndexes: List<Int> get() = _footerIndexes

    private var _prefsItems: MutableList<PrefsItem> = mutableListOf()
    val prefsItems: List<PrefsItem> get() = _prefsItems

    override fun prefsItem(content: @Composable PrefsScope.() -> Unit) {
        _prefsItems.add(
            PrefsItem(
                content = { @Composable { content() } }
            )
        )
    }

    override fun prefsGroup(title: String, headerFontSize: TextUnit, modifier: Modifier, contentAlignment: Alignment, items: PrefsScope.() -> Unit) {
        // add header index so we know where each group starts, includes the spacers
        _headerIndexes.addAll(this.prefsItems.size + 0 until this.prefsItems.size + 3)


        if (!_headerIndexes.contains(this.prefsItems.size-1)) {
            this.prefsItem {
                Spacer(modifier = Modifier.height(12.dp))
            }
        } else {
            _prefsItems.removeAt(_prefsItems.lastIndex)
            _headerIndexes.removeAt(_headerIndexes.lastIndex)
            _headerIndexes.removeAt(_headerIndexes.lastIndex)
        }

        this.prefsItem {
            GroupHeader(
                title = title,
                fontSize = headerFontSize,
                modifier = modifier,
                contentAlignment = contentAlignment
            )
        }

        this.prefsItem {
            Spacer(modifier = Modifier.height(12.dp))
        }

        // add all children to hierarchy
        this.apply(items)

        // add totalSize -2/-1 to footerIndexes as that is the index of the last item added and the spacer respectively
        _footerIndexes.add(this.prefsItems.size - 2)
        _footerIndexes.add(this.prefsItems.size - 1)
    }

    override fun prefsGroup(
        header: @Composable PrefsScope.() -> Unit,
        items: PrefsScope.() -> Unit
    ) {
        // add header index so we know where each group starts, includes the spacers
        _headerIndexes.addAll(this.prefsItems.size + 0 until this.prefsItems.size + 3)

        if (!_headerIndexes.contains(this.prefsItems.size-1)) {
            this.prefsItem {
                Spacer(modifier = Modifier.height(12.dp))
            }
        } else {
            _prefsItems.removeAt(_prefsItems.lastIndex)
            _headerIndexes.removeAt(_headerIndexes.lastIndex)
            _headerIndexes.removeAt(_headerIndexes.lastIndex)
        }

        // Replace the relevant part in prefsGroup(header, items)
        this.prefsItem {
            // Collect child items in a temporary scope
            val tempScope = PrefsScopeImpl()
            tempScope.apply(items)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    header()
                    Spacer(modifier = Modifier.height(12.dp))
                    Divider()
                    // Render collected child items inside the card
                    tempScope.prefsItems.forEachIndexed { idx, item ->
                        item.content.invoke(tempScope, idx)()
                    }
                }
            }
        }

        this.prefsItem {
            Spacer(modifier = Modifier.height(12.dp))
        }

        // add totalSize -2/-1 to footerIndexes as that is the index of the last item added and the spacer respectively
        _footerIndexes.add(this.prefsItems.size - 2)
        _footerIndexes.add(this.prefsItems.size - 1)
    }

    override fun prefsSubGroup(
        header: @Composable (PrefsScope.() -> Unit),
        items: PrefsScope.() -> Unit
    ) {
        // add header index so we know where each group starts, includes the spacers
        _headerIndexes.addAll(this.prefsItems.size + 0 until this.prefsItems.size + 3)

        if (!_headerIndexes.contains(this.prefsItems.size-1)) {
            this.prefsItem {
                Spacer(modifier = Modifier.height(12.dp))
            }
        } else {
            _prefsItems.removeAt(_prefsItems.lastIndex)
            _headerIndexes.removeAt(_headerIndexes.lastIndex)
            _headerIndexes.removeAt(_headerIndexes.lastIndex)
        }

        this.prefsItem {
            header()
        }

        this.prefsItem {
            Spacer(modifier = Modifier.height(12.dp))
            Divider()
        }

        // add all children to hierarchy
        this.apply(items)

        // add totalSize -2/-1 to footerIndexes as that is the index of the last item added and the spacer respectively
        _footerIndexes.add(this.prefsItems.size - 2)
        _footerIndexes.add(this.prefsItems.size - 1)
    }

    fun getPrefsItem(index: Int): @Composable () -> Unit {
        val prefsItem = prefsItems[index]
        return prefsItem.content.invoke(this, index)
    }
}

internal class PrefsItem(
    val content: PrefsScope.(index: Int) -> @Composable () -> Unit
)