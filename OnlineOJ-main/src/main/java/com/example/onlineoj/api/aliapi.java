package com.example.onlineoj.api;

import java.util.Arrays;
import java.lang.System;
import java.util.Scanner;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class aliapi {
    public static GenerationResult callWithMessage(String message1) throws ApiException, NoApiKeyException, InputRequiredException {
        Generation gen = new Generation();
        Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content("我给你的文本都是算法题和我的题解，上面是题目，下面是题解，你要判断我的做法对不对，只要不能直接通过题解直接实现题目的要求就算错，首先告诉我正确与错误，再解释原因"
                        )
                .build();
        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content(message1)
                .build();
        GenerationParam param = GenerationParam.builder()
                // 若没有配置环境变量，请用阿里云百炼API Key将下行替换为：.apiKey("sk-xxx")
                .apiKey("sk-086fc6dbee744eeb8f4f9b741c645fe3")
                // 模型列表：https://help.aliyun.com/zh/model-studio/getting-started/models
                .model("qwen-plus")
                .messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();
        return gen.call(param);
    }
    public static String callWithMessage1(String message1) throws ApiException, NoApiKeyException, InputRequiredException {
        GenerationResult result = callWithMessage(message1);
        String message2=result.getOutput().getChoices().get(0).getMessage().getContent();
        return message2;
    }
}
    /*public static void main() {
        try {
            Scanner = new Scanner(System.in);
            String message1 = scanner.nextLine();
            aliapi api = new aliapi();
            String message2 = callWithMessage1(message1);

            System.out.println(message2);
        } catch (ApiException e) {
            System.err.println("错误信息："+e.getMessage());

        } catch (NoApiKeyException e) {
            throw new RuntimeException(e);
        } catch (InputRequiredException e) {
            throw new RuntimeException(e);
        }

    }
}*/