package cz.johnyapps.cheers.entities;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import cz.johnyapps.cheers.ItemWithId;

@Entity(tableName = "tag_table")
public class Tag implements ItemWithId {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    private String name;

    public Tag(long id, @NonNull String name) {
        this.id = id;
        this.name = name;
    }

    public Tag(@NonNull String name) {
        this.name = name;
    }

    @Override
    public long getId() {
        return id;
    }

    @NonNull
    @Override
    public String getText(@NonNull Context context) {
        return getName();
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }
}
