package com.spedu.GPTUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class Chat {
    private final String ACCESS_TOKEN_URI = "https://aip.baidubce.com/oauth/2.0/token";
    private final String CHAT_URI = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/eb-instant";
 
    private String apiKey = "oLlnuNh0rVgh9AFpRqMptou8";
    private String secretKey = "XjNLghD60TSlabFpaXWGEsCuBwOSGp88";
    private String accessToken = "";
 
    private OkHttpClient client ;
 
    //请求参数
    private RequestMessage requestBody = new RequestMessage();
    //响应超时时间
    private int responseTimeOut = 5000;
 
    public Chat(){
        this.client = new OkHttpClient.Builder().readTimeout(responseTimeOut, TimeUnit.SECONDS).build();
    }
 
    public Chat(int responseTimeOut){
        this.client = new OkHttpClient.Builder().readTimeout(responseTimeOut,TimeUnit.SECONDS).build();
    }
 
     public boolean getAccessToken(){
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        //创建一个请求
        Request request = new Request.Builder()
                .url(ACCESS_TOKEN_URI+"?client_id=" + apiKey + "&client_secret=" + secretKey + "&grant_type=client_credentials")
                .method("POST",body)
                .addHeader("Content-Type", "application/json")
                .build();
        try {
            //使用浏览器对象发起请求
            Response response = client.newCall(request).execute();
            //只能执行一次response.body().string()。下次再执行会抛出流关闭异常，因此需要一个对象存储返回结果
            String responseMessage = response.body().string();
            log.debug("获取accessToken成功");
            JSONObject jsonObject = JSON.parseObject(responseMessage);
            accessToken = (String) jsonObject.get("access_token");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
 
    /**
     *
     * 获取问题参数，返回答案
     * @param
     */
    public String getAnswer(Message message){
        //通过参数获取一个Message
//        Message message = new Message("user",question);
        //将新的问题添加到消息上下文
        requestBody.addMessage(message);
        String jsonStr = JSON.toJSONString(requestBody);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonStr);
        Request request = new Request.Builder()
                .url(CHAT_URI + "?access_token="+accessToken)
                .addHeader("Content-Type", "application/json")
                .method("POST",body)
                .build();
        try {
            Response response = client.newCall(request).execute();
//            log.debug("发送一次请求，询问问题：{}",question);
            String responseJsonStr = response.body().string();
            ResponseMessage responseMessage = JSON.parseObject(responseJsonStr, ResponseMessage.class);
//            System.out.println("返回的响应结果为:"+responseJsonStr);
            String result = responseMessage.getResult();
            String answer = result.replaceAll("\n+", "\n");
//            log.debug("{}",answer);
//            System.out.println(answer);
            Message assistant = new Message("assistant", answer);
            requestBody.addMessage(assistant);
            getRequestBody();
            return answer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "请联系管理员";
    }
 
    public void getRequestBody(){
        System.out.println(JSON.toJSONString(requestBody));
    }

    public String playChat(Chat chat,String ask){

        boolean result = chat.getAccessToken();
        if (result){
            Scanner scanner = new Scanner(ask);
            String question = scanner.nextLine();
            Message message = new Message("user",question);
            return chat.getAnswer(message);
        }
        return "出错了，请联系管理员";
    }


    public static void main(String[] args) {
//        Chat chat = new Chat();
//        boolean result = chat.getAccessToken();
//        if (result){
//            Scanner scanner = new Scanner(System.in);
//            String question = scanner.nextLine();
//            while(!"q".equals(question)){
//                System.out.println(chat.getAnswer(question));
////                chat.getRequestBody();
//                question = scanner.nextLine();
//            }
//        }
    }
}