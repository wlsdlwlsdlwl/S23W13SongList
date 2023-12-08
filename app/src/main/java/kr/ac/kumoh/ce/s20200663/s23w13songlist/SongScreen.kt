package kr.ac.kumoh.ce.s20200663.s23w13songlist

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage

enum class SongScreen {
    List,
    Detail
}

@Composable
fun SongApp(songList: List<Song>) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SongScreen.List.name,
    ) {
        composable(route = SongScreen.List.name) {
            SongList(songList) {
                //itdms onNacigateToDetail()의 인자로 전달되는 String
                //"Detail/$index"
                navController.navigate(it)
            }
        }
        composable(route = SongScreen.Detail.name + "/{index}",
            arguments = listOf(navArgument("index") {
                type = NavType.IntType
            })
        ) {
            val index = it.arguments?.getInt("index") ?: -1
            if(index>=0)
                SongDetail(songList[index])
        }
    }
}

@Composable
fun SongList(list: List<Song>, onNavigateToDetail: (String) -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(list.size) {
            SongItem(it, list[it], onNavigateToDetail)
        }
    }
}

@Composable
fun SongItem(index: Int, song: Song, onNavigateToDetail: (String) -> Unit) {
    //var expanded by remember { mutableStateOf(false)}

    Card(
        modifier = Modifier.clickable {
            onNavigateToDetail(SongScreen.Detail.name + "/$index")
        },
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                //.background(Color(255, 210, 210))
                .padding(8.dp)
        ) {
            AsyncImage(
                model = "https://picsum.photos/300/300?random=${song.id}",
                contentDescription = "노래 앨범 이미지",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    //.clip(CircleShape)
                    .clip(RoundedCornerShape(percent = 10)),
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceAround
                //verticalArrangement = Arrangement.SpaceEvenly
            ) {
                TextTitle(song.title)
                TextSinger(song.singer)
            }
        }
//        AnimatedVisibility(visible = expanded) {
//            song.lyrics?.let {Text(it)}
//        }
    }
}

@Composable
fun TextTitle(title: String) {
    Text(title, fontSize = 30.sp)
}

@Composable
fun TextSinger(singer: String) {
    Text(singer, fontSize = 20.sp)
}

@Composable
fun SongDetail(song: Song) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RatingBar(song.rating)
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            song.title,
            fontSize = 40.sp,
            textAlign = TextAlign.Center,
            lineHeight = 45.sp
        )
        Spacer(modifier = Modifier.height(16.dp))

        AsyncImage(
            model = "https://picsum.photos/300/300?random=${song.id}",
            contentDescription = "노래 앨범 이미지",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(400.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = "https://i.pravatar.cc/100?u=${song.singer}",
                contentDescription = "가수 이미지",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(50.dp).clip(CircleShape)
            )
            Text(song.singer, fontSize = 30.sp)
        }
        Spacer(modifier = Modifier.height(32.dp))

        song.lyrics?.let {
            Text(
                it,
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                lineHeight = 35.sp
            )
        }
        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/results?search_query=노래방+${song.title}")
            )
            startActivity(context, intent, null)
        }) {
            Text("YouTube 노래방 검색")
        }
    }
}


@Composable
fun RatingBar(stars: Int) {
    Row {
        repeat(stars) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "starts",
                modifier = Modifier.size(48.dp),
                tint = Color.Red)
        }
    }
}