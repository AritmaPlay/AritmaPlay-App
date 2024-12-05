package com.aritmaplay.app.util

object AvatarGenerator {
    private const val BASE_URL = "https://api.dicebear.com/9.x/thumbs/png"
    private const val SCALE = 75
    private val flipOption = listOf(true, false)
    private val faceRotationOption = listOf("-20", "-15", "-10", "-5", "0", "5", "10", "15", "20")
    private val faceOffsetXOption = listOf("-5", "0", "5")
    private val faceOffsetYOption = listOf("-5", "0", "5")
    private val shapeRotationOption = listOf("-20", "-15", "-10", "-5", "0", "5", "10", "15", "20")
    private val shapeOffsetXOption = listOf("-5", "0", "5")
    private val shapeOffsetYOption = listOf("-5", "0", "5")
    private val backgroundColorOption = listOf("FCFF9B", "5A8EFF", "484E5E", "E97474", "32FFAD", "DAA3ED")
    private val shapeColorOption = listOf("FCFF9B", "5A8EFF", "484E5E", "E97474", "32FFAD", "DAA3ED")
    private val eyesOption = listOf("variant1W10", "variant2W10", "variant3W10", "variant4W10", "variant5W10", "variant6W10", "variant7W10", "variant8W10", "variant9W10")
    private val faceOption = listOf("variant1", "variant2", "variant3", "variant4", "variant5")
    private val mouthOption = listOf("variant1", "variant2", "variant3", "variant4", "variant5")

    fun generateAvatarUrl(): String {
        val url = buildString {
            append(BASE_URL)
            append("?scale=")
            append(SCALE)
            append("&flip=")
            append(flipOption.random())
            append("&faceRotation=")
            append(faceRotationOption.random())
            append("&faceOffsetX=")
            append(faceOffsetXOption.random())
            append("&faceOffsetY=")
            append(faceOffsetYOption.random())
            append("&shapeRotation=")
            append(shapeRotationOption.random())
            append("&shapeOffsetX=")
            append(shapeOffsetXOption.random())
            append("&shapeOffsetY=")
            append(shapeOffsetYOption.random())
            append("&backgroundColor=")
            append(backgroundColorOption.random())
            append("&shapeColor=")
            append(shapeColorOption.random())
            append("&eyes=")
            append(eyesOption.random())
            append("&face=")
            append(faceOption.random())
            append("&mouth=")
            append(mouthOption.random())
        }

        return url
    }
}