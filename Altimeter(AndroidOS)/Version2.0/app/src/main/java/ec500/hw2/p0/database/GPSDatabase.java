package ec500.hw2.p0.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ec500.hw2.p0.model.Loc;

@Database(entities = {Loc.class}, version = 2, exportSchema = false)
public abstract class GPSDatabase extends RoomDatabase {

    public abstract LocDao locDao();
}
