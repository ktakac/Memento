package hr.foi.rampu.memento.ws

import com.google.gson.annotations.SerializedName

data class NewsItem(
    var title: String?,
    var text: String?,
    var date: String?,
    @SerializedName("image_path") var imagePath: String?
)
