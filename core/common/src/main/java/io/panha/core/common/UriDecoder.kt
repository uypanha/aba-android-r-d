package io.panha.core.common

import android.net.Uri


class UriDecoder : StringDecoder {

    override fun decodeString(encodedString: String): String = Uri.decode(encodedString)
}
