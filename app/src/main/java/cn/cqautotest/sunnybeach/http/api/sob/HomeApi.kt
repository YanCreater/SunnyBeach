package cn.cqautotest.sunnybeach.http.api.sob

import cn.cqautotest.sunnybeach.model.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HomeApi : ISobApi {

    /**
     * 根据文章id获取文章详情内容
     */
    @GET("ct/article/detail/{articleId}")
    suspend fun getArticleDetailById(@Path("articleId") articleId: String): ApiResponse<ArticleDetail>

    /**
     * 根据分类id获取内容
     */
    @GET("ct/content/home/recommend/{categoryId}/{page}")
    suspend fun getArticleListByCategoryId(
        @Path("categoryId") categoryId: String,
        @Path("page") page: Int
    ): ApiResponse<ArticleInfo>

    /**
     * 获取推荐内容
     */
    @GET("ct/content/home/recommend/{page}")
    suspend fun getRecommendContent(@Path("page") page: Int): ApiResponse<ArticleInfo>

    /**
     * 获取分类列表
     */
    @GET("ct/category/list")
    suspend fun getCategories(): ApiResponse<HomeCategories>

    /**
     * 获取问答列表
     * page：页码，从 1 开始
     * state：状态，lastest：最新的，noanswer：等待解决的，hot：热门的
     * category：-2（固定参数）
     */
    @GET("ct/wenda/list")
    suspend fun getQaList(
        @Query("page") page: Int,
        @Query("state") state: String,
        @Query("category") category: Int = -2
    ): ApiResponse<QaInfo>
}