/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: DingtalkService
 * Author:   rubby
 * Date:     2019/2/19 16:02
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.lxd.educ.service;

import com.lxd.educ.entity.MessageInfo;
import com.lxd.educ.result.SendResult;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author rubby
 * @create 2019/2/19
 * @since 1.0.0
 */

public interface DingtalkService {
    /**
     *
     * @param messageInfo
     * @return
     */
    SendResult sendMessage(MessageInfo messageInfo);
}
