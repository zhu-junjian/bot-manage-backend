package com.mirrormetech.corm.core.chat;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mirrormetech.corm.common.result.ApiResult;
import com.mirrormetech.corm.core.chat.domain.VO.ChatSessionVO;
import com.mirrormetech.corm.core.chat.domain.dto.CreateSessionDTO;
import com.mirrormetech.corm.core.chat.domain.dto.QueryListDTO;
import com.mirrormetech.corm.core.chat.domain.dto.msg.*;
import com.mirrormetech.corm.core.chat.infra.DO.ChatSessionDO;
import com.mirrormetech.corm.core.chat.service.ChatSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

/**
 * 会话域
 * chat session
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {

    @Qualifier("chatSessionServiceImpl")
    private final ChatSessionService chatSessionService;

    /**
     * 消息页首页接口
     * 分页查询 会话列表
     * @param page 分页条件
     * @param req 查询条件 userId  status
     * @return 分页查询结果
     */
    @GetMapping
    public ApiResult<Page<ChatSessionVO>> selectSessionsByUserId(@RequestParam(value = "userId") Long userId,
                                            @RequestParam(value = "page", defaultValue = "1") Integer page,
                                            @RequestParam(value = "size", defaultValue = "20") Integer size) {
        QueryListDTO req = new QueryListDTO();
        req.setUserId(userId);
        req.setStatus(0);
        Page<ChatSessionDO> queryPage = new Page<>(page, size);
        Page<ChatSessionVO> pageResult = chatSessionService.selectSessionByUserId(queryPage,req);
        return ApiResult.success(pageResult);
    }

    /**
     * 删除会话
     * @param sessionId 会话Id
     */
    @PutMapping("/delete/{sessionId}")
    public ApiResult<ChatSessionDO> deleteSession(@PathVariable Long sessionId) {
        chatSessionService.deleteSession(sessionId);
        return ApiResult.success();
    }

    /**
     * 置顶会话
     * @param sessionId 会话Id
     * @return 影响行数 >0 成功
     */
    @PutMapping("/top/{sessionId}")
    public ApiResult<ChatSessionDO> topSession(@PathVariable Long sessionId) {
        chatSessionService.topSession(sessionId);
        return ApiResult.success();
    }

    /**
     * 取消置顶
     * @param sessionId 会话Id
     * @return 影响行数 >0 成功
     */
    @PutMapping("/unTop/{sessionId}")
    public ApiResult<ChatSessionDO> unTopSession(@PathVariable Long sessionId) {
        chatSessionService.unTopSession(sessionId);
        return ApiResult.success();
    }

    /**
     * 创建会话
     */
    @PostMapping()
    public ApiResult<ChatSessionDO> startSession(@RequestBody CreateSessionDTO createSessionDTO) {
        chatSessionService.createSession(createSessionDTO);
        return ApiResult.success();
    }

    @PostMapping("/likeMsg")
    public ApiResult<ChatSessionDO> sendLikeMsg(SendLikeMsgDTO sendLikeMsgDTO){
        return ApiResult.success();
    }

    /**
     * 发布一条手信消息
     * @param sendHandMsgDTO 手信消息传输类
     * @return 发布结果
     */
    @PostMapping("/handMsg")
    public ApiResult<ChatSessionDO> sendHandMsg(@RequestBody SendHandMsgDTO sendHandMsgDTO){
        chatSessionService.sendMessage(sendHandMsgDTO);
        return ApiResult.success();
    }

    @PostMapping("/followingMsg")
    public ApiResult<ChatSessionDO> sendFollowingMsg(){
        return ApiResult.success();
    }

    @PostMapping("/imgMsg")
    public ApiResult<ChatSessionDO> sendImgMsg(@RequestBody SendImgMsgDTO sendImgMsgDTO){
        chatSessionService.sendMessage(sendImgMsgDTO);
        return ApiResult.success();
    }

    @PostMapping("/textMsg")
    public ApiResult<ChatSessionDO> sendTextMsg(@RequestBody SendTextMsgDTO sendTextMsgDTO){
        chatSessionService.sendMessage(sendTextMsgDTO);
        return ApiResult.success();
    }

    @PostMapping("/titleMsg")
    public ApiResult<ChatSessionDO> sendTitleMsg(@RequestBody SendTitleMsgDTO sendTitleMsgDTO){
        chatSessionService.sendMessage(sendTitleMsgDTO);
        return ApiResult.success();
    }
}
