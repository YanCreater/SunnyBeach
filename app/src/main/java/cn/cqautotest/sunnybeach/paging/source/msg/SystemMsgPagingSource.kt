package cn.cqautotest.sunnybeach.paging.source.msg

import androidx.paging.PagingSource
import androidx.paging.PagingState
import cn.cqautotest.sunnybeach.execption.ServiceException
import cn.cqautotest.sunnybeach.http.ServiceCreator
import cn.cqautotest.sunnybeach.http.api.sob.MsgApi
import cn.cqautotest.sunnybeach.model.msg.SystemMsg
import timber.log.Timber

/**
 * author : A Lonely Cat
 * github : https://github.com/anjiemo/SunnyBeach
 * time   : 2021/10/25
 * desc   : 系统列表消息 PagingSource
 */
class SystemMsgPagingSource : PagingSource<Int, SystemMsg.Content>() {

    private val msgApi = ServiceCreator.create<MsgApi>()

    override fun getRefreshKey(state: PagingState<Int, SystemMsg.Content>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SystemMsg.Content> {
        return try {
            val page = params.key ?: FIRST_PAGE_INDEX
            Timber.d("load：===> page is $page")
            val response = msgApi.getSystemMsgList(page)
            val responseData = response.getData()
            val prevKey = if (responseData.first) null else page - 1
            val nextKey = if (responseData.last) null else page + 1
            if (response.isSuccess()) LoadResult.Page(
                data = responseData.content,
                prevKey = prevKey,
                nextKey = nextKey
            )
            else LoadResult.Error(ServiceException())
        } catch (t: Throwable) {
            t.printStackTrace()
            LoadResult.Error(t)
        }
    }

    companion object {
        private const val FIRST_PAGE_INDEX = 1
    }
}