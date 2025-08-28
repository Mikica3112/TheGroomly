package com.example.thegroomly

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.thegroomly.data.local.dao.GroomerDao
import com.example.thegroomly.data.local.entities.GroomerEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class GroomerDaoMediumTest {

    @Database(entities = [GroomerEntity::class], version = 1, exportSchema = false)
    abstract class TestDb : RoomDatabase() {
        abstract fun groomerDao(): GroomerDao
    }

    private lateinit var db: TestDb
    private lateinit var dao: GroomerDao

    @Before
    fun setup() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, TestDb::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.groomerDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insert_and_read_favorites() = runBlocking {
        dao.insertAll(
            listOf(
                GroomerEntity(id = 0, name = "Čupko", distanceMeters = 1500, photoRes = 0, isFavorite = true),
                GroomerEntity(id = 0, name = "Nera", distanceMeters = 800, photoRes = 0, isFavorite = true),
                GroomerEntity(id = 0, name = "Maza", distanceMeters = 1200, photoRes = 0, isFavorite = false)
            )
        )
        val favorites = dao.getFavorites().first()
        assertThat(favorites.size, `is`(2))
        assertThat(favorites.map { it.name }, containsInAnyOrder("Čupko", "Nera"))
    }

    @Test
    fun update_favorite_flag() = runBlocking {
        dao.insertAll(
            listOf(
                GroomerEntity(id = 0, name = "Bobi", distanceMeters = 900, photoRes = 0, isFavorite = false)
            )
        )
        val inserted = dao.getAll().first().first { it.name == "Bobi" }
        dao.setFavorite(inserted.id, true)
        val favorites = dao.getFavorites().first()
        assertThat(favorites.any { it.name == "Bobi" }, `is`(true))
    }

    @Test
    fun getAll_sortedByDistance() = runBlocking {
        dao.insertAll(
            listOf(
                GroomerEntity(id = 0, name = "Čupko", distanceMeters = 1500, photoRes = 0, isFavorite = false),
                GroomerEntity(id = 0, name = "Nera", distanceMeters = 800, photoRes = 0, isFavorite = false),
                GroomerEntity(id = 0, name = "Maza", distanceMeters = 1200, photoRes = 0, isFavorite = false)
            )
        )
        val all = dao.getAll().first()
        assertThat(all.map { it.name }, `is`(listOf("Nera", "Maza", "Čupko")))
    }

    @Test
    fun insert_replace_on_conflict_by_id() = runBlocking {
        dao.insertAll(
            listOf(
                GroomerEntity(id = 0, name = "Rex", distanceMeters = 1000, photoRes = 0, isFavorite = false)
            )
        )
        val original = dao.getAll().first().first { it.name == "Rex" }
        dao.insertAll(
            listOf(
                GroomerEntity(id = original.id, name = "Rexić", distanceMeters = 700, photoRes = 1, isFavorite = true)
            )
        )
        val updated = dao.getAll().first().first { it.id == original.id }
        assertThat(updated.name, `is`("Rexić"))
        assertThat(updated.distanceMeters, `is`(700))
        assertThat(updated.isFavorite, `is`(true))
    }
}
