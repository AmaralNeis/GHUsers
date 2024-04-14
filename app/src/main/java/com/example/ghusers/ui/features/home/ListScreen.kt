package com.example.ghusers.ui.features.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ghusers.R
import com.example.ghusers.model.UserItem
import com.example.ghusers.ui.components.ErrorScreen
import com.example.ghusers.ui.components.LoadingIndicator
import com.example.ghusers.ui.components.SearchBarGH
import com.example.ghusers.ui.theme.GHUsersTheme
import com.skydoves.landscapist.glide.GlideImage


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListScreen(viewModel: HomeViewModel = hiltViewModel(), onItemSelectd: (String) -> Unit = {}) {

    val uiState by viewModel.state.collectAsState()
    val items = uiState.items
    val isFirstLoading = uiState.isFirstLoading()
    val isLoading = uiState.isLoading
    val showError = uiState.showError

    val pullRequestState = rememberPullRefreshState(
        refreshing =  isLoading,
        onRefresh = { uiState.onRefresh() }
    )

    when {
        isFirstLoading -> { LoadingIndicator() }
        showError -> {
            ErrorScreen(
                title = R.string.error_users_list_title,
                subtitle = R.string.error_users_list_subtitle) {
                uiState.onRefresh()
            }
        }
         else -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Column {
                    SearchBarGH(
                        placeholder = R.string.search_user,
                        modifier = Modifier.padding(16.dp),
                        onSearch= uiState.onSearch
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .pullRefresh(state = pullRequestState),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(items.size) { index -> ListItem(item = items[index], onItemSelectd) }
                    }
                }

                PullRefreshIndicator(refreshing = uiState.isLoading, state = pullRequestState, Modifier.align(
                    Alignment.TopCenter))
            }
        }
    }
}

@Composable
fun ListItem(item: UserItem,  onItemSelectd: (String) -> Unit = {}) {
    Column(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .clickable {
                    item.login?.let(onItemSelectd)
                }
        ) {
            GlideImage(
                imageModel = item.avatarUrl,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(22.dp))
            )
            Text(
                text = item.login ?: "",
                fontSize = 18.sp,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        Divider(modifier = Modifier.height(0.5.dp))
    }
}



@Preview(showBackground = true)
@Composable
fun ListPreview() {
    GHUsersTheme {
        ListScreen()
    }
}