package com.prism.common.result;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分页响应")
public class PageResult<T> {

    @Schema(description = "当前页数据列表")
    private List<T> data;

    @Schema(description = "分页信息")
    private PageInfo pageInfo;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "分页详情")
    public static class PageInfo {
        @Schema(description = "当前页码", example = "1")
        private int currentPage;

        @Schema(description = "每页条数", example = "20")
        private int pageSize;

        @Schema(description = "总记录数", example = "100")
        private long total;

        @Schema(description = "总页数", example = "5")
        private int totalPages;

        @Schema(description = "是否有下一页")
        private boolean hasNext;

        @Schema(description = "是否有上一页")
        private boolean hasPrevious;
    }

    /**
     * 从 MyBatis-Plus Page 对象构建 PageResult（支持 Entity→VO 转换）
     */
    public static <E, T> PageResult<T> from(IPage<E> page, List<T> data) {
        PageInfo pageInfo = PageInfo.builder()
                .currentPage((int) page.getCurrent())
                .pageSize((int) page.getSize())
                .total(page.getTotal())
                .totalPages((int) page.getPages())
                .hasNext(page.getCurrent() < page.getPages())
                .hasPrevious(page.getCurrent() > 1)
                .build();

        return PageResult.<T>builder()
                .data(data)
                .pageInfo(pageInfo)
                .build();
    }

    /**
     * 从 MyBatis-Plus Page 对象构建 PageResult（数据直接使用 page.getRecords）
     */
    public static <T> PageResult<T> from(IPage<T> page) {
        return from(page, page.getRecords());
    }
}
