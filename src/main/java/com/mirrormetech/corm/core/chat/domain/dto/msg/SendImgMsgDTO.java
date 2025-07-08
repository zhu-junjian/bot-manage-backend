package com.mirrormetech.corm.core.chat.domain.dto.msg;

import com.alibaba.fastjson.JSON;
import com.mirrormetech.corm.core.message.domain.MessageTypeEnum;
import com.mirrormetech.corm.core.message.domain.content.ImgTextContent;
import lombok.Data;

@Data
public class SendImgMsgDTO extends BaseMsgDTO {

    private Integer receiverType;

    // 标题
    private String title;

    // 文本
    private String text;

    // 跳转文本
    private String jumpText;

    // 跳转URL
    private String jumpUrl;

    // 图片URL
    private String imageUrl;

    @Override
    public Integer getMessageType(){
        return MessageTypeEnum.IMG_TEXT.getCode();
    }

    @Override
    public String getContent() {
        ImgTextContent imgTextContent = new ImgTextContent();
        imgTextContent.setTitle(title);
        imgTextContent.setText(text);
        imgTextContent.setJumpText(jumpText);
        imgTextContent.setJumpUrl(jumpUrl);
        imgTextContent.setImageUrl(imageUrl);
        return JSON.toJSONString(imgTextContent);
    }
}
