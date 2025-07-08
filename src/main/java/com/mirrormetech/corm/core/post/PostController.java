package com.mirrormetech.corm.core.post;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mirrormetech.corm.common.result.ApiResult;
import com.mirrormetech.corm.core.post.domain.Post;
import com.mirrormetech.corm.core.post.domain.dto.PostDTO;
import com.mirrormetech.corm.core.post.domain.dto.QueryListDTO;
import com.mirrormetech.corm.core.post.domain.dto.WorksQueryDTO;
import com.mirrormetech.corm.core.post.infra.Do.PostDO;
import com.mirrormetech.corm.core.post.service.impl.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author spencer
 * @date 2025/04/28
 */
@RequestMapping("/api/v1/posts")
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostServiceImpl postService;

    @GetMapping("/user")
    public ApiResult<Page<PostDO>> listPosts(@RequestParam(value = "sourceUserId", required = false) Long sourceUserId,
                                              @RequestParam(value = "targetUserId",required = true) Long targetUserId,
                                              @RequestParam(value = "page", defaultValue = "1") Integer page,
                                              @RequestParam(value = "size", defaultValue = "20") Integer size) {
        WorksQueryDTO worksQueryDTO = new WorksQueryDTO(sourceUserId, targetUserId, page, size);
        Page<PostDO> userWorks = postService.getUserWorks(worksQueryDTO);
        return ApiResult.success(userWorks);
    }

    @GetMapping("/user/like")
    public ApiResult<Page<PostDO>> likedPost(@RequestParam(value = "sourceUserId", required = false) Long sourceUserId,
                                             @RequestParam(value = "targetUserId",required = true) Long targetUserId,
                                             @RequestParam(value = "page", defaultValue = "1") Integer page,
                                             @RequestParam(value = "size", defaultValue = "20") Integer size) {
        WorksQueryDTO worksQueryDTO = new WorksQueryDTO(sourceUserId, targetUserId, page, size);
        Page<PostDO> userWorks = postService.getUserLikedWorks(worksQueryDTO);
        return ApiResult.success(userWorks);
    }

    /**
     * 2.3.3  内容发布
     * @param postDTO
     * @return
     */
    @PostMapping
    public ApiResult<Post> post(@RequestBody PostDTO postDTO) {
        return ApiResult.success(postService.create(postDTO));
    }

    /**
     * 2.3.4  内容发布-列表查询（热门手信、按内容大类搜索）
     * 是否点赞按照当前登录用户ID查询
     * @param firstLevelCategory 一级大类ID
     * @param secondLevelCategory 二级大类ID
     * @param page 页码
     * @param size 条数
     * @return 社区列表
     */
    @GetMapping
    public ApiResult<Page<Post>> getPostById(@RequestParam(value = "firstLevelCategory",required = false) Long firstLevelCategory,
                                             @RequestParam(value = "secondLevelCategory",required = false) Long secondLevelCategory,
                                             @RequestParam(value = "page") Integer page,
                                             @RequestParam(value = "size") Integer size) {
        QueryListDTO queryListDTO = new QueryListDTO();
        queryListDTO.setFlcId(firstLevelCategory);
        queryListDTO.setSlcId(secondLevelCategory);
        return ApiResult.success(postService.getAllPosts(queryListDTO, new Page<>(page, size)));
    }
}
