package com.example.booksrepositoryapp.ui.landingPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun LandingPageScreen(
    onGetStartedClick: () -> Unit,
    onRegisterClick: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
    ) {
        Image(
            painter = painterResource(LandingPageResources.landingBg),
            contentDescription = null,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.55f),
            contentScale = ContentScale.FillBounds,
        )

        Image(
            painter = painterResource(LandingPageResources.appLogoImage),
            contentDescription = stringResource(LandingPageResources.appLogoTitle),
            modifier =
                Modifier
                    .size(250.dp)
                    .align(Alignment.Center),
        )

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 25.dp),
        ) {
            Text(
                text = stringResource(LandingPageResources.landingDescription),
                textAlign = TextAlign.Center,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
            )

            Button(
                onClick = onGetStartedClick,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                shape = RoundedCornerShape(10.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White,
                    ),
            ) {
                Text(stringResource(LandingPageResources.getStarted))
            }

            TextButton(
                onClick = onRegisterClick,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
            ) {
                Text(
                    text = stringResource(LandingPageResources.register),
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                )
            }
        }
    }
}
