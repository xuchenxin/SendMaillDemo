package com.werfei.soft.SendMaillDemo.service;

import com.werfei.soft.SendMaillDemo.bean.Email;
import com.werfei.soft.SendMaillDemo.utils.AlgorithmicUtils;
import org.springframework.scheduling.annotation.Async;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Properties;

import static com.sun.org.apache.xml.internal.serialize.OutputFormat.Defaults.Encoding;

/**
 * Created by wef on 2016/4/25.
 */
public class MailServiceImpl implements MailService {

    private String host;

    private String sender;

    private String senderPwd;

    private String protocol;

    // 邮件连接会话
    private Session mailSession = null;
    // 邮件连接
    private Transport transport = null;

    public void sendMail(Email mailInfo) {
        try {
            // 初始化会话，保证邮件可以发送。
            if (!initSession(mailInfo)) {
                return;
            }
            MimeMessage message = new MimeMessage(mailSession);
            // 邮件发送者初始化。
            InternetAddress sender = new InternetAddress();
            String user = mailInfo.getSender();
            int nameStart = user.indexOf("(");
            if (nameStart > 0) {
                String address = user.replaceAll("(.*?)\\(.*", "$1");
                String pName = user.replaceAll(".*?\\((.*?)\\).*", "$1");
                sender.setAddress(address);
                sender.setPersonal(pName, Encoding);
            } else {
                // 这里不写的话，居然可以发出去，而且收到邮件后，发件人居然是空(null)！
                sender.setAddress(user);
            }
            // 发件人。
            message.setFrom(sender);
            // 标题。
            message.setSubject(mailInfo.getSubject(), Encoding);
            // 内容。
            if (mailInfo.isHtmlContent()) {
                File[] attachFiles = mailInfo.getAttachFiles();
                String[] attachNames = mailInfo.getAttachFileNames();
                if (AlgorithmicUtils.isEmpty(attachFiles) && AlgorithmicUtils.isEmpty(attachFiles)) {
                    message.setContent(mailInfo.getContent(), "text/html;charset=utf8");
                } else {
                    MimeMultipart contentMulti = new MimeMultipart("related");
                    // 添加正文。
                    MimeBodyPart contentBody = createContentHtml(mailInfo.getContent());
                    contentMulti.addBodyPart(contentBody);
                    // 添加附件。
                    if (AlgorithmicUtils.isEmpty(attachFiles)) {
                        // 添加指定文件名的附件。
                        for (String fileName : attachNames) {
                            MimeBodyPart part = createAttachment(fileName);
                            contentMulti.addBodyPart(part);
                        }
                    } else {
                        // 添加文件类型的附件。
                        for (File file : attachFiles) {
                            MimeBodyPart part = createAttachment(file);
                            contentMulti.addBodyPart(part);
                        }
                    }
                    // 添加到消息中。
                    message.setContent(contentMulti);
                }
            } else {
                // 直接设置纯文本内容。
                message.setText(mailInfo.getContent(), Encoding);
            }
            // 收件人。
            String receivers = mailInfo.getReceivers();
            for (String receiver : receivers.split(",")) {
                // 添加到收件人列表中。
                message.addRecipient(MimeMessage.RecipientType.TO, getAddress(receiver));
            }

            // 可选项
            // 抄送人。
            String ccs = mailInfo.getCc();
            if (!AlgorithmicUtils.isEmpty(ccs)) {
                for (String receiver : ccs.split(",")) {
                    // 添加到抄送收件人列表中。
                    message.addRecipient(MimeMessage.RecipientType.CC, getAddress(receiver));
                }
            }
            // 密送。
            String bccs = mailInfo.getBcc();
            if (!AlgorithmicUtils.isEmpty(bccs)) {
                for (String receiver : bccs.split(",")) {
                    // 添加到抄送收件人列表中。
                    message.addRecipient(MimeMessage.RecipientType.BCC, getAddress(receiver));
                }
            }
            // 发送前需要先保存一下邮件消息。
            message.saveChanges();
            // 发送邮件！
            transport.sendMessage(message, message.getAllRecipients());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("完成了");
        }

    }

    @Async
    public void sendMail(String subject, String content, String... receivers) {
        if (receivers == null || receivers.length == 0) {
            return;
        }
        Email mailInfo = new Email();
        mailInfo.setSubject(subject);
        mailInfo.setContent(content);
        mailInfo.setHtmlContent(false);
        // 如果指定了邮件接收者，那么使用指定的邮件接收人

        String totalReceiver = "";
        // 拼接邮件接收人，用逗号隔开。
        for (String receiver : receivers) {
            totalReceiver += receiver + ",";
        }
        // 去掉邮件接收人最后的逗号。
        totalReceiver = totalReceiver.replaceAll(",$", "");
        mailInfo.setReceivers(totalReceiver);
        sendMail(mailInfo);
    }

    @Async
    public void sendHtmlMail(String subject, String content, String... receivers) {
        // 判断邮件接收者是否为空
        if (receivers == null || receivers.length == 0) {
            return;
        }
        Email mailInfo = new Email();
        mailInfo.setSubject(subject);
        mailInfo.setContent(content);
        mailInfo.setHtmlContent(true);
        String totalReceiver = "";
        // 拼接邮件接收人，用逗号隔开。
        for (String receiver : receivers) {
            totalReceiver += receiver + ",";
        }
        // 去掉邮件接收人最后的逗号。
        totalReceiver = totalReceiver.replaceAll(",$", "");
        mailInfo.setReceivers(totalReceiver);
        sendMail(mailInfo);
    }

    private InternetAddress getAddress(String mailAddress) {
        if (AlgorithmicUtils.isEmpty(mailAddress)) {
            return null;
        }
        InternetAddress address = new InternetAddress();
        if (mailAddress.indexOf("(") > 0) {
            String addr = mailAddress.replaceAll("(.*?)\\(.*", "$1");
            String pName = mailAddress.replaceAll(".*?\\((.*?)\\).*", "$1");
            address.setAddress(addr);
            try {
                address.setPersonal(pName, Encoding);
            } catch (Exception e) {
                try {
                    address.setPersonal(pName);
                } catch (Exception e2) {
                }
            }
        } else {
            address.setAddress(mailAddress);
        }

        return address;
    }

    private boolean initSession(Email info) {
        try {
            // 邮件初始化需要的配置文件。
            // 设置邮件服务器地址。
            if (AlgorithmicUtils.isEmpty(info.getHost())) {
                info.setHost(host);
            }
            // 设置邮件发送者。
            if (AlgorithmicUtils.isEmpty(info.getSender())) {
                info.setSender(sender);
            }
            // 设置邮件发送者密码
            if (AlgorithmicUtils.isEmpty(info.getSenderPwd())) {
                info.setSenderPwd(senderPwd);
            }
            // 设置邮件发送时使用的协议
            if (AlgorithmicUtils.isEmpty(info.getProtocol())) {
                info.setProtocol(protocol);
            }

            // 设置标准的javax.mail需要的配置
            Properties props = new Properties();
            props.put("mail.smtp.host", info.getHost());
            props.put("mail.smtp.auth", "true");
            props.put("mail.transport.protocol", info.getProtocol());
            // 获取默认的会话。
            mailSession = Session.getDefaultInstance(props);
            // 不要调试，防止速度过慢。
            mailSession.setDebug(false);
            transport = mailSession.getTransport();
            String user = info.getSender();
            int nameStart = user.indexOf("(");
            if (nameStart > 0) {
                user = user.substring(0, nameStart);
            }
            transport.connect(user, info.getSenderPwd());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // 根据指定的文件路径，创建一个邮件的附件部分。
    private MimeBodyPart createAttachment(String filePath) {
        // 检查文件路径是否存在。
        File file = new File(filePath);
        return createAttachment(file);
    }

    // 根据指定的文件，创建一个邮件的附件部分。
    private MimeBodyPart createAttachment(File attach) {
        // 检查文件路径是否存在。
        if (!(attach.exists() && attach.isFile() && attach.canRead())) {
            return null;
        }
        // 如果文件存在，并且可读，创建附件。
        MimeBodyPart attachmentPart = new MimeBodyPart();
        try {
            FileDataSource fds = new FileDataSource(attach);
            attachmentPart.setDataHandler(new DataHandler(fds));
            attachmentPart.setFileName(fds.getName());
            attachmentPart.setContentID(fds.getName());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return attachmentPart;
    }

    // 以指定的字符串为html内容，创建一个邮件的正文部分。
    // 注意： 组合正文以及正文中使用的附件,需要创建一个新的父MimeMultipart，类型为：related,
    //       组合所有的BodyPart，需要使用新的父MimeMultipart，类型为：mixed
    private MimeBodyPart createContentHtml(String strContent) {
        MimeBodyPart textBody = new MimeBodyPart();
        try {
            if (AlgorithmicUtils.isEmpty(strContent)) {
                textBody.setContent("", "text/html;charset=utf8");
                return textBody;
            }
            textBody.setContent(strContent, "text/html;charset=utf8");
        } catch (Exception e) {
        }
        return textBody;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderPwd() {
        return senderPwd;
    }

    public void setSenderPwd(String senderPwd) {
        this.senderPwd = senderPwd;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
