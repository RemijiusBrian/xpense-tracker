package com.xpenses.android.feature_expenses.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.xpenses.android.core.data.local.db.XTDatabase
import com.xpenses.android.feature_expenses.data.local.entity.ExpenseTagEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class ExpenseTagDaoTest {

    private lateinit var database: XTDatabase
    private lateinit var tagDao: ExpenseTagDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            XTDatabase::class.java
        ).allowMainThreadQueries()
            .build()
        tagDao = database.expenseTagDao
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertTag() = runTest {
        val tag = ExpenseTagEntity("Test Tag")
        tagDao.insert(tag)
        val tagsList = tagDao.getAllTags().first()
        assertThat(tagsList).contains(tag.name)
    }

    @Test
    fun deleteTag() = runTest {
        val tag = ExpenseTagEntity("Test Tag")
        tagDao.insert(tag)
        val tagsList = tagDao.getAllTags().first()
        assertThat(tagsList).contains(tag.name)
        tagDao.deleteTag(tag.name)
        val listAfterDeletion = tagDao.getAllTags().first()
        assertThat(listAfterDeletion).doesNotContain(tag.name)
    }
}