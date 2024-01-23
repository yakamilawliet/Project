package com.project.appointment.utils;


import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;



/**
 * description:
 *
 * @author su
 * @date 2022/2/15 14:41
 */
@Component
public class SMSUtils {

    private static final Logger log = LoggerFactory.getLogger(SMSUtils.class);
    @Value("${sms.accessKeyId}")
    private String accessKeyId;
    @Value("${sms.secret}")
    private String secret;
    @Value("${sms.signName}")
    private String signName; // 短信签名
    @Value("${sms.templateCode}")
    private String templateCode;  //短信模板
    @Value("${sms.regionId}")
    private String regionId;   // 短信服务器区域


    public void sendMsg(String phone, String code) {

        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, secret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();

        request.setSysMethod(MethodType.POST);
        //下面两个不能动
        request.setSysProduct("Dysmsapi");
        request.setSysDomain("dysmsapi.aliyuncs.com");

        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        //自定义参数(手机号,验证码,签名,模板)
        request.putQueryParameter("RegoinId", regionId);
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", signName); //填自己申请的名称
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");

        try {
            CommonResponse response = client.getCommonResponse(request);
            log.info("阿里云短信响应信息：" + response.getData());
            boolean success = response.getHttpResponse().isSuccess();
            log.info("短信发送是否成功：" + success);
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
