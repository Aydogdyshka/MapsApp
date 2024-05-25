package com.example.mapsnavigation.common.presentation.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mapsnavigation.common.presentation.model.UILocation
import com.example.mapsnavigation.ui.theme.MapsNavigationTheme

@Composable
fun LocationListView(
    modifier: Modifier,
    locations: List<UILocation>
){
    LazyColumn(modifier = modifier) {
        items(locations) {
            ListItemView(item = it)
        }
    }
}

@Composable
fun ListItemView(item: UILocation) {
    // Layout for each item in the list
    Row(modifier = Modifier.padding(16.dp)) {
        Text("Number: ${item.id}", modifier = Modifier.padding(end = 16.dp))
        Column {
            Text("Latitude: ${item.latitude}", modifier = Modifier.padding(end = 16.dp))
            Text("Longitude: ${item.longitude}", modifier = Modifier.padding(end = 16.dp))
        }
        Text("Date: ${item.date}", modifier = Modifier.padding(end = 16.dp))
    }
}
@Preview
@Composable
fun LocationListViewPreview(){
    MapsNavigationTheme {
        //LocationListView()
    }
}