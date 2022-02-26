package com.example.demo.utils;

import com.example.demo.bean.ResultMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageUtils {
    public static String getMessage(boolean isSystem,String fromName,Object message) throws JsonProcessingException {
        ResultMessage resultMessage=new ResultMessage();
        resultMessage.setSystem(isSystem);
        resultMessage.setMessage(message);
        if(fromName != null){
            resultMessage.setFromName(fromName);
        }
        ObjectMapper mapper=new ObjectMapper();
        return mapper.writeValueAsString(resultMessage);
    }
}
