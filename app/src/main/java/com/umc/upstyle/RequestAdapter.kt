import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.umc.upstyle.Post
import com.umc.upstyle.R
import com.umc.upstyle.Request

class RequestAdapter(
    private val requestList: List<Request>,
    private val onRequestClick: (Request) -> Unit
) : RecyclerView.Adapter<RequestAdapter.RequestViewHolder>() {

    inner class RequestViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        private val tvCount: TextView = view.findViewById(R.id.tv_count)

        fun bind(request: Request) {
            tvTitle.text = request.title
            tvCount.text = "${request.commentCount}명 투표"

            itemView.setOnClickListener {
                onRequestClick(request) // 클릭 이벤트 실행
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_request, parent, false)
        return RequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        holder.bind(requestList[position])
    }

    override fun getItemCount(): Int = requestList.size
}
