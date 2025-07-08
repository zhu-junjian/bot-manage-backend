package com.mirrormetech.corm.core.chat.domain.dto.msg;

import com.alibaba.fastjson.JSON;
import com.mirrormetech.corm.core.message.domain.content.TitleTextContent;
import lombok.Data;

@Data
public class SendTitleMsgDTO extends BaseMsgDTO {

    private Integer receiverType;

    private String title;

    private String text;

    @Override
    public Integer getMessageType(){
        return 4;
    }

    @Override
    public String getContent() {
        TitleTextContent titleTextContent = new TitleTextContent();
        titleTextContent.setTitle(title);
        titleTextContent.setText(text);
        return JSON.toJSONString(titleTextContent);
    }
}
