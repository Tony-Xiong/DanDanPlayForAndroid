package com.xyoye.dandanplay.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xyoye.dandanplay.R;
import com.xyoye.dandanplay.base.BaseMvpActivity;
import com.xyoye.dandanplay.base.BaseRvAdapter;
import com.xyoye.dandanplay.bean.AnimeBean;
import com.xyoye.dandanplay.bean.AnimeDetailBean;
import com.xyoye.dandanplay.bean.event.SearchMagnetEvent;
import com.xyoye.dandanplay.mvp.impl.AnimeDetailPresenterImpl;
import com.xyoye.dandanplay.mvp.presenter.AnimeDetailPresenter;
import com.xyoye.dandanplay.mvp.view.AnimeDetailView;
import com.xyoye.dandanplay.ui.weight.CornersCenterCrop;
import com.xyoye.dandanplay.ui.weight.ExpandableTextView;
import com.xyoye.dandanplay.ui.weight.ScrollableHelper;
import com.xyoye.dandanplay.ui.weight.ScrollableLayout;
import com.xyoye.dandanplay.ui.weight.SpacesItemDecoration;
import com.xyoye.dandanplay.ui.weight.item.AnimeEpisodeItem;
import com.xyoye.dandanplay.ui.weight.item.AnimeMoreItem;
import com.xyoye.dandanplay.ui.weight.item.AnimeRecommendItem;
import com.xyoye.dandanplay.utils.AppConfig;
import com.xyoye.dandanplay.utils.CommonUtils;
import com.xyoye.dandanplay.utils.interf.AdapterItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by YE on 2018/7/20.
 */

public class AnimeDetailActivity extends BaseMvpActivity<AnimeDetailPresenter> implements AnimeDetailView, ScrollableHelper.ScrollableContainer{
    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolBar;
    @BindView(R.id.scroll_layout)
    ScrollableLayout scrollableLayout;
    @BindView(R.id.anima_image_iv)
    ImageView animaImageIv;
    @BindView(R.id.anima_title_tv)
    TextView animaTitleTv;
    @BindView(R.id.anima_rating_tv)
    TextView animaTRatingTv;
    @BindView(R.id.anima_onair_tv)
    TextView animaOnairTv;
    @BindView(R.id.anima_airday_tv)
    TextView animaAirdayTv;
    @BindView(R.id.anima_favorited_tv)
    TextView animaFavoritedTv;
    @BindView(R.id.anima_restricted_tv)
    TextView animaRestrictedTv;
    @BindView(R.id.anima_intro_tv)
    ExpandableTextView animaIntroTv;
    @BindView(R.id.nested_scroll_view)
    NestedScrollView nestedScrollView;
    @BindView(R.id.anime_type_tv)
    TextView animeTypeTv;
    @BindView(R.id.anima_info_ll)
    TableLayout animaInfoLl;
    @BindView(R.id.select_episode_tv)
    TextView selectEpisodeTv;
    @BindView(R.id.episode_linear_rv)
    RecyclerView episodeLinearRv;
    @BindView(R.id.recommend_rv)
    RecyclerView recommendRv;
    @BindView(R.id.recommend_ll)
    LinearLayout recommendLl;
    @BindView(R.id.more_rv)
    RecyclerView moreRv;
    @BindView(R.id.more_ll)
    LinearLayout moreLl;
    @BindView(R.id.exit_select_iv)
    ImageView exitSelectIv;
    @BindView(R.id.episode_grid_rv)
    RecyclerView episodeGridRv;
    @BindView(R.id.normal_episode_ll)
    LinearLayout normalEpisodeLL;
    @BindView(R.id.select_episode_ll)
    LinearLayout selectEpisodeLl;

    private AnimeDetailBean animeDetailBean;
    MenuItem favoriteItem = null;
    private boolean isFavorite = false;
    private String animaId = "";

    private BaseRvAdapter<AnimeDetailBean.BangumiBean.EpisodesBean> episodeLinearAdapter;
    private BaseRvAdapter<AnimeDetailBean.BangumiBean.EpisodesBean> episodeGridAdapter;
    private BaseRvAdapter<AnimeBean> recommendAdapter;
    private BaseRvAdapter<AnimeBean> moreAdapter;

    List<AnimeDetailBean.BangumiBean.EpisodesBean> episodeLinearList;
    List<AnimeDetailBean.BangumiBean.EpisodesBean> episodeGridList;
    List<AnimeBean> recommendList;
    List<AnimeBean> moreList;

    @Override
    public void initView() {
        setTitle(R.string.anime_detail_title);
        episodeLinearList = new ArrayList<>();
        episodeGridList = new ArrayList<>();
        recommendList = new ArrayList<>();
        moreList = new ArrayList<>();

        episodeLinearAdapter = new BaseRvAdapter<AnimeDetailBean.BangumiBean.EpisodesBean>(episodeLinearList) {
            @NonNull
            @Override
            public AdapterItem<AnimeDetailBean.BangumiBean.EpisodesBean> onCreateItem(int viewType) {
                return new AnimeEpisodeItem(false);
            }
        };

        episodeGridAdapter = new BaseRvAdapter<AnimeDetailBean.BangumiBean.EpisodesBean>(episodeGridList) {
            @NonNull
            @Override
            public AdapterItem<AnimeDetailBean.BangumiBean.EpisodesBean> onCreateItem(int viewType) {
                return new AnimeEpisodeItem(true);
            }
        };

        recommendAdapter = new BaseRvAdapter<AnimeBean>(recommendList) {
            @NonNull
            @Override
            public AdapterItem<AnimeBean> onCreateItem(int viewType) {
                return new AnimeRecommendItem();
            }
        };

        moreAdapter = new BaseRvAdapter<AnimeBean>(moreList) {
            @NonNull
            @Override
            public AdapterItem<AnimeBean> onCreateItem(int viewType) {
                return new AnimeMoreItem();
            }
        };

        episodeLinearRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        episodeLinearRv.setNestedScrollingEnabled(false);
        episodeLinearRv.addItemDecoration(new SpacesItemDecoration(10));
        episodeLinearRv.setAdapter(episodeLinearAdapter);

        episodeGridRv.setLayoutManager(new GridLayoutManager(this, 2));
        episodeGridRv.addItemDecoration(new SpacesItemDecoration(0, 10 ,10,10, 2));
        episodeGridRv.setAdapter(episodeGridAdapter);

        recommendRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recommendRv.setNestedScrollingEnabled(false);
        recommendRv.addItemDecoration(new SpacesItemDecoration(10));
        recommendRv.setAdapter(recommendAdapter);

        moreRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        moreRv.setNestedScrollingEnabled(false);
        moreRv.setAdapter(moreAdapter);

        animaId = getIntent().getStringExtra("animaId");
        presenter.getAnimeDetail(animaId);
    }

    @Override
    public void initListener() {

    }

    @NonNull
    @Override
    protected AnimeDetailPresenter initPresenter() {
        return new AnimeDetailPresenterImpl(this, this);
    }

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_anime_detail;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favorite:
                if (AppConfig.getInstance().isLogin()) {
                    if (isFavorite) {
                        presenter.followCancel(animaId);
                    } else {
                        presenter.followConfirm(animaId);
                    }
                } else {
                    ToastUtils.showShort(R.string.anime_detail_not_login_hint);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorite, menu);
        favoriteItem = menu.findItem(R.id.favorite);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void showAnimeDetail(AnimeDetailBean bean) {
        animeDetailBean = bean;
        //封面
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .transform(new CornersCenterCrop(10));
        Glide.with(this)
                .load(bean.getBangumi().getImageUrl())
                .apply(options)
                .into(animaImageIv);
        //标题
        animaTitleTv.setText(bean.getBangumi().getAnimeTitle());
        //连载状态
        animaOnairTv.setText(bean.getBangumi().isIsOnAir()
                ? "状态：连载中"
                : "状态：完结");
        //关注状态
        animaFavoritedTv.setText(bean.getBangumi().isIsFavorited()
                ? "已关注"
                : "未关注");
        if (favoriteItem != null) {
            if (bean.getBangumi().isIsFavorited()) {
                favoriteItem.setTitle("取消关注");
                isFavorite = true;
            } else {
                favoriteItem.setTitle("关注");
                isFavorite = false;
            }
        }
        //连载日期
        animaAirdayTv.setText(getChineseText(bean.getBangumi().getAirDay()));
        //评分
        double rating = bean.getBangumi().getRating();
        int ratingInt = (int) rating;
        if (rating == ratingInt) {
            animaTRatingTv.setText( ratingInt + "");
        } else {
            DecimalFormat df = new DecimalFormat("#.0");
            animaTRatingTv.setText(df.format(rating));
        }
        //类型
        animeTypeTv.setText("类型："+bean.getBangumi().getTypeDescription());
        //限制级
        if (bean.getBangumi().isIsRestricted())
            animaRestrictedTv.setVisibility(View.VISIBLE);
        else
            animaRestrictedTv.setVisibility(View.GONE);
        //简介
        animaIntroTv.setText("简介：" + bean.getBangumi().getSummary());
        //剧集
        episodeGridList.addAll(bean.getBangumi().getEpisodes());
        episodeLinearList.addAll(bean.getBangumi().getEpisodes());
        Collections.reverse(episodeGridList);
        episodeGridAdapter.notifyDataSetChanged();
        episodeLinearAdapter.notifyDataSetChanged();
        //相关推荐
        if (bean.getBangumi().getRelateds() != null && bean.getBangumi().getRelateds().size() > 0){
            recommendLl.setVisibility(View.VISIBLE);
            recommendList.addAll(bean.getBangumi().getRelateds());
            recommendAdapter.notifyDataSetChanged();
        }
        //更多推荐
        if (bean.getBangumi().getSimilars() != null && bean.getBangumi().getSimilars().size() > 0){
            moreLl.setVisibility(View.VISIBLE);
            moreList.addAll(bean.getBangumi().getSimilars());
            moreAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void afterFollow(boolean isFollow) {
        if (favoriteItem == null)  return;
        if (isFollow){
            isFavorite = true;
            favoriteItem.setTitle("取消关注");
            animaFavoritedTv.setText("已关注");
            ToastUtils.showShort("关注成功");
        }else {
            isFavorite = false;
            favoriteItem.setTitle("关注");
            animaFavoritedTv.setText("未关注");
            ToastUtils.showShort("取消关注成功");
        }
    }

    private String getChineseText(int day) {
        switch (day) {
            case 0:
                return "日期：星期日";
            case 1:
                return "日期：星期一";
            case 2:
                return "日期：星期二";
            case 3:
                return "日期：星期三";
            case 4:
                return "日期：星期四";
            case 5:
                return "日期：星期五";
            case 6:
                return "日期：星期六";
            default:
                return "日期：未知";
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SearchMagnetEvent event) {
        String episode = event.getEpisodeName();
        if (episode.startsWith("第") && episode.endsWith("话")) {
            String temp = episode.substring(1, episode.length() - 1);
            episode = CommonUtils.isNum(temp) ? temp : episode;
        }
        Intent intent = new Intent(AnimeDetailActivity.this, SearchActivity.class);
        intent.putExtra("anime_title", "/" + animeDetailBean.getBangumi().getAnimeTitle());
        intent.putExtra("search_word", animeDetailBean.getBangumi().getSearchKeyword() + "" + episode);
        intent.putExtra("is_anime", true);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    @Override
    public void showError(String message) {
        ToastUtils.showShort(message);
    }

    @OnClick({R.id.select_episode_tv, R.id.exit_select_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.select_episode_tv:
                selectEpisodeLl.setVisibility(View.VISIBLE);
                normalEpisodeLL.setVisibility(View.GONE);
                break;
            case R.id.exit_select_iv:
                selectEpisodeLl.setVisibility(View.GONE);
                normalEpisodeLL.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public View getScrollableView() {
        return nestedScrollView;
    }

    @Override
    public RecyclerView getChildView() {
        return moreRv;
    }
}
