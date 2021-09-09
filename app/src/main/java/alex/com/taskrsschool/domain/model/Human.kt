package alex.com.taskrsschool.domain.model

import alex.com.taskrsschool.data.TABLE_NAME
import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = TABLE_NAME)
data class Human(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "age") val age: Int?,
    @ColumnInfo(name = "profession") val profession: String?,
    @ColumnInfo(name = "color") val color: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeValue(age)
        parcel.writeString(profession)
        parcel.writeInt(color)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Human> {
        override fun createFromParcel(parcel: Parcel): Human {
            return Human(parcel)
        }

        override fun newArray(size: Int): Array<Human?> {
            return arrayOfNulls(size)
        }
    }
}


