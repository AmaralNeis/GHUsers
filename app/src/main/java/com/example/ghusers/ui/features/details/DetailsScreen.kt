package com.example.ghusers.ui.features.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ghusers.R
import com.example.ghusers.model.Repository
import com.example.ghusers.model.UserDetail
import com.example.ghusers.ui.components.EmptyViewGH
import com.example.ghusers.ui.components.LoadingIndicator
import com.example.ghusers.ui.components.TextWithIconGH
import com.example.ghusers.ui.theme.GHUsersTheme
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun DetailsScreen(viewModel: DetailsViewModel = hiltViewModel(),
                  userLogin: String,
                  onBack: () -> Unit = {},
                  onShare: (String) -> Unit = {},
                  openUrl: (String) -> Unit = {}) {

    val uiState by viewModel.state.collectAsState()
    val user = uiState.user
    val repos = uiState.repos
    val showEmptyRepository = uiState.showEmptyRepository
    val isLoading = uiState.isLoading


    LaunchedEffect( Unit) {
        uiState.fetchUser(userLogin)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            ToolbarDetailsScreen(htmlUrl = user?.htmlUrl, onBack, onShare)
            DetailsHeaderView(user = user, openUrl)

            when {
                isLoading -> { LoadingIndicator() }
                showEmptyRepository -> { DetailsEmptyView() }
                else -> { repos?.let { DetailsListView(repos = it) } }
            }
        }
    }
}
@Composable
private fun ToolbarDetailsScreen(
    htmlUrl: String?,
    onBack: () -> Unit = {},
    onShare: (String) -> Unit = {},) {
    Box(modifier = Modifier.height(44.dp)){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            htmlUrl?.let {
                IconButton(onClick = {onShare(htmlUrl)}) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

        }
    }
    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
private fun DetailsHeaderView(user: UserDetail?, openUrl: (String) -> Unit = {}) {
    Column(Modifier.padding(horizontal = 16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            GlideImage(
                imageModel = user?.avatar,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(40.dp))
            )
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                user?.name?.let {
                    Text(
                        text = it,
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }

                user?.login?.let {
                    Text(
                        text = it,
                        fontSize = 16.sp,
                        color = Color.Gray,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }
            }

        }

        user?.bio?.let {
            Text(
                text = it,
                fontSize = 16.sp,
                style = MaterialTheme.typography.bodySmall,
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ){
            user?.company?.let {
                TextWithIconGH(
                    text = it,
                    iconDrawable = R.drawable.ic_apartment,
                    color =  MaterialTheme.colorScheme.primary)
            }

            user?.location?.let {
                TextWithIconGH(
                    text = it,
                    iconDrawable = R.drawable.ic_location,
                    color =  MaterialTheme.colorScheme.primary
                )
            }
        }

        user?.blog?.let {url ->
            if (url.isNotBlank()) {
                TextWithIconGH(
                    text = url,
                    iconDrawable = R.drawable.ic_link,
                    color = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.primary
                ){
                    openUrl(url)
                }
            }

        }

        //TODO: Validar o texo de seguidores para somente uma linha
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ){
            user?.followers?.let {
                TextWithIconGH(
                    text = pluralStringResource(id = R.plurals.followers_count, count = it, it),
                    icon = Icons.Default.Person,
                    color = MaterialTheme.colorScheme.primary)

            }

            user?.following?.let {
                TextWithIconGH(
                    text = stringResource(id = R.string.following_count, it),
                    icon = Icons.Default.Person,
                    color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
private fun DetailsListView(repos: List<Repository>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Text(text = stringResource(id = R.string.repositories))
        }
        items(repos.size, key = { index -> repos[index].id ?: -1   }) { index ->
            DetailsRepositoryCard(repo = repos[index])
        }
    }
}

@Composable
private fun DetailsRepositoryCard(repo: Repository) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        Column(modifier = Modifier.padding(8.dp)) {
            repo.name?.let {
                Text(
                    text = it,
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

            }
            repo.description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(4.dp))
            }

            repo.language?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )



            }
            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color.Gray, thickness = 0.5.dp)


        }
    }
}

@Composable
private fun DetailsEmptyView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        EmptyViewGH(
            title = R.string.empty_repository_title,
            subtitle = R.string.empty_repository_subtitle
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DetailsPreview() {
    GHUsersTheme {
        DetailsRepositoryCard(Repository(1, "teste-assasa", "Em dezembro de 81", "Swift"))
    }
}