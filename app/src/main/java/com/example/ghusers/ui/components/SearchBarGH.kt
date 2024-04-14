package com.example.ghusers.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.ghusers.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarGH(
    @StringRes placeholder: Int,
    modifier: Modifier? = null,
    onSearch: (String) -> Unit = {}
) {
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    var searchHistory by remember {  mutableStateOf(mutableSetOf<String>()) }
    SearchBar(
        query = query,
        onQueryChange = {query = it},
        onSearch = {
            onSearch(it)
            active = false
            searchHistory.add(it)

        },
        active = active,
        onActiveChange = {
            active = it
        },
        placeholder = { Text(text = stringResource(id = placeholder))},
        leadingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")},
        trailingIcon = {
            if (active) {
                IconButton(onClick = {if (query.isNotEmpty()) query = "" else active = false }) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
                }
            } else null
        },
        modifier = if (modifier != null) modifier else Modifier

    ) {
        searchHistory.toList().takeLast(3).forEach { item ->
            ListItem(
                modifier = Modifier.clickable { query = item },
                headlineContent = { Text(text = item) },
                leadingContent = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_history),
                        contentDescription = null
                    )
                }
            )
        }
    }
}