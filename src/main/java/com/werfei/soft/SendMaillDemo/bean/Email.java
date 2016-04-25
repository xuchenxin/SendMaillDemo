package com.werfei.soft.SendMaillDemo.bean;

import java.io.File;

/**
 * Created by wef on 2016/4/25.
 */
public class Email {
    // 是否以Html的形式发送邮件内容。默认就是用html的形式发送吧！
    private boolean htmlContent = true;
    // 邮件接收者，用逗号隔开。如果有称谓描述，使用半角小括号表示。例如：abc@111.com(张三)
    private String receivers = null;
    // 邮件抄送者，用逗号隔开，称谓信息同邮件接收者。
    private String cc = null;
    // 邮件密送者，规则同接收者。
    private String bcc = null;
    // 邮件标题。
    private String subject = null;
    // 邮件内容。
    private String content = null;
    // 邮件附件文件名列表，注意文件名是需要包含文件绝对路径的。
    private String[] attachFileNames = null;
    // 邮件附件文件列表。
    private File[] attachFiles = null;
    // 邮件发送日期。
    private String mailDate = null;
    // 邮件发送者。理论上来说应该跟Transport实例连接的用户名一致。规则同邮件接收者。但是邮件发送者只能有一个。
    private String sender = null;
    // 邮件发送者的密码。
    private String senderPwd = null;
    // 邮件服务器地址。
    private String host = null;
    // 发送邮件使用的协议名称。
    private String protocol = null;

    /**
     * @return the htmlContent
     */
    public boolean isHtmlContent() {
        return htmlContent;
    }
    /**
     * @param htmlContent the htmlContent to set
     */
    public void setHtmlContent(boolean htmlContent) {
        this.htmlContent = htmlContent;
    }
    /**
     * @return the receivers
     */
    public String getReceivers() {
        return receivers;
    }
    /**
     * @param receivers the receivers to set
     */
    public void setReceivers(String receivers) {
        this.receivers = receivers;
    }
    /**
     * @return the cc
     */
    public String getCc() {
        return cc;
    }
    /**
     * @param cc the cc to set
     */
    public void setCc(String cc) {
        this.cc = cc;
    }
    /**
     * @return the bcc
     */
    public String getBcc() {
        return bcc;
    }
    /**
     * @param bcc the bcc to set
     */
    public void setBcc(String bcc) {
        this.bcc = bcc;
    }
    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }
    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }
    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }
    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }
    /**
     * @return the attachFileNames
     */
    public String[] getAttachFileNames() {
        return attachFileNames;
    }
    /**
     * @param attachFileNames the attachFileNames to set
     */
    public void setAttachFileNames(String[] attachFileNames) {
        this.attachFileNames = attachFileNames;
    }
    /**
     * @return the attachFiles
     */
    public File[] getAttachFiles() {
        return attachFiles;
    }
    /**
     * @param attachFiles the attachFiles to set
     */
    public void setAttachFiles(File[] attachFiles) {
        this.attachFiles = attachFiles;
    }
    /**
     * @return the mailDate
     */
    public String getMailDate() {
        return mailDate;
    }
    /**
     * @param mailDate the mailDate to set
     */
    public void setMailDate(String mailDate) {
        this.mailDate = mailDate;
    }
    /**
     * @return the sender
     */
    public String getSender() {
        return sender;
    }
    /**
     * @param sender the sender to set
     */
    public void setSender(String sender) {
        this.sender = sender;
    }
    /**
     * @return the senderPwd
     */
    public String getSenderPwd() {
        return senderPwd;
    }
    /**
     * @param senderPwd the senderPwd to set
     */
    public void setSenderPwd(String senderPwd) {
        this.senderPwd = senderPwd;
    }
    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }
    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }
    /**
     * @return the protocol
     */
    public String getProtocol() {
        return protocol;
    }
    /**
     * @param protocol the protocol to set
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
