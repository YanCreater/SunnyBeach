package cn.cqautotest.sunnybeach.ui.activity

import android.view.View
import androidx.activity.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import cn.cqautotest.sunnybeach.R
import cn.cqautotest.sunnybeach.app.AppActivity
import cn.cqautotest.sunnybeach.databinding.MessageCenterActivityBinding
import cn.cqautotest.sunnybeach.ui.activity.msg.*
import cn.cqautotest.sunnybeach.util.setFixOnClickListener
import cn.cqautotest.sunnybeach.util.simpleToast
import cn.cqautotest.sunnybeach.util.startActivity
import cn.cqautotest.sunnybeach.util.takeIfLogin
import cn.cqautotest.sunnybeach.viewmodel.MsgViewModel

/**
 * author : A Lonely Cat
 * github : https://github.com/anjiemo/SunnyBeach
 * time   : 2021/10/24
 * desc   : 消息中心界面
 */
class MessageCenterActivity : AppActivity() {

    private val mBinding by viewBinding<MessageCenterActivityBinding>()
    private val mMsgViewModel by viewModels<MsgViewModel>()

    override fun getLayoutId(): Int = R.layout.message_center_activity

    override fun initView() {
        mBinding.articleContainer.setFixOnClickListener {
            takeIfLogin {
                // 文章评论消息列表
                startActivity<ArticleMsgListActivity>()
            }
        }
        mBinding.likeContainer.setFixOnClickListener {
            takeIfLogin {
                // 点赞消息列表
                startActivity<LikeMsgListActivity>()
            }
        }
        mBinding.fishContainer.setFixOnClickListener {
            takeIfLogin {
                // 摸鱼评论消息列表
                startActivity<FishMsgListActivity>()
            }
        }
        mBinding.atMeContainer.setFixOnClickListener {
            takeIfLogin {
                // @我消息列表
                startActivity<AtMeMsgListActivity>()
            }
        }
        mBinding.qaContainer.setFixOnClickListener {
            takeIfLogin {
                // 问答消息列表
                startActivity<QaMsgListActivity>()
            }
        }
        mBinding.systemContainer.setFixOnClickListener {
            takeIfLogin {
                // 系统消息列表
                startActivity<SystemMsgListActivity>()
            }
        }
    }

    override fun initData() {

    }

    override fun onRightClick(view: View?) {
        takeIfLogin {
            // 设置消息全部已读
            mMsgViewModel.readAllMsg().observe(this) {
                val tips = it.getOrNull() ?: return@observe
                simpleToast(tips)
            }
        }
    }
}