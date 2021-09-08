import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.looker.howlmusic.ui.components.BottomNavigationItems

@Preview(showBackground = true)
@Composable
fun BottomNavigationItemPreview() {
    Row {
        BottomNavigationItems(
            icon = Icons.Default.Person,
            label = "Person",
            selected = true
        ) {}
        BottomNavigationItems(
            icon = Icons.Default.Person,
            label = "Person",
            selected = false
        ) {}
    }
}