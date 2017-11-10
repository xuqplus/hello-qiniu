package cn.xuqplus.qiniuphotoalbum.service;

import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class QiniuService {

  @Value("${qiniu.accessKey}")
  private String accessKey;

  @Value("${qiniu.secretKey}")
  private String secretKey;

  @Value("${qiniu.bucket.xuqplus}")
  private String bucket;

  @Value("${qiniu.bucket.xuqplus.domain}")
  private String domain;

  /**
   * 获取uptoken
   */
  public Map getUptoken() {
    return new HashMap() {{
      put("uptoken", Auth.create(accessKey, secretKey).uploadToken(bucket));
    }};
  }

  /**
   * 获取域名
   */
  public String getDomain() {
    return domain;
  }

  /**
   * 获取文件列表
   */
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
