package com.aritmaplay.app.util

object AvatarGenerator {
    const val BASE_URL = "https://api.dicebear.com/9.x/thumbs/svg"
    const val FLIP = true
    const val SCALE = 75
    val backgroundColorOption = listOf("0a5b83", "1c799f", "69d2e7", "f1f4dc", "d1d4f9", "b6e3f4", "c0aede", "ffd5dc")
    val shapeColorOption = listOf("0a5b83", "1c799f", "69d2e7", "f1f4dc", "d1d4f9", "b6e3f4", "c0aede", "ffd5dc")
    val eyesOption = listOf("variant1W10", "variant1W12", "variant1W14", "variant1W16", "variant2W10", "variant2W12", "variant2W14", "variant2W16", "variant3W10", "variant3W12", "variant3W14", "variant3W16", "variant4W10", "variant4W12", "variant4W14", "variant4W16", "variant5W10", "variant5W12", "variant5W14", "variant5W16", "variant6W10", "variant6W12", "variant6W14", "variant6W16", "variant7W10", "variant7W12", "variant7W14", "variant7W16", "variant8W10", "variant8W12", "variant8W14", "variant8W16", "variant9W10", "variant9W12", "variant9W14", "variant9W16")
    val faceOption = listOf("variant1", "variant2", "variant3", "variant4", "variant5")
    val mouthOption = listOf("variant1", "variant2", "variant3", "variant4", "variant5")

    fun generateAvatarUrl(): String {
        val url = "$BASE_URL?flip=$FLIP&scale=$SCALE&backgroundColor=${backgroundColorOption.random()}&shapeColor=${shapeColorOption.random()}&eyes=${eyesOption.random()}&face=${faceOption.random()}&mouth=${mouthOption.random()}"
        return url;
    }
}