package com.demo.mapper;

import com.demo.beans.BoardInfoBean;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface MenuMapper {
    @Select("select board_info_idx, board_info_name " +
            "from board_info_table " +
            "order by board_info_idx")
    List<BoardInfoBean> getMenuList();
}
