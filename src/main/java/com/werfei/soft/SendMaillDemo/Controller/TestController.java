package com.werfei.soft.SendMaillDemo.Controller;

import com.werfei.soft.SendMaillDemo.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by wef on 2016/4/25.
 */
@Controller
public class TestController {

    @Autowired
    private MailService mailService;

    @RequestMapping("/test")
    @ResponseBody
    public Object test() {
        mailService.sendMail("测试邮件", "这是测试邮件", "459876292@qq.com");
        return "成功";
    }
}
