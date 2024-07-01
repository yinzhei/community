package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
@RequestMapping("user")
public class UserController {
    private static final Logger logger= LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Value("${community.path.upload}")
    private String uploadPath;
    @Autowired
    private HostHolder hostHolder;
    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @LoginRequired
    @RequestMapping(path = "setting",method = RequestMethod.GET)
    public String setting(){
        return "site/setting.html";
    }

    @LoginRequired
    @RequestMapping(path = "upload",method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model){
        if (headerImage==null){
            model.addAttribute("error","您还没有选择图片");
            return "site/setting.html";
        }
        String filename = headerImage.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)){
            model.addAttribute("error","文件格式不正确");
            return "site/setting.html";
        }
        filename = CommunityUtil.generateUUID()+suffix;
        File dest = new File(uploadPath+"/"+filename);
        try  {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath +"/user/headImg/" + filename;
        userService.updateHeadUrlById(user.getId(),headerUrl);
        return "redirect:/index";
    }

    @RequestMapping(value = "headImg/{filename}",method = RequestMethod.GET)
    @ResponseBody
    public String getHeadImg(@PathVariable("filename") String filename, HttpServletResponse response){
        filename = uploadPath + "/"+filename;
        String suffix = filename.substring(filename.lastIndexOf("."));
        response.setContentType("image/"+suffix);
        try(
            OutputStream os = response.getOutputStream();
            FileInputStream fis = new FileInputStream(filename);
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b=fis.read(buffer))!=-1){
                os.write(buffer,0,b);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "site/setting.html";
    }

}
