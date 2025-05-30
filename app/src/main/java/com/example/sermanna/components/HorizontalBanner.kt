import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sermanna.R

@Composable
fun SwipeableBanner() {
    var currentPage by remember { mutableStateOf(0) }
    val pages =
        listOf(R.drawable.cleaning_service, R.drawable.handyman, R.drawable.furniture_assembly)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { _, dragAmount ->
                        if (dragAmount > 100) {
                            // Swipe right
                            currentPage = (currentPage - 1).coerceAtLeast(0)
                        } else if (dragAmount < -100) {
                            // Swipe left
                            currentPage = (currentPage + 1).coerceAtMost(pages.size - 1)
                        }
                    }
                }
        ) {
            Image(
                painter = painterResource(id = pages[currentPage]),
                contentDescription = "Banner $currentPage",
                modifier = Modifier.fillMaxSize()
            )
        }

        DotsIndicator(currentPage = currentPage, totalPages = pages.size)
    }
}

@Composable
fun DotsIndicator(currentPage: Int, totalPages: Int) {
    Row(
        modifier = Modifier.padding(8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 0 until totalPages) {
            Dot(isSelected = currentPage == i)
        }
    }
}

@Composable
fun Dot(isSelected: Boolean) {
    val color = if (isSelected) Color.Blue else Color.Gray
    Box(
        modifier = Modifier
            .size(8.dp)
            .padding(4.dp)
            .background(color)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewSwipeableBanner() {
    SwipeableBanner()
}
