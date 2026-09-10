package com.xhj.examination.controller;

import com.xhj.examination.common.Result;
import com.xhj.examination.entity.Paper;
import com.xhj.examination.service.PaperService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/paper")
@Tag(name="试卷相关接口")
public class PaperController {

    @Autowired
    private PaperService paperService;
    @GetMapping("/list")
    @Operation(summary = "查看全部试卷信息")
    public Result<List<Paper>> list(){
        List<Paper> paperList = paperService.selectAll();
        return Result.success(paperList);
    }
    @GetMapping("/{id}")
    @Operation(summary = "查看单份试卷信息")
    public Result<Paper> getById(@PathVariable Long id){
        Paper paper = paperService.selectById(id);
        return Result.success(paper);
    }
    @GetMapping("/course/{courseId}")
    @Operation(summary = "根据课程ID查询试卷列表")
    public Result<List<Paper>> getPaperListByCourseId(@PathVariable Long courseId) {
        return paperService.getPaperListByCourseId(courseId);
    }
}
