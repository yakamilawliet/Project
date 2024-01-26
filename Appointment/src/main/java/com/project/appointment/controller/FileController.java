package com.project.appointment.controller;

import cn.hutool.core.lang.UUID;
import com.aliyun.oss.model.ObjectMetadata;
import com.project.appointment.common.Result;
import com.project.appointment.service.impl.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/file")
public class FileController {


 @Autowired
 private FileService fileService;

 @PostMapping(value = "/avatar/upload", headers = "content-type=multipart/form-data")
 public Result uploadAvatar(@RequestParam("file") MultipartFile file) {
     return Result.success(fileService.uploadFile(file));
 }

 @GetMapping("/avatar/download/{filename}")
 public Result downloadAvatar(@PathVariable("filename") String filename, HttpServletResponse response) throws IOException {
     String status = fileService.download(filename, response);
     if (status.equals("success")) {
         return Result.success(status);
     } else {
         return Result.error(status);
     }
 }

// @PostMapping(value = "/upload", headers = "content-type=multipart/form-data")
// public Result uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
//
//     // 获取文件的输入流
//     InputStream inputStream = file.getInputStream();
//     // 生成文件名
//     String filename = UUID.randomUUID().toString() + file.getOriginalFilename();
//     // 调用文件上传方法
//     String fileUrl = fileService.uploadFile(filename, inputStream, );
//     return Result.success(fileUrl);
// }

// @GetMapping("/download/{filename}")
// public Result downloadFile(@PathVariable("filename") String filename) throws IOException {
//
//     // 调用文件下载方法
//     InputStream inputStream = fileService.downloadFile(filename);
//     // 设置文件响应头
//     HttpHeaders headers = new HttpHeaders();
//     headers.add("Content-Disposition", "attachment; filename=" + filename);
//     ResponseEntity<InputStreamResource> responseEntity = ResponseEntity.ok()
//             .headers(headers)
//             .contentType(MediaType.APPLICATION_OCTET_STREAM)
//             .body(new InputStreamResource(inputStream));
//     return Result.success(responseEntity);
// }
}