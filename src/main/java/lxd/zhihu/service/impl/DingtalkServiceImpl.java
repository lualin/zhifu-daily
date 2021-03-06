/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: DingtalkServiceImpl
 * Author:   rubby
 * Date:     2019/2/19 16:17
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package lxd.zhihu.service.impl;

import com.alibaba.fastjson.JSONObject;
import lxd.zhihu.entity.MessageInfo;
import lxd.zhihu.entity.StoryEntity;
import lxd.zhihu.entity.LatestNewsEntity;
import lxd.zhihu.message.MarkdownMessage;
import lxd.zhihu.result.SendResult;
import lxd.zhihu.service.DingtalkService;
import lxd.zhihu.service.ZhihuNewsService;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author rubby
 * @create 2019/2/19
 * @since 1.0.0
 */
@Service
public class DingtalkServiceImpl implements DingtalkService {

    HttpClient httpclient = HttpClients.createDefault();

    @Autowired
    private ZhihuNewsService zhihuNewsService;

    @Override
    public SendResult sendMessage(MessageInfo messageInfo) {
        /*//构建MarkdownMessage信息
        MarkdownMessage markdownMessage = new MarkdownMessage();
        markdownMessage.setTitle("知乎日报");
        markdownMessage.setMsgType(messageInfo.getMsgType());
        if (messageInfo.getAtMobiles().size() == 0) {
            markdownMessage.setAtAll(true);
        } else {
            markdownMessage.setAtMobiles(messageInfo.getAtMobiles());
            for (String mobile: messageInfo.getAtMobiles()
                 ) {
                markdownMessage.add("@" + mobile);
            }
        }
        markdownMessage.add("\n\n");
        markdownMessage.add(MarkdownMessage.getHeaderText(3, "知乎日报"));
        //拿到知乎新闻信息
        LatestNewsEntity zhifuDailyMessage = zhihuNewsService.getMessage();
        List<StoryEntity> stories = zhifuDailyMessage.getStories();
        for (StoryEntity story: stories
             ) {
            markdownMessage.add(MarkdownMessage.getLinkText(story.getTitle(), "https://news-at.zhihu.com/story/" + story.getId()));
            markdownMessage.add("\n\n");
            for (String image: story.getImages()
                 ) {
                markdownMessage.add(MarkdownMessage.getImageText(image));
                markdownMessage.add("\n\n");
            }
        }
        markdownMessage.add(zhifuDailyMessage.getDate() + "发布");
        markdownMessage.add(MarkdownMessage.getLinkText("知乎", "https://news-at.zhihu.com/"));

        SendResult sendResult = new SendResult();
        //调用钉钉api发送消息
        try {
            HttpPost httpPost = new HttpPost(messageInfo.getWebhook());
            httpPost.addHeader("Content-Type", "application/json; charset=utf-8");
            StringEntity se = new StringEntity(markdownMessage.toJsonString(), "utf-8");
            httpPost.setEntity(se);
            HttpResponse response = httpclient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(response.getEntity());
                JSONObject obj = JSONObject.parseObject(result);
                Integer errcode = obj.getInteger("errcode");
                sendResult.setErrorCode(errcode);
                sendResult.setErrorMsg(obj.getString("errmsg"));
                sendResult.setIsSuccess(errcode.equals(0));
            }
            return sendResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendResult;*/
        return null;
    }
}
