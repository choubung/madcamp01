package com.example.myapp;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;
import androidx.room.Delete;
import java.util.List;

@Dao
public interface ContactDao {
    @Query("DELETE FROM contact WHERE idx = :idx")
    void delete(int idx);

    @Query("SELECT * FROM contact")
    List<ContactEntity> getAllContact();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(ContactEntity contact);

    @Update
    void update(ContactEntity contact);
}
