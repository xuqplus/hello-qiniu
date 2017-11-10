package cn.xuqplus.qiniuphotoalbum.controller;

import cn.xuqplus.qiniuphotoalbum.service.QiniuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api("七牛云相关操作")
@RestController
public class QiniuApiController {

  @Autowired
  QiniuService qiniuService;

  @ApiOperation(value = "生成uptoken")
  @RequestMapping(value = "uptoken", method = RequestMethod.GET)
  public Map getUptoken(HttpSession session) {
    try {
      if ((long) session.getAttribute("uptoken.updateTime") - System.currentTimeMillis()
          < 1000L * 60 * 10) {
        return (Map) session.getAttribute("uptoken");
      }
    } catch (NullPointerException e) {
      //don't care
    }
    Map uptoken = qiniuService.getUptoken();
    session.setAttribute("uptoken.updateTime", System.currentTimeMillis());
    session.setAttribute("uptoken", uptoken);
    return uptoken;
  }

  @ApiOperation(value = "获取文件域名")
  @RequestMapping(value = "domain", method = RequestMethod.GET)
  public String getDomain() {
    return qiniuService.getDomain();
  }

  @ApiOperation(value = "文件列表")
  @RequestMapping(value = "filelist", method = RequestMethod.GET)
  public List getFileList(String prefix, HttpSession session) {
    try {
      if ((long) session.getAttribute("filelist." + prefix + ".updateTime") - System
          .currentTimeMillis()
          < 1000L * 60 * 10) {
        return (List) session.getAttribute("filelist." + prefix);
      }
    } catch (NullPointerException e) {
      //don't care
    }
    List filelist = qiniuService.getFileList(prefix);
    session.setAttribute("filelist." + prefix + ".updateTime", System.currentTimeMillis());
    session.setAttribute("filelist." + prefix, filelist);
    return filelist;
  }
}
