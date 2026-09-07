package com.xhj.examination.mapper;

import com.xhj.examination.entity.Paper;
import java.util.List;

public interface PaperMapper {

    List<Paper> selectAll();
    Paper selectById(Integer id);
}