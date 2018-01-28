package com.suny.association.web.controller;

import com.suny.association.web.validate.code.ValidateCodeGenerator;
import com.suny.association.web.validate.code.image.ImageCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Comments:  对页面产生验证码
 * @author :   孙建荣
 *         Create Date: 2017/03/14 17:02
 */
@RequestMapping("/code")
@Controller
public class CodeController {
    private static Logger logger = LoggerFactory.getLogger(CodeController.class);

    private final ValidateCodeGenerator imageCodeGeneratorImpl;

    @Autowired
    public CodeController(ValidateCodeGenerator imageCodeGeneratorImpl) {
        this.imageCodeGeneratorImpl = imageCodeGeneratorImpl;
    }


    @RequestMapping(value = "/imageCode", method = RequestMethod.GET)
    public void generateImageCode(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        ImageCode validateCode = (ImageCode) imageCodeGeneratorImpl.generatorCode();
        // 将四位数字的验证码保存到Session中。
        HttpSession session = req.getSession();
        logger.warn("generation code is : {} ", validateCode.getCode());
        session.setAttribute("imageCode", validateCode.getCode());
        // 禁止图像缓存。
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setDateHeader("Expires", 0);
        resp.setContentType("image/jpeg");
        // 将图像输出到Servlet输出流中。
        ServletOutputStream servletOutputStream = resp.getOutputStream();
        ImageIO.write(validateCode.getImage(), "jpeg", servletOutputStream);
        servletOutputStream.close();
    }


}
