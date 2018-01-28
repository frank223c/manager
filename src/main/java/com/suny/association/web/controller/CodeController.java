package com.suny.association.web.controller;

import com.suny.association.entity.dto.ResultDTO;
import com.suny.association.enums.LoginEnum;
import com.suny.association.utils.TokenProcessor;
import com.suny.association.web.validate.code.ValidateCodeGenerator;
import com.suny.association.web.validate.code.image.ImageCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Comments:  对页面产生验证码
 *
 * @author :   孙建荣
 *         Create Date: 2017/03/14 17:02
 */
@RequestMapping("/code")
@Controller
public class CodeController {
    private static final String TOKEN = "token";
    private static Logger logger = LoggerFactory.getLogger(CodeController.class);

    private final ValidateCodeGenerator imageCodeGeneratorImpl;

    @Autowired
    public CodeController(ValidateCodeGenerator ImageCodeGeneratorImpl) {
        this.imageCodeGeneratorImpl = ImageCodeGeneratorImpl;
    }


    @RequestMapping("/generateCode")
    public void generateCode(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        ImageCode validateCode = (ImageCode) imageCodeGeneratorImpl.generatorCode();
        // 将四位数字的验证码保存到Session中。
        HttpSession session = req.getSession();
        logger.warn("generation code is : {} ", validateCode.getCode());
        session.setAttribute("code", validateCode.getCode());
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


    /**
     * 在提交登录前检查一次验证码是否正确
     *
     * @param request  request请求
     * @param formCode 表单传过来的验证码
     * @return 验证验证码的结果
     */
    @RequestMapping("/checkCode")
    @ResponseBody
    public ResultDTO checkCode(HttpServletRequest request,
                               @RequestParam String formCode) {
        String sessionCode = (String) request.getSession().getAttribute("code");
        logger.info("表单传过来的验证码为{},session里面的验证码为{}", formCode, sessionCode);
        if (!"".equals(formCode) && sessionCode.equals(formCode)) {
            return ResultDTO.successResult(LoginEnum.VALIDATE_CODE_SUCCESS);
        }
        return ResultDTO.failureResult(LoginEnum.VALIDATE_CODE_ERROR);
    }

    @RequestMapping("/getToken")
    @ResponseBody
    public String getToken(HttpServletRequest request) {
        //   1.1   把session里面的token标记先移除
        request.getSession().removeAttribute(TOKEN);
        //    1.2 产生个新的token
        String token = TokenProcessor.getInstance().makeToken();
        request.getSession().setAttribute(TOKEN, token);
        logger.info("产生的令牌值是 {}", token);
        return token;
    }

}
