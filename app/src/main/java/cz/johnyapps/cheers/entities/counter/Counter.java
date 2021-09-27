package cz.johnyapps.cheers.entities.counter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.johnyapps.cheers.tools.TimeUtils;

@Entity(tableName = "counter_table")
public class Counter {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    private List<CounterEntry> counterEntries = new ArrayList<>();
    private float volume;
    private long beverageId;
    private boolean active = true;
    @NonNull
    private Date startTime;
    @Nullable
    private Date endTime;

    public Counter(long beverageId, float volume) {
        this.beverageId = beverageId;
        this.volume = volume;
        startTime = TimeUtils.getDate();
        endTime = null;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBeverageId() {
        return beverageId;
    }

    public void setBeverageId(long beverageId) {
        this.beverageId = beverageId;
    }

    @NonNull
    public List<CounterEntry> getCounterEntries() {
        return counterEntries;
    }

    public void setCounterEntries(@NonNull List<CounterEntry> counterEntries) {
        this.counterEntries = counterEntries;
    }

    public int getCount() {
        return counterEntries.size();
    }

    public void addCounterEntry() {
        this.counterEntries.add(new CounterEntry());
    }

    public void removeLastCounterEntry() {
        if (!counterEntries.isEmpty()) {
            counterEntries.remove(counterEntries.size() - 1);
        }
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @NonNull
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(@NonNull Date startTime) {
        this.startTime = startTime;
    }

    @Nullable
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(@Nullable Date endTime) {
        this.endTime = endTime;
    }
}
