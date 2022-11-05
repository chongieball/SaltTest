package com.chongieball.salttest.ui.screen.login

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.chongieball.salttest.data.ProcessState
import com.chongieball.salttest.ui.Route

@Composable
fun LoginScreen(loginViewModel: LoginViewModel, navHostController: NavHostController?) {
    val loginState = loginViewModel.loginState.observeAsState(ProcessState.initial())
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordVisibility = remember { mutableStateOf(false) }
    val errorMessage = rememberSaveable { mutableStateOf("") }
    val isEmailValid = rememberSaveable { mutableStateOf(false) }
    val isPasswordValid = rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current

    if (loginState.value.isSuccess()) {
        val userId = (loginState.value as ProcessState.Success).data
        LaunchedEffect("navigation") {
            navHostController?.navigate("${Route.HOME}/${userId}")
        }
    }

    if (loginState.value.isError()) {
        val error = (loginState.value as ProcessState.ErrorState)
        Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
    }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (textLogin, emailTextField, passwordField, buttonLogin) = createRefs()

        val chainRef = createVerticalChain(textLogin, emailTextField, passwordField, buttonLogin, chainStyle = ChainStyle.Packed)

        constrain(chainRef) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
        }

        Text(text = "Sign in your account", modifier = Modifier.constrainAs(textLogin) {
            top.linkTo(parent.top)
            bottom.linkTo(emailTextField.top)
            centerHorizontallyTo(parent)
        })

        TextField(value = email.value,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
            ),
            placeholder = { Text("Email") },
            onValueChange = {
                if (it.isEmpty()) {
                    errorMessage.value = "Email must not empty"
                } else if (!Patterns.EMAIL_ADDRESS.matcher(it).matches()) {
                    errorMessage.value = "Email not valid"
                } else {
                    errorMessage.value = ""
                    isEmailValid.value = true
                }
                email.value = it
            }, modifier = Modifier.constrainAs(emailTextField) {
                top.linkTo(textLogin.bottom, margin = 16.dp)
                bottom.linkTo(passwordField.top)
                centerHorizontallyTo(parent)
            })

        TextField(value = password.value,
            visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
            ),
            trailingIcon = {
                val image =
                    if (passwordVisibility.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

                IconButton(onClick = {
                    passwordVisibility.value = !passwordVisibility.value
                }) {
                    Icon(imageVector = image, "")
                }
            },
            placeholder = { Text("Password") },
            onValueChange = {
                if (it.length < 8) {
                    errorMessage.value = "Password minimal 8 character"
                } else {
                    errorMessage.value = ""
                    isPasswordValid.value = true
                }
                password.value = it
            }, modifier = Modifier.constrainAs(passwordField) {
                top.linkTo(emailTextField.bottom, margin = 16.dp)
                bottom.linkTo(buttonLogin.top)
                centerHorizontallyTo(parent)

            })

        Button(
            modifier = Modifier.width(275.dp).constrainAs(buttonLogin) {
                top.linkTo(passwordField.bottom, margin = 32.dp)
                bottom.linkTo(parent.bottom)
                centerHorizontallyTo(parent)
            },
            onClick = {
                if (isEmailValid.value && isPasswordValid.value) loginViewModel.login(
                    email.value,
                    password.value
                )
            },
            enabled = !loginState.value.isLoading() && (isEmailValid.value && isPasswordValid.value)
        ) {
            if (loginState.value.isLoading()) CircularProgressIndicator() else Text(
                text = "Login"
            )
        }
    }
}