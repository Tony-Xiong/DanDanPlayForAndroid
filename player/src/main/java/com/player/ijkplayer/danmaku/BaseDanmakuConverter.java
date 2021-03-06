package com.player.ijkplayer.danmaku;

import com.player.danmaku.danmaku.model.BaseDanmaku;
import com.player.danmaku.danmaku.model.FBDanmaku;
import com.player.danmaku.danmaku.model.FTDanmaku;
import com.player.danmaku.danmaku.model.R2LDanmaku;

import static com.player.danmaku.danmaku.model.BaseDanmaku.TYPE_SCROLL_RL;

/**
 * Created by long on 2016/12/22.
 * 弹幕转换器，用来进行自定义数据和弹幕的转换
 */
public abstract class BaseDanmakuConverter<T extends BaseDanmakuData> {

//    BaseDanmaku parseDanmaku(T info);

    public abstract T convertDanmaku(BaseDanmaku danmaku);

    /**
     * 对弹幕的基础数据进行初始化
     * @param data
     * @param danmaku
     */
    protected void initData(T data, BaseDanmaku danmaku) {
        int danmakuType = TYPE_SCROLL_RL;
        if (danmaku instanceof R2LDanmaku) {
            danmakuType = BaseDanmaku.TYPE_SCROLL_RL;
        } else if (danmaku instanceof FBDanmaku) {
            danmakuType = BaseDanmaku.TYPE_FIX_BOTTOM;
        } else if (danmaku instanceof FTDanmaku) {
            danmakuType = BaseDanmaku.TYPE_FIX_TOP;
        }
        data.type = danmakuType;
        data.content = danmaku.text.toString();
        data.time = danmaku.getTime();
        data.textSize = danmaku.textSize;
        data.textColor = danmaku.textColor;
    }
}
