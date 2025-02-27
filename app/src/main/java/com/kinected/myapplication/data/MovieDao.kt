package com.kinected.myapplication.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao{

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insertMovies(resultsItem: ResultsItem)

	@Query("SELECT id FROM movie_table WHERE bookmark = 1")
	fun getBookmarkedList(): Flow<List<Int>>

	@Query("UPDATE movie_table SET bookmark = NOT bookmark WHERE id = :id")
	suspend fun doBookmark(id: Int)

	@Query("DELETE FROM movie_table")
	suspend fun deleteAllList()

	@Query("SELECT * FROM movie_table ORDER BY id ASC")
	suspend fun getAllFavMovies(): List<ResultsItem>
}


