package com.example.mobileproject;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Photo {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int appDataId; // AppData tablosundaki ilgili gezi notunun ID'si

    public String imagePath; // Resmin dosya yolu
}