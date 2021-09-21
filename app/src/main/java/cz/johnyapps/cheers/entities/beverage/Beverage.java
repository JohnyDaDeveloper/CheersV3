package cz.johnyapps.cheers.entities.beverage;

import android.content.Context;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import cz.johnyapps.cheers.ItemWithId;
import cz.johnyapps.cheers.tools.TextUtils;

@Entity(tableName = "beverage_table")
public class Beverage implements ItemWithId {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    private String name;
    @ColorInt
    private int color;
    @ColorInt
    private int textColor;
    private float alcohol;

    public Beverage(@NonNull String name,
                    @ColorInt int color,
                    @ColorInt int textColor,
                    float alcohol) {
        this.name = name;
        this.color = color;
        this.textColor = textColor;
        this.alcohol = alcohol;
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
}
