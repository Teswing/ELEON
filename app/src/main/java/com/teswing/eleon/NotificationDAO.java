package com.teswing.eleon;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NotificationDAO {

    @Insert
    void insert(Notification notification);

    @Update
    void update(Notification notification);

    @Delete
    void delete(Notification notification);

    // Следуя уроку
    @Query("DELETE FROM notification_table")
    void deleteAll();

    @Query("SELECT * FROM notification_table ORDER BY id ASC")
    LiveData<List<Notification>> getAll();

}
