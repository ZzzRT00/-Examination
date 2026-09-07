package com.xhj.examination.service;

import com.xhj.examination.entity.Paper;
import java.util.List;

public interface PaperService {
    List<Paper> selectAll();
    Paper selectById(Integer id);
}
