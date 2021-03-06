package com.xyoye.dandanplay.utils.net;

import com.xyoye.dandanplay.bean.BangumiBean;
import com.xyoye.dandanplay.bean.AnimeDetailBean;
import com.xyoye.dandanplay.bean.AnimeFavoriteBean;
import com.xyoye.dandanplay.bean.AnimeTypeBean;
import com.xyoye.dandanplay.bean.BannerBeans;
import com.xyoye.dandanplay.bean.DanmuDownloadBean;
import com.xyoye.dandanplay.bean.DanmuMatchBean;
import com.xyoye.dandanplay.bean.DanmuSearchBean;
import com.xyoye.dandanplay.bean.MagnetBean;
import com.xyoye.dandanplay.bean.PersonalBean;
import com.xyoye.dandanplay.bean.PlayHistoryBean;
import com.xyoye.dandanplay.bean.RegisterBean;
import com.xyoye.dandanplay.bean.SeasonAnimeBean;
import com.xyoye.dandanplay.bean.SubGroupBean;
import com.xyoye.dandanplay.bean.UploadDanmuBean;
import com.xyoye.dandanplay.bean.params.HistoryParam;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

/**
 * Created by YE on 2018/7/9.
 */


public interface RetrofitService {

    @FormUrlEncoded
    @POST("api/v2/match")
    Observable<DanmuMatchBean> matchDanmu(@FieldMap Map<String, String> params);

    @GET("/api/v2/search/episodes")
    Observable<DanmuSearchBean> searchDanmu(@Query("anime") String anime, @Query("episode") String episode);

    @FormUrlEncoded
    @POST("api/v2/comment/{episodeId}")
    Observable<UploadDanmuBean> uploadDanmu(@FieldMap Map<String, String> params, @Path("episodeId") String episodeId);

    @GET("api/v2/comment/{episodeId}?withRelated=true")
    Observable<DanmuDownloadBean> downloadDanmu(@Path("episodeId") String episodeId);

    @GET("api/v2/homepage/banner")
    Observable<BannerBeans> getBanner();

    @GET("api/v2/bangumi/shin")
    Observable<BangumiBean> getAnimes();

    @GET("api/v2/bangumi/{animeId}")
    Observable<AnimeDetailBean> getAnimaDetail(@Path("animeId") String animaId);

    @FormUrlEncoded
    @POST("api/v2/login")
    Observable<PersonalBean> login(@FieldMap Map<String, String> params);

    @GET("api/v2/login/renew")
    Observable<PersonalBean> reToken();

    @GET("api/v2/favorite")
    Observable<AnimeFavoriteBean> getFavorite();

    @GET("/api/v2/playhistory")
    Observable<PlayHistoryBean> getPlayHistory();

    @FormUrlEncoded
    @POST("/api/v2/favorite")
    Observable<CommJsonEntity> addFavorite(@FieldMap Map<String, String> params);

    @DELETE("/api/v2/favorite/{animeId}")
    Observable<CommJsonEntity> reduceFavorite(@Path("animeId") String animaId);

    @FormUrlEncoded
    @POST("/api/v2/register")
    Observable<RegisterBean> register(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/api/v2/user/password")
    Observable<CommJsonEntity> changePassword(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/api/v2/register/resetpassword")
    Observable<CommJsonEntity> resetPassword(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/api/v2/user/profile")
    Observable<CommJsonEntity> changeScreenName(@Field("screenName") String screenName);

    @GET("/list")
    Observable<MagnetBean> searchMagnet(@Query("keyword") String keyword, @Query("type") String typeId, @Query("subgroup") String subGroupId);

    @GET("/type")
    Observable<AnimeTypeBean> getAnimeType();

    @GET("/subgroup")
    Observable<SubGroupBean> getSubGroup();

    @POST("/Magnet/Parse")
    Observable<ResponseBody> downloadTorrent(@Body RequestBody requestBody);

    @POST("/api/v2/playhistory")
    Observable<CommJsonEntity> addPlayHistory(@Body HistoryParam params);

    @GET("/api/v2/bangumi/season/anime")
    Observable<SeasonAnimeBean> getAnimeSeason();

    @GET("/api/v2/bangumi/season/anime/{year}/{month}")
    Observable<BangumiBean> getSeasonAnime(@Path("year") String year, @Path("month") String month);

    //-----------------------------------------------------------

    @Streaming
    @GET("/{abi}")
    Observable<ResponseBody> downloadSo(@Path("abi") String abi);
}
