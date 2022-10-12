package org.sbma_shakeit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun TabsView(selectedIndex: MutableState<Int>, headers: List<String>) {
    TabRow(selectedTabIndex = selectedIndex.value, backgroundColor = Color.White) {
        headers.forEachIndexed { index, header ->
            val selected = selectedIndex.value == index
            Tab(
                modifier = if (selected) Modifier
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colors.primaryVariant)
                    .height(50.dp)
                else Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color.White)
                    .height(50.dp),
                selected = selected,
                onClick = { selectedIndex.value = index },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Black
            ) {
                Text(header)
            }
        }
    }
}