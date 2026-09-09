package com.xhj.examination.controller;

import com.xhj.examination.entity.ExamRecord;
import com.xhj.examination.service.ExamRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/examRecord")
@Tag(name = "考试记录相关接口")
public class ExamRecordController {

    @Autowired
    private ExamRecordService examRecordService;

    @PostMapping("/add")
    @Operation(summary = "新增考试记录")
    public String add(@RequestBody ExamRecord examRecord){
        examRecordService.addRecord(examRecord);
        return "新增成功";
    }

    @GetMapping("/list/{studentId}")
    @Operation(summary = "根据学生id查询全部考试历史")
    public List<ExamRecord> getList(@PathVariable Long studentId){
        return examRecordService.getStudentRecord(studentId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据id查询单条考试记录")
    public ExamRecord getOne(@PathVariable Long id){
        return examRecordService.getById(id);
    }
}
