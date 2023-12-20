package com.example.mobileproject;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PhotoDao {
    @Query("SELECT * FROM Photo WHERE appDataId = :appDataId")
    List<Photo> getPhotosForAppData(int appDataId);

    @Insert
    void insertPhoto(Photo photo);

    // Diğer gerekli metodlar...
}