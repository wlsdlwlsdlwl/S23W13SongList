package kr.ac.kumoh.ce.s20200663.s23w13songlist

import retrofit2.http.GET

interface SongApi {
    @GET("song")
    suspend fun getSongs(): List<Song>
}