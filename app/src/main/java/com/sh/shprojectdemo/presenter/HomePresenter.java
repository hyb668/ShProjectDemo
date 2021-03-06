package com.sh.shprojectdemo.presenter;

import com.sh.shprojectdemo.view.HomeView;

/**
 * Created by zhush on 2017/1/25.
 * E-mail zhush@jerei.com
 * PS
 */
public class HomePresenter {

    HomeView homeView;

    public HomePresenter(HomeView homeView) {
        this.homeView = homeView;
    }

    public void initViewData(){
        String[] urls = new String[]{"http://imgsrc.baidu.com/forum/w%3D580/sign=4c51a1afa5efce1bea2bc8c29f50f3e8/4353b319ebc4b7459f17554fcdfc1e178b8215ea.jpg",
                "http://tc.sinaimg.cn/maxwidth.2048/tc.service.weibo.com/p/img4_cache_netease_com/2479a6e4a2845db0727f44808138f946.jpg",
                "http://www.12fly.com.my/images/lifestyle/EventsC/2015/angry-cat/02.jpg"
       };
        homeView.getImages(urls);
    }
}
