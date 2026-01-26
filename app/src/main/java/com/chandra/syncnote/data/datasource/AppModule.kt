package com.chandra.syncnote.data.datasource


import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.chandra.syncnote.data.repository.NoteRepositoryImpl
import com.chandra.syncnote.domain.repository.NoteRepository
import com.chandra.syncnote.domain.usecases.AddNoteUseCase
import com.chandra.syncnote.domain.usecases.DeleteAllNoteUseCase
import com.chandra.syncnote.domain.usecases.DeleteNoteUseCase
import com.chandra.syncnote.domain.usecases.EditNoteUseCase
import com.chandra.syncnote.domain.usecases.GetNoteByIdUseCase
import com.chandra.syncnote.domain.usecases.GetNotesUseCase
import com.chandra.syncnote.domain.usecases.NoteUseCase
import com.chandra.syncnote.mainscreen.EditNoteScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(app: Application): SyncNoteDatabase =
        Room.databaseBuilder(
            app,
            SyncNoteDatabase::class.java,
            SyncNoteDatabase.DATA_BASE_NAME
        ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideNotesDao(db: SyncNoteDatabase): NotesDao = db.notesDao

    @Provides
    @Singleton
    fun provideNoteRepository(notesDao: NotesDao): NoteRepository =
        NoteRepositoryImpl(notesDao)

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCase =
        NoteUseCase(
            addNoteUseCase = AddNoteUseCase(repository),
            deleteNoteUseCase = DeleteNoteUseCase(repository),
            getNoteByIdUseCase = GetNoteByIdUseCase(repository),
            getNoteUseCase = GetNotesUseCase(repository),
            deleteAllNotesUseCase = DeleteAllNoteUseCase(repository),
            editNotesUseCase = EditNoteUseCase(repository)
        )
}

