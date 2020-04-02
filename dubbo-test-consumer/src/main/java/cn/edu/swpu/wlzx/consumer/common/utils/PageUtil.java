package cn.edu.swpu.wlzx.consumer.common.utils;

import java.util.List;

/**
 * 自定义List分页工具
 * @author gongsir
 * @date 2020/4/2 22:36
 * 编码不要畏惧变化，要拥抱变化
 */
public class PageUtil {

    /**
     * 开始分页
     * @param list 需要分页的list
     * @param pageNum 页码
     * @param pageSize 每页多少条数据
     * @return list
     */
    public static List<?> startPage(List<?> list, Integer pageNum,
                                 Integer pageSize) {
        if (list == null) {
            return null;
        }
        if (list.size() == 0) {
            return null;
        }

        // 记录总数
        Integer count = list.size();
        // 页数
        Integer pageCount = 0;
        if (count % pageSize == 0) {
            pageCount = count / pageSize;
        } else {
            pageCount = count / pageSize + 1;
        }

        // 开始索引
        int fromIndex = 0;
        // 结束索引
        int toIndex = 0;

        if (!pageNum.equals(pageCount)) {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = fromIndex + pageSize;
        } else {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = count;
        }

        return list.subList(fromIndex, toIndex);
    }
}
