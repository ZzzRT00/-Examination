package com.xhj.examination.controller;

import com.xhj.examination.common.Result;
import com.xhj.examination.entity.PaperQuestion;
import com.xhj.examination.entity.PaperQuestionVO;
import com.xhj.examination.entity.StudentPaperQuestionVO;
import com.xhj.examination.service.PaperQuestionService;
import com.xhj.examination.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/paperQuestion")
@Tag(name = "试卷题目相关接口")
public class PaperQuestionController {

    @Autowired
    private PaperQuestionService paperQuestionService;
    @Autowired
    private QuestionService questionService;

    @PostMapping("/add")
    @Operation(summary = "添加题目")
    public Result<Integer> add(@RequestBody PaperQuestion paperQuestion){
        int i = paperQuestionService.add(paperQuestion);
        return i > 0 ? Result.success(i) : Result.fail(500, "添加失败");
    }

    @GetMapping("/list/{paperId}")
    @Operation(summary = "查看试卷题目")
    public Result<List<PaperQuestion>> list(@PathVariable Long paperId){
        return Result.success(paperQuestionService.getByPaperId(paperId));
    }

    @GetMapping("/teacher/paper/{paperId}")
    @Operation(summary = "显示试卷题目（教师端）")
    public Result<List<PaperQuestionVO>> getPaperQuestion(@PathVariable Long paperId){
        List<PaperQuestionVO> list = paperQuestionService.getQuestionByPaperId(paperId);
        return Result.success(list);
    }

    @GetMapping("/student/paper/{paperId}")
    @Operation(summary = "显示试卷题目（学生端）")
    public Result<List<StudentPaperQuestionVO>> getStudentPaperQuestions(@PathVariable Long paperId) {
        List<StudentPaperQuestionVO> list = questionService.getStudentQuestionList(paperId);
        return Result.success(list);
    }
}
