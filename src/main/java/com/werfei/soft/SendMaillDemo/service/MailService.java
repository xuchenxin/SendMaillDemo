package com.werfei.soft.SendMaillDemo.service;

import com.werfei.soft.SendMaillDemo.bean.Email;

/**
 * Created by wef on 2016/4/25.
 */
public interface MailService {

    public void sendMail(Email mailInfo);

    /**
     * 使用系统默认的发件人，给指定的人发送邮件。内容可以是文本，也可以是html。无论是什么类型，都按照文本内容进行发送。
     * 如果没有指定接收者，发送给系统默认的邮件接收者。
     * */
    public void sendMail(String subject,String content,String... receivers);

    /**
     * 使用系统默认的发件人，给指定的人发送邮件。内容可以是文本，也可以是html。但无论是什么类型，都按照html的格式发送。
     * 如果没有指定接收者，发送给系统默认的邮件接收者。
     * */
    public void sendHtmlMail(String subject,String content,String... receivers);
}
