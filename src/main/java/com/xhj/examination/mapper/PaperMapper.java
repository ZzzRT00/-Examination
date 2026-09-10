package com.xhj.examination.mapper;

import com.xhj.examination.entity.Paper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PaperMapper {

    List<Paper> selectAll();

    Paper selectById(Long id);

    List<Paper> getPaperListByCourseId(Long courseId);
}