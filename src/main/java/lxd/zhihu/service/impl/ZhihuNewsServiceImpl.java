/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: zhiFuDailyServiceImpl
 * Author:   rubby
 * Date:     2019/2/19 10:27
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package lxd.zhihu.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import lxd.zhihu.aspect.LogAnnotation;
import lxd.zhihu.entity.HotNewsEntity;
import lxd.zhihu.entity.LatestNewsEntity;
import lxd.zhihu.entity.OldNewsEntity;
import lxd.zhihu.service.ZhihuNewsService;
import lxd.zhihu.service.zhihu.ZhihuClient;
import lxd.zhihu.service.zhihu.requests.QueryHotNewsRequest;
import lxd.zhihu.service.zhihu.requests.QueryLatestNewsRequest;
import lxd.zhihu.service.zhihu.requests.QueryOldNewsRequest;
import lxd.zhihu.service.zhihu.responses.*;
import lxd.zhihu.thread.ThreadPoolWorker;
import lxd.zhihu.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author rubby
 * @create 2019/2/19
 * @since 1.0.0
 */
@Service
public class ZhihuNewsServiceImpl implements ZhihuNewsService {

    private static final Logger logger = LoggerFactory.getLogger(ZhihuNewsServiceImpl.class);

    private final ZhihuClient zhihuClient;

    @Autowired
    public ZhihuNewsServiceImpl(ZhihuClient zhihuClient) {
        this.zhihuClient = zhihuClient;
    }

    @Override
    @LogAnnotation
    public LatestNewsResponse queryLatestNews(QueryLatestNewsRequest request) {
        LatestNewsResponse latestNewsResponse = new LatestNewsResponse();

        BaseZhihuResponse zhihuResponse = zhihuClient.getResponse(request);
        BeanUtils.copyProperties(zhihuResponse, latestNewsResponse);
        String data = zhihuResponse.getData();
        LatestNewsEntity latestNewsEntity = JSON.parseObject(data, LatestNewsEntity.class);
        latestNewsResponse.setLatestNewsEntity(latestNewsEntity);
        logger.debug(String.format("%s------获取最新消息成功", request.getRequestId()));
        return latestNewsResponse;
    }

    @Override
    @LogAnnotation
    public OldNewsResponse queryOldNewsByDate(QueryOldNewsRequest request) {
        OldNewsResponse oldNewsResponse = new OldNewsResponse();

        BaseZhihuResponse zhihuResponse = zhihuClient.getResponse(request);
        BeanUtils.copyProperties(zhihuResponse, oldNewsResponse);
        String data = zhihuResponse.getData();
        OldNewsEntity oldNewsEntity = JSON.parseObject(data, OldNewsEntity.class);
        oldNewsResponse.setOldNewsEntity(oldNewsEntity);

        logger.debug(String.format("%s------根据日期%s获取消息成功", request.getRequestId(), request.getDateStr()));
        return oldNewsResponse;
    }

    @Override
    @LogAnnotation
    public HotNewsResponse queryHotNews(QueryHotNewsRequest request) {
        HotNewsResponse hotNewsResponse = new HotNewsResponse();

        BaseZhihuResponse zhihuResponse = zhihuClient.getResponse(request);
        BeanUtils.copyProperties(zhihuResponse, hotNewsResponse);
        String data = zhihuResponse.getData();
        HotNewsEntity hotNewsEntity = JSON.parseObject(data, HotNewsEntity.class);
        hotNewsResponse.setHotNewsEntity(hotNewsEntity);
        logger.debug(String.format("%s------获取热门消息成功", request.getRequestId()));
        return hotNewsResponse;
    }

    @Override
    @LogAnnotation
    public List<OldNewsResponse> listOldNewsByDateRange(String startDateStr, String endDateStr) {
        List<OldNewsResponse> oldNewsResponses = new ArrayList<>();

        List<String> dateStrs = DateUtil.getDateRange(startDateStr, endDateStr);
        if (dateStrs != null) {
            threadPoolBatchProcess(dateStrs, oldNewsResponses);
        }

        return oldNewsResponses;
    }

    /**
     * 线程池批量处理
     *
     * @param dateStrs         时间
     * @param oldNewsResponses 返回值
     */
    private void threadPoolBatchProcess(List<String> dateStrs, List<OldNewsResponse> oldNewsResponses) {
        List<ListenableFuture<Integer>> futures = Lists.newArrayList();
        for (String dateStr : dateStrs
        ) {
            futures.add(ThreadPoolWorker.executorService.submit(() -> {
                oldNewsResponses.add(queryOldNewsByDate(new QueryOldNewsRequest(dateStr)));
                return 1;
            }));
        }
        ListenableFuture<List<Integer>> resultsFuture = Futures.successfulAsList(futures);
        try {
            resultsFuture.get();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
/*    private void threadPoolBatchProcess(List<String> dateStrs, List<OldNewsResponse> oldNewsResponses) {
        for (String dateStr : dateStrs
        ) {
            ListenableFuture<OldNewsResponse> listenableFuture = ThreadPoolWorker.executorService.submit(new QueryOldNewsByDateTask(dateStr));
            listenableFuture.addListener(new Runnable() {
                @Override
                public void run() {
                    try {
                        OldNewsResponse oldNewsResponse = listenableFuture.get();
                        oldNewsResponses.add(oldNewsResponse);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }, ThreadPoolWorker.executorService2);

        }

    }
    class QueryOldNewsByDateTask implements Callable<OldNewsResponse> {
        private  Logger logger = LoggerFactory.getLogger(QueryOldNewsByDateTask.class);

        private String dateStr;

        public QueryOldNewsByDateTask(String dateStr) {
            this.dateStr = dateStr;
        }

        @Override
        public OldNewsResponse call() throws Exception {
            OldNewsResponse oldNewsResponse = queryOldNewsByDate(new QueryOldNewsRequest(dateStr));
            return oldNewsResponse;
        }
    }*/

}
