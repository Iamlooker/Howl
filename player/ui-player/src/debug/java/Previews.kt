import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.looker.ui_player.MiniPlayer
import com.looker.ui_player.R
import com.looker.ui_player.components.PlayAndSkipButton
import com.looker.ui_player.components.PreviousAndQueue

@Preview(showBackground = true)
@Composable
fun MiniPlayerPreview() {
    MiniPlayer(
        songName = "Name",
        artistName = "Name",
        albumArt = R.drawable.error_image,
        icon = Icons.Rounded.PlayArrow,
        toggled = true
    ) {}
}

@Preview(showBackground = true)
@Composable
fun ControllerPreview() {
    Column {
        PlayAndSkipButton(playIcon = Icons.Default.PlayArrow) {}
        PreviousAndQueue {}
    }
}