package com.hmyh.jetnote.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hmyh.jetnote.R
import com.hmyh.jetnote.components.NoteInputText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(modifier: Modifier){

    var title by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    Column(
        modifier = modifier.padding(8.dp)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.app_name)
                )
            },
            actions = {
                Icon(
                    imageVector = Icons.Rounded.Notifications,
                    contentDescription = "Icon"
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0XFFDADFE3)
            )
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NoteInputText(
                text = title,
                label = "Title",
                onTextChange = {

                }
            )

            NoteInputText(
                text = description,
                label = "Add a note",
                onTextChange = {

                }
            )
        }
    }

}

@Preview
@Composable
fun NoteScreenPreview(){
    NoteScreen(modifier = Modifier)
}