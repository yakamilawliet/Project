package com.project.appointment.controller;

import com.project.appointment.common.Result;
import com.project.appointment.service.impl.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileController {


 @Autowired
 private FileService fileService;

 @PostMapping(value = "/avatar/upload", headers = "content-type=multipart/form-data")
 public Result uploadAvatar(@RequestParam("file") MultipartFile file) {
     return Result.success(fileService.uploadAvatar(file));
 }

 @GetMapping("/avatar/download/{filename}")
 public Result downloadAvatar(@PathVariable("filename") String filename, HttpServletResponse response) throws IOException {
     String status = fileService.downloadAvatar(filename, response);
     if (status.equals("success")) {
         return Result.success(status);
     } else {
         return Result.error(status);
     }
 }
}