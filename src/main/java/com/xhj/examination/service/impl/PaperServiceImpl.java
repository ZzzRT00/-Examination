package com.xhj.examination.service.impl;

import com.xhj.examination.common.Result;
import com.xhj.examination.entity.Paper;
import com.xhj.examination.mapper.PaperMapper;
import com.xhj.examination.service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PaperServiceImpl implements PaperService {

    @Autowired
    private PaperMapper paperMapper;

    @Override
    public List<Paper> selectAll() {
        return paperMapper.selectAll();
    }

    @Override
    public Paper selectById(Long id) {
        return paperMapper.selectById(id);
    }

    @Override
    public Result<List<Paper>> getPaperListByCourseId(Long courseId) {
        List<Paper> list = paperMapper.getPaperListByCourseId(courseId);
        return Result.success(list);
    }
}
