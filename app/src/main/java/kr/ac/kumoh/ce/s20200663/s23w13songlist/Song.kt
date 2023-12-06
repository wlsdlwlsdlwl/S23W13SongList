package kr.ac.kumoh.ce.s20200663.s23w13songlist

data class Song(
    val id: Int,
    val title: String,
    val singer: String,
    val rating: Int,
    val lyrics: String?
)
