package com.spedu.controller;


import com.spedu.GPTUtils.Chat;
import com.spedu.util.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    @Resource
    Chat chat;

    @GetMapping("/addChat")
    public Result addChat(@RequestParam("question") String question){
        return Result.success(chat.playChat(chat,question));
    }

}
