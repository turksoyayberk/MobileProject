package com.example.mobileproject;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class AppData {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String note;//Gezi için yazdığımız notlar
    public String title;//Gezinin başlığı

    /////Map bilgileri
    public double latitude;
    public double longitude;
    // Diğer veri türlerini de buraya ekleyebilirsiniz
    // Örneğin: String name, boolean isActive, vb.
}