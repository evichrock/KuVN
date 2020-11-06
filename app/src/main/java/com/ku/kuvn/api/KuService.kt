package com.ku.kuvn.api

import android.app.Application
import android.content.res.AssetManager
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.internal.bind.DateTypeAdapter
import com.google.gson.reflect.TypeToken
import com.ku.kuvn.BuildConfig
import com.ku.kuvn.KuApplication
import com.ku.kuvn.api.league.League
import com.ku.kuvn.api.league.LeagueResponse
import io.reactivex.rxjava3.core.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*
import java.util.concurrent.TimeUnit

object KuService {

//    private const val API_URL = "https://quanlyku.us"
    lateinit var kuApi: KuApi
        private set

//    init {
//        val loggingInterceptor = HttpLoggingInterceptor().apply {
//            level = if (BuildConfig.DEBUG) {
//                HttpLoggingInterceptor.Level.BODY
//            } else {
//                HttpLoggingInterceptor.Level.NONE
//            }
//        }
//        val builder = OkHttpClient.Builder()
//            .readTimeout(60, TimeUnit.SECONDS)
//            .connectTimeout(60, TimeUnit.SECONDS)
//            .addInterceptor(loggingInterceptor)
//
//        kuApi = Retrofit.Builder().client(builder.build())
//            .addConverterFactory(GsonConverterFactory.create(createGsonObject()))
//            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
//            .baseUrl(API_URL)
//            .build()
//            .create(KuApi::class.java)
//    }

    fun init(app: Application) {
        kuApi = LocalKuApiImpl(app.assets)
    }

    fun AssetManager.readAssetsFile(fileName : String): String = open(fileName).bufferedReader().use{it.readText()}

//    private fun createGsonObject(): Gson {
//        return GsonBuilder()
//            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
//            .registerTypeAdapter(Date::class.java, DateTypeAdapter())
//            .create()
//    }

    class LocalKuApiImpl(private val assets: AssetManager) : KuApi {

        override fun getMenus(page: Int): Single<BaseResponse<MenuResponse>> {
            return Single.fromCallable { assets.readAssetsFile("menus.json") }
                .map<BaseResponse<MenuResponse>> {
                    Gson().fromJson(it, object: TypeToken<BaseResponse<MenuResponse>>(){}.type)
                }
        }

        override fun getBanners(): Single<BaseResponse<List<Banner>>> {
            return Single.fromCallable { assets.readAssetsFile("banners.json") }
                .map<BaseResponse<List<Banner>>> {
                    Gson().fromJson(it, object: TypeToken<BaseResponse<List<Banner>>>(){}.type)
                }
        }

        override fun getGifts(page: Int): Single<BaseResponse<BlogResponse>> {
            return Single.fromCallable { assets.readAssetsFile("blogs.json") }
                .map<BaseResponse<BlogResponse>> {
                    Gson().fromJson(it, object: TypeToken<BaseResponse<BlogResponse>>(){}.type)
                }
        }

        override fun getBlogsByCategory(id: String, page: Int): Single<BaseResponse<BlogResponse>> {
            return Single.fromCallable { assets.readAssetsFile("${id}.json") }
                .map<BaseResponse<BlogResponse>> {
                    Gson().fromJson(it, object: TypeToken<BaseResponse<BlogResponse>>(){}.type)
                }
        }

        override fun getGift(id: String): Single<BaseResponse<Blog>> {
            return Single.fromCallable { assets.readAssetsFile("${id}.json") }
                .map<BaseResponse<Blog>> {
                    Gson().fromJson(it, object: TypeToken<BaseResponse<Blog>>(){}.type)
                }
        }

        override fun getGames(): Single<GameResponse> {
            return Single.fromCallable { assets.readAssetsFile("games.json") }
                .map<GameResponse> {
                    Gson().fromJson(it, object: TypeToken<GameResponse>(){}.type)
                }
        }

        override fun getLeagues(): Single<LeagueResponse> {
            return Single.fromCallable { assets.readAssetsFile("leagues.json") }
                .map<LeagueResponse> {
                    Gson().fromJson(it, object: TypeToken<LeagueResponse>(){}.type)
                }
        }

        override fun getLeague(id: String): Single<BaseResponse<League>> {
            return Single.fromCallable { assets.readAssetsFile("${id}.json") }
                .map<BaseResponse<League>> {
                    Gson().fromJson(it, object : TypeToken<BaseResponse<League>>() {}.type)
                }
        }

        override fun getMenuSetting(): Single<BaseResponse<Menu>> {
            return Single.fromCallable { assets.readAssetsFile("menu_register.json") }
                .map<BaseResponse<Menu>> {
                    Gson().fromJson(it, object: TypeToken<BaseResponse<Menu>>(){}.type)
                }
        }

        override fun getSettings(): Single<BaseResponse<Setting>> {
            return Single.fromCallable { assets.readAssetsFile("settings.json") }
                .map<BaseResponse<Setting>> {
                    Gson().fromJson(it, object: TypeToken<BaseResponse<Setting>>(){}.type)
                }
        }

    }

    interface KuApi {

        @GET("api/menus")
        fun getMenus(@Query("page") page: Int = 1): Single<BaseResponse<MenuResponse>>

        @GET("api/banners")
        fun getBanners(): Single<BaseResponse<List<Banner>>>

        @GET("api/blogs")
        fun getGifts(@Query("page") page: Int = 1): Single<BaseResponse<BlogResponse>>

        @GET("api/blog_categories/{id}/blogs")
        fun getBlogsByCategory(@Path("id") id: String, @Query("page") page: Int = 1): Single<BaseResponse<BlogResponse>>

        @GET("api/blogs/{id}")
        fun getGift(@Path("id") id: String): Single<BaseResponse<Blog>>

        @GET("api/games")
        fun getGames(): Single<GameResponse>

        @GET("api/leagues")
        fun getLeagues(): Single<LeagueResponse>

        @GET("api/leagues/{id}")
        fun getLeague(@Path("id") id: String): Single<BaseResponse<League>>

        @GET("api/menus/register")
        fun getMenuSetting(): Single<BaseResponse<Menu>>

        @GET("api/settings/mobile")
        fun getSettings(): Single<BaseResponse<Setting>>
    }
}