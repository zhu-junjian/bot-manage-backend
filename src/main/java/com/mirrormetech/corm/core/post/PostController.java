package com.mirrormetech.corm.core.post;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mirrormetech.corm.common.result.ApiResult;
import com.mirrormetech.corm.core.post.domain.Post;
import com.mirrormetech.corm.core.post.domain.dto.PostDTO;
import com.mirrormetech.corm.core.post.domain.dto.QueryListDTO;
import com.mirrormetech.corm.core.post.service.impl.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author spencer
 * @date 2025/04/28
 */
@RequestMapping("/api/posts")
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostServiceImpl postService;

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
     * @param firstLevelCategory
     * @param secondLevelCategory
     * @param page
     * @param size
     * @return
     */
    @GetMapping
    public ApiResult<Page<Post>> getPostById(@RequestParam(value = "firstLevelCategory",required = false) Long firstLevelCategory,
                                             @RequestParam(value = "secondLevelCategory",required = false) Long secondLevelCategory,
                                             @RequestParam(value = "page") Integer page,
                                             @RequestParam(value = "size") Integer size) {
        QueryListDTO queryListDTO = new QueryListDTO(firstLevelCategory, secondLevelCategory);
        return ApiResult.success(postService.getAllPosts(queryListDTO, page, size));
    }
}
