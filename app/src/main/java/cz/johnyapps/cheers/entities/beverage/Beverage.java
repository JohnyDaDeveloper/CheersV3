package cz.johnyapps.cheers.entities.beverage;

import android.content.Context;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

import cz.johnyapps.cheers.entities.ItemWithId;
import cz.johnyapps.cheers.entities.Tag;
import cz.johnyapps.cheers.tools.TextUtils;

@Entity(tableName = "beverage_table")
public class Beverage implements ItemWithId, Comparable<Beverage> {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    private String name;
    @ColorInt
    private int color;
    @ColorInt
    private int textColor;
    private float alcohol;
    @NonNull
    private List<Tag> tags = new ArrayList<>();
    private boolean deleted = false;

    public Beverage(@NonNull String name,
                    @ColorInt int color,
                    @ColorInt int textColor,
                    float alcohol) {
        this.name = name;
        this.color = color;
        this.textColor = textColor;
        this.alcohol = alcohol;
    }

    @Override
    public int compareTo(Beverage o) {
        if (o == null) {
            return 1;
        }

        return getName().compareTo(o.getName());
    }

    @NonNull
    @Override
    public String getText(@NonNull Context context) {
        if (alcohol > 0) {
            return String.format("%s (%s%s)",
                    getName(),
                    TextUtils.decimalToStringWithTwoDecimalDigits(getAlcohol()),
                    "%");
        }

        return getName();
    }

    @Override
    public long getId() {
        return id;
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

    @ColorInt
    public int getColor() {
        return color;
    }

    public void setColor(@ColorInt int color) {
        this.color = color;
    }

    @ColorInt
    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(@ColorInt int textColor) {
        this.textColor = textColor;
    }

    public float getAlcohol() {
        return alcohol;
    }

    public void setAlcohol(float alcohol) {
        this.alcohol = alcohol;
    }

    @NonNull
    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(@NonNull List<Tag> tags) {
        this.tags = tags;
    }

    public void addTag(@NonNull Tag tag) {
        this.tags.add(tag);
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Beverage beverage = (Beverage) o;

        if (id != beverage.id) return false;
        if (color != beverage.color) return false;
        if (textColor != beverage.textColor) return false;
        if (Float.compare(beverage.alcohol, alcohol) != 0) return false;
        if (deleted != beverage.deleted) return false;
        return name.equals(beverage.name);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + name.hashCode();
        result = 31 * result + color;
        result = 31 * result + textColor;
        result = 31 * result + (alcohol != +0.0f ? Float.floatToIntBits(alcohol) : 0);
        result = 31 * result + (deleted ? 1 : 0);
        return result;
    }
}
