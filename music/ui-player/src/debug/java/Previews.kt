import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.looker.ui_player.Player
import com.looker.ui_player.R

@Preview
@Composable
fun PlayerPreview() {
    Player(
        songName = "Baila Conmigo",
        artistName = "Selena Gomez, Rauw Alejandro",
        albumArt = R.drawable.error_image
    )
}