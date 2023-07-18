package com.riza.example.auth.register.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.riza.example.commonui.theme.MyTheme

/**
 * Created by ahmadriza on 18/07/23.
 */

data class State(
    val isLoading: Boolean = false,
    val activeSection: RegisterSection = RegisterSection.IDENTITY,
    val idNumber: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun RegisterScreen(
) {
    val state = State()
    val scrollState = rememberScrollState()

    MyTheme {
        Scaffold(
            topBar = {
                Column {
                    LargeTopAppBar(
                        title = {
                            Text(text = "Daftar")
                        },
                        scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
                    )
                    TopAppBar(
                        title = {
                            SectionTabRow()
                        },
                        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
                    )
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(scrollState)
            ) {
                IDTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.idNumber,
                    onValueChange = {}
                )
            }
        }
    }

}

enum class RegisterSection {
    IDENTITY, ADDRESS, SUMMARY
}

@Composable
fun SectionTabRow(
    activeSection: RegisterSection = RegisterSection.ADDRESS
) {
    LazyRow {
        items(RegisterSection.values()) { section ->
            SectionTab(
                number = (section.ordinal + 1).toString(),
                title = section.name,
                isActive = activeSection.ordinal >= section.ordinal,
                onClick = {
                }
            )
        }
    }
}

@Composable
fun SectionTab(
    number: String,
    title: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        val backgroundColor = if (isActive) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onSurface

        Text(
            text = number,
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .size(16.dp)
                .background(color = backgroundColor, shape = CircleShape)
        )

        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall.copy(
                color = backgroundColor
            )
        )


    }
}