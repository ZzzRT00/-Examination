package com.xhj.examination.controller;

import com.xhj.examination.common.Result;
import com.xhj.examination.entity.Question;
import com.xhj.examination.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question")
@Tag(name = "题目相关接口")
public class QuestionController {

    @Autowired
    private QuestionService questionService;


    @GetMapping("/course/{courseId}")
    @Operation(summary = "查询试卷题目")
    public List<Question> getByCourse(@PathVariable Long courseId){
        return questionService.getQuestionList(courseId);
    }

    @PostMapping("/add")
    @Operation(summary = "添加新题目")
    public String add(@RequestBody Question question){
        int res = questionService.add(question);
        if(res>0){
            return "新增成功";
        }else{
            return "新增失败";
        }
    }

    @PutMapping("/update")
    @Operation(summary = "修改题目")
    public Result updateQuestion(@RequestBody Question question) {
        return questionService.updateQuestion(question);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除题目")
    public Result deleteQuestion(@PathVariable Integer id) {
        return questionService.deleteQuestion(id);
    }
}
