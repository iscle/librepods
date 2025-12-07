package me.kavishdevar.librepods.presentation.ui.rename

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.capsule.ContinuousCapsule
import me.kavishdevar.librepods.R
import me.kavishdevar.librepods.presentation.ui.component.LiquidDialog

@Composable
fun RenameDialog(
    name: String,
    onNameChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
) {
    LiquidDialog(
        onDismissRequest = onDismissRequest,
        backdrop = rememberLayerBackdrop()
    ) {
        Column(
            modifier = Modifier
                .padding(14.dp)
        ) {
            val focusRequester = remember { FocusRequester() }
            val keyboardController = LocalSoftwareKeyboardController.current

            var nameTextFieldValue by remember(name) {
                mutableStateOf(TextFieldValue(name))
            }

            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
                keyboardController?.show()
                nameTextFieldValue = nameTextFieldValue.copy(
                    selection = TextRange(nameTextFieldValue.text.length)
                )
            }

            val isLightTheme = !isSystemInDarkTheme()
            val contentColor = if (isLightTheme) Color.Black else Color.White
            val accentColor =
                if (isLightTheme) Color(0xFF0088FF)
                else Color(0xFF0091FF)
            val containerColor =
                if (isLightTheme) Color(0xFFFAFAFA).copy(0.6f)
                else Color(0xFF121212).copy(0.4f)

            BasicText(
                text = stringResource(R.string.name),
                Modifier.padding(28.dp, 24.dp, 28.dp, 12.dp),
                style = TextStyle(contentColor, 24f.sp, FontWeight.Medium)
            )

            BasicTextField(
                value = nameTextFieldValue,
                onValueChange = {
                    nameTextFieldValue = it
                },
                modifier = Modifier
                    .padding(24.dp, 12.dp, 24.dp, 12.dp)
                    .focusRequester(focusRequester),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 16.sp,
                    color = if (isLightTheme) Color.Black else Color.White
                ),
                singleLine = true,
                cursorBrush = SolidColor(if (isLightTheme) Color.Black else Color.White),
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier
                            .height(56.dp)
                            .fillMaxWidth()
                            .background(
                                if (isLightTheme) Color.White else Color.Black,
                                RoundedCornerShape(26.dp)
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            innerTextField()
                        }

                        Text(
                            text = "\uDBC0\uDC61",
                            modifier = Modifier.clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                nameTextFieldValue = nameTextFieldValue.copy(text = "")
                            },
                            fontSize = 16.sp,
                            color = if (isLightTheme) Color.Black.copy(alpha = 0.6f) else Color.White.copy(
                                alpha = 0.6f
                            )
                        )
                    }
                }
            )

            Row(
                Modifier
                    .padding(24.dp, 12.dp, 24.dp, 24.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    Modifier
                        .clip(ContinuousCapsule)
                        .background(containerColor.copy(0.2f))
                        .clickable {
                            onDismissRequest()
                        }
                        .height(48.dp)
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(
                        4.dp,
                        Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicText(
                        text = "Cancel",
                        style = TextStyle(contentColor, 16f.sp)
                    )
                }

                Row(
                    Modifier
                        .clip(ContinuousCapsule)
                        .background(accentColor)
                        .clickable {
                            onNameChange(nameTextFieldValue.text)
                            onDismissRequest()
                        }
                        .height(48.dp)
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(
                        4.dp,
                        Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicText(
                        "Save",
                        style = TextStyle(Color.White, 16f.sp)
                    )
                }
            }
        }
    }
}
