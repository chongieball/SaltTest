package com.chongieball.salttest.ui.screen.home

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.chongieball.salttest.data.ProcessState

@Composable
fun HomeScreen(userId: Int, homeViewModel: HomeViewModel) {
    val userState = homeViewModel.userState.observeAsState(ProcessState.initial())

    val context = LocalContext.current

    LaunchedEffect(userId) {
        homeViewModel.getUser(userId)
    }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (loading, ivAvatar, tvEmail, tvFullName) = createRefs()

        if (userState.value.isLoading()) {
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp).constrainAs(loading) {
                  centerTo(parent)
                },
                color = Color.White
            )
        } else if (userState.value.isSuccess()) {
            val userData = (userState.value as ProcessState.Success).data
            val chainRef = createVerticalChain(ivAvatar, tvEmail, tvFullName)
            constrain(chainRef) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }

            AsyncImage(
                model = userData.avatar,
                contentScale = ContentScale.Crop,
                modifier = Modifier.clip(CircleShape).constrainAs(ivAvatar) {
                    top.linkTo(parent.top)
                    centerHorizontallyTo(parent)
                },
                contentDescription = "avatar"
            )

            Text(text = userData.email, modifier = Modifier.constrainAs(tvEmail) {
                top.linkTo(ivAvatar.bottom, margin = 16.dp)
                bottom.linkTo(tvFullName.top)
                centerHorizontallyTo(parent)
            })

            Text(text = userData.fullName, modifier = Modifier.constrainAs(tvFullName) {
                top.linkTo(tvEmail.bottom, margin = 16.dp)
                bottom.linkTo(parent.bottom)
                centerHorizontallyTo(parent)
            })
        } else if (userState.value.isError()) {
            val error = (userState.value as ProcessState.ErrorState)
            Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
        }

    }
}