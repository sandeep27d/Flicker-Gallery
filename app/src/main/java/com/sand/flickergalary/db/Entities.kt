package com.sand.flickergalary.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "photos")
@Parcelize
data class Photo(
    @PrimaryKey @SerializedName("id") val id: String,
    @SerializedName("owner") val owner: String? = null,
    @SerializedName("secret") val secret: String? = null,
    @SerializedName("server") val server: String? = null,
    @SerializedName("farm") val farm: Int? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("ispublic") val isPublic: Int? = null,
    @SerializedName("isfriend") val isFriend: Int? = null,
    @SerializedName("isfamily") val isFamily: Int? = null,
    var isFav: Int = 0
) : Parcelable {
    fun toFavPhoto() = FavPhoto(id, owner, secret, server, farm, title, isPublic, isFriend, isFamily, isFav)

    val uri: String
        get() {
            return "https://farm${farm}.staticflickr.com/${server}/${id}_${secret}_m.jpg"
        }
    val uri_medium: String
        get() {
            return "https://farm${farm}.staticflickr.com/${server}/${id}_${secret}_z.jpg"
        }
}

@Entity
data class RemoteKeys(@PrimaryKey val photoId: String, val curKey: Int?, val prevKey: Int?, val nextKey: Int?)

@Entity(tableName = "fav_photos")
@Parcelize
data class FavPhoto(
    @PrimaryKey @SerializedName("id") val id: String,
    @SerializedName("owner") val owner: String? = null,
    @SerializedName("secret") val secret: String? = null,
    @SerializedName("server") val server: String? = null,
    @SerializedName("farm") val farm: Int? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("ispublic") val isPublic: Int? = null,
    @SerializedName("isfriend") val isFriend: Int? = null,
    @SerializedName("isfamily") val isFamily: Int? = null,
    var isFav: Int = 0
) : Parcelable {
    val uri: String
        get() {
            return "https://farm${farm}.staticflickr.com/${server}/${id}_${secret}_m.jpg"
        }
    val uri_medium: String
        get() {
            return "https://farm${farm}.staticflickr.com/${server}/${id}_${secret}_z.jpg"
        }

    fun toPhoto() = Photo(id,owner, secret, server, farm, title, isPublic, isFriend, isFamily, isFav)
}
