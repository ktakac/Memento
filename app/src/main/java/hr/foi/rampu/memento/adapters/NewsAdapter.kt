package hr.foi.rampu.memento.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.foi.rampu.memento.R
import hr.foi.rampu.memento.ws.NewsItem
import hr.foi.rampu.memento.ws.WsNews

class NewsAdapter(private val newsList: List<NewsItem>) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var title: TextView
        private var text: TextView
        private var dateTime: TextView
        private var imageView: ImageView

        init {
            title = view.findViewById(R.id.tv_news_list_item_title)
            text = view.findViewById(R.id.tv_news_list_item_text)
            dateTime = view.findViewById(R.id.tv_news_list_item_date)
            imageView = view.findViewById(R.id.iv_news_list_item_image)
        }

        fun bind(newsItem: NewsItem) {
            title.text = newsItem.title
            text.text = newsItem.text
            dateTime.text = newsItem.date
            Picasso.get().load(WsNews.BASE_URL + newsItem.imagePath).into(imageView)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_list_item, parent, false)

        return NewsViewHolder(view)
    }

    override fun getItemCount(): Int = newsList.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(newsList[position])
    }
}