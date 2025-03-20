package com.most4devmyreciepes.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.most4devmyreciepes.data.local.converter.Converters
import com.most4devmyreciepes.data.local.dao.RecipeDao
import com.most4devmyreciepes.data.local.dbmodels.RecipeDbModel

@Database(
    entities = [RecipeDbModel::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class RecipeDatabase : RoomDatabase() {
    abstract val recipeDao: RecipeDao
} 