package cn.xuqplus.qiniuphotoalbum.controller;

import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api("七牛云相关操作")
@RestController
public class QiniuApiController {

  @Value("${qiniu.xuqplus.accessKey}")
  private String accessKey;

  @Value("${qiniu.xuqplus.secretKey}")
  private String secretKey;

  @Value("${qiniu.xuqplus.bucket}")
  private String bucket;

  @ApiOperation(value = "生成uptoken")
  @RequestMapping(value = "uptoken", method = RequestMethod.GET)
  public Map getUptoken() {
    return new HashMap() {{
      put("uptoken", Auth.create(accessKey, secretKey).uploadToken(bucket));
    }};
  }

  @ApiOperation(value = "文件列表")
  @RequestMapping(value = "filelist", method = RequestMethod.GET)
  public List getFileList(String prefix) {
    List<FileInfo> result = new ArrayList();
    Auth auth = Auth.create(accessKey, secretKey);
    BucketManager bucketManager = new BucketManager(auth, new Configuration(Zone.zone0()));
    int limit = 100;
    BucketManager.FileListIterator fileListIterator = bucketManager
        .createFileListIterator(bucket, prefix, limit, "");
    while (fileListIterator.hasNext()) {
      FileInfo[] items = fileListIterator.next();
      for (FileInfo item : items) {
        result.add(item);
      }
    }
    return result;
  }
}
