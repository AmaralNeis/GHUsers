package com.example.ghusers.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun TextWithIconGH(text: String,
                   icon: ImageVector? = null,
                   @DrawableRes iconDrawable: Int? = null,
                   color: Color = Color.Black,
                   textColor: Color = Color.Gray,
                   onClick: (() -> Unit)? = null
) {
    Row(modifier = if (onClick != null) Modifier.clickable { onClick() } else Modifier) {
        icon?.let {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color
            )
        }

        iconDrawable?.let {
            Icon(
                painter = painterResource(id = iconDrawable),
                contentDescription = null,
                tint = color
            )
        }

        Text(
            text = text,
            color = textColor,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = 8.dp)

        )
    }
}