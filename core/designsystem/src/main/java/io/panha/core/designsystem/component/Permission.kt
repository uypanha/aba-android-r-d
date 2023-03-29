package io.panha.core.designsystem.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Permission(
    permission: String,
    rational: String,
    permissionNotAvailableContent: @Composable (permissionState: PermissionState) -> Unit = { },
    content: @Composable () -> Unit = { }
) {

    val permissionState = rememberPermissionState(permission)

    PermissionRequired(
        permissionState = permissionState,
        permissionNotGrantedContent = {
            Rationale(text = rational) {
                permissionState.launchPermissionRequest()
            }
        },
        permissionNotAvailableContent = {
            permissionNotAvailableContent(permissionState)
        },
        content = content
    )
}

@Composable
private fun Rationale(text: String, onRequestPermission: () -> Unit) {
    AlertDialog(onDismissRequest = {}, title = {
        Text(text = text)
    }, confirmButton = {
        Button(onClick = onRequestPermission) { Text(text = "OK") }
    })
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun PermissionRequired(
    permissionState: PermissionState,
    permissionNotGrantedContent: @Composable (() -> Unit),
    permissionNotAvailableContent: @Composable (() -> Unit),
    content: @Composable (() -> Unit),
) {
    when {
        permissionState.status.isGranted -> {
            content()
        }
        permissionState.status.shouldShowRationale -> {
            permissionNotGrantedContent()
        }
        else -> {
            permissionNotAvailableContent()
        }
    }
}
