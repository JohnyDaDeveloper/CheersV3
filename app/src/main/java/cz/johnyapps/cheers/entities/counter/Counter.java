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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Counter counter = (Counter) o;

        if (id != counter.id) return false;
        if (Float.compare(counter.volume, volume) != 0) return false;
        if (beverageId != counter.beverageId) return false;
        if (active != counter.active) return false;
        if (!counterEntries.equals(counter.counterEntries)) return false;
        if (!startTime.equals(counter.startTime)) return false;
        return (endTime == null ? 0 : endTime.getTime()) == (counter.endTime == null ? 0 : counter.endTime.getTime());
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + counterEntries.hashCode();
        result = 31 * result + (volume != +0.0f ? Float.floatToIntBits(volume) : 0);
        result = 31 * result + (int) (beverageId ^ (beverageId >>> 32));
        result = 31 * result + (active ? 1 : 0);
        result = 31 * result + startTime.hashCode();
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        return result;
    }
}
