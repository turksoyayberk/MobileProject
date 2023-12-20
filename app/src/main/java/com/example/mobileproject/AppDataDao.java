package com.example.mobileproject;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface AppDataDao {
    @Query("SELECT * FROM AppData WHERE id = :id")
    AppData getAppDataById(int id);
    @Query("SELECT * FROM AppData")
    List<AppData> getAllMarkers();
    @Insert
    long insertMarker(AppData marker);
    @Update
    void updateMarker(AppData marker);
    @Delete
    void deleteMarker(AppData marker);
}
