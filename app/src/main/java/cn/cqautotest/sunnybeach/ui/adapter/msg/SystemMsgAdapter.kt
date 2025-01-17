package cn.cqautotest.sunnybeach.ui.adapter.msg

import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import cn.cqautotest.sunnybeach.R
import cn.cqautotest.sunnybeach.databinding.SystemMsgListItemBinding
import cn.cqautotest.sunnybeach.ktx.asViewBinding
import cn.cqautotest.sunnybeach.ktx.itemDiffCallback
import cn.cqautotest.sunnybeach.ktx.setFixOnClickListener
import cn.cqautotest.sunnybeach.model.msg.SystemMsg
import cn.cqautotest.sunnybeach.ui.adapter.delegate.AdapterDelegate
import com.blankj.utilcode.util.TimeUtils
import com.bumptech.glide.Glide

/**
 * author : A Lonely Cat
 * github : https://github.com/anjiemo/SunnyBeach
 * time   : 2021/10/25
 * desc   : 系统列表消息适配器
 */
class SystemMsgAdapter(private val adapterDelegate: AdapterDelegate) :
    PagingDataAdapter<SystemMsg.Content, SystemMsgAdapter.SystemMsgViewHolder>(diffCallback) {

    inner class SystemMsgViewHolder(val binding: SystemMsgListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        constructor(parent: ViewGroup) : this(parent.asViewBinding<SystemMsgListItemBinding>())

        fun onBinding(item: SystemMsg.Content?, position: Int) {
            item ?: return
            with(binding) {
                Glide.with(root)
                    .load(R.mipmap.ic_gold)
                    .placeholder(R.mipmap.ic_gold)
                    .error(R.mipmap.ic_gold)
                    .circleCrop()
                    .into(ivAvatar)
                cbNickName.text = item.title
                val sdf = TimeUtils.getSafeDateFormat("yyyy-MM-dd HH:mm")
                tvDesc.text = TimeUtils.getFriendlyTimeSpanByNow(item.publishTime, sdf)
                tvReplyMsg.height = 0
                tvChildReplyMsg.text = HtmlCompat.fromHtml(item.content, HtmlCompat.FROM_HTML_MODE_LEGACY)
            }
        }
    }

    override fun onViewAttachedToWindow(holder: SystemMsgViewHolder) {
        super.onViewAttachedToWindow(holder)
        adapterDelegate.onViewAttachedToWindow(holder)
    }

    override fun onBindViewHolder(holder: SystemMsgViewHolder, position: Int) {
        holder.itemView.setFixOnClickListener { adapterDelegate.onItemClick(it, position) }
        holder.onBinding(getItem(position), position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SystemMsgViewHolder = SystemMsgViewHolder(parent)

    companion object {

        private val diffCallback =
            itemDiffCallback<SystemMsg.Content>({ oldItem, newItem -> oldItem.id == newItem.id }) { oldItem, newItem -> oldItem == newItem }
    }
}