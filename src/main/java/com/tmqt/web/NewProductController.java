package com.tmqt.web;


import com.qcloud.cos.exception.CosClientException;
import com.tmqt.dto.RestResult;
import com.tmqt.dto.RestResultBuilder;
import com.tmqt.model.Newproduct;
import com.tmqt.service.NewProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;

import java.util.*;

/**
 * Created by chenw on 2018/12/19.
 */
@Controller
@RequestMapping
public class NewProductController {
    @Autowired
    private NewProductService newProductService;

    @Value("${spring.tengxun.secretId}")
    private String secretId;
    @Value("${spring.tengxun.secretKey}")
    private String secretKey;
    @Value("${spring.tengxun.bucket}")
    private String bucket;
    @Value("${spring.tengxun.bucketName}")
    private String bucketName;
    @Value("${spring.tengxun.path}")
    private String path;
    @Value("${spring.tengxun.pathHead}")
    private String pathHead;

    /**
     * 添加新品推广
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addNewProduct", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public RestResult addNewProduct(@RequestParam("imgFile") MultipartFile file, String code, Date startTime, int videoState, int detailImageState, int buyerShowState, int testClickState, int stockState, String remarks){
//        String addCode = code;
//        Date addStartTime = startTime;
//        int addVideoState = videoState;
//        int addDetailImageState = detailImageState;
//        int addBuyerShowState = buyerShowState;
//        int addTestClickState = testClickState;
//        int addStockState = stockState;
//        System.out.println(addStartTime+"+"+addCode+"+"+addVideoState+"+"+addDetailImageState+"+"+addBuyerShowState+"+"+addTestClickState+"+"+addStockState+"+");

        if(file == null){
            return new RestResultBuilder().setCode(500)
                    .setMsg("文件上传失败")
                    .build();
        }
        String oldFileName = file.getOriginalFilename();
        String eName = oldFileName.substring(oldFileName.lastIndexOf("."));
        String newFileName = UUID.randomUUID()+eName;
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        int day=cal.get(Calendar.DATE);
        // 1 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region(bucket));
        // 3 生成cos客户端
        COSClient cosclient = new COSClient(cred, clientConfig);
        // bucket的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
        String bucketName = this.bucketName;

        // 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20 M 以下的文件使用该接口
        // 大文件上传请参照 API 文档高级 API 上传
        File localFile = null;
        try {
            localFile = File.createTempFile("temp",null);
            file.transferTo(localFile);
            // 指定要上传到 COS 上的路径
            String key = "/newproduct/"+year+"/"+month+"/"+day+"/"+newFileName;
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
            PutObjectResult putObjectResult = cosclient.putObject(putObjectRequest);

            Newproduct newproduct = new Newproduct();
            newproduct.setCode(code);
            newproduct.setStarttime(startTime);
            newproduct.setImage("http://"+this.path+putObjectRequest.getKey());
            newproduct.setVideostate(videoState);
            newproduct.setDetailimagestate(detailImageState);
            newproduct.setBuyershowstate(buyerShowState);
            newproduct.setTestclickstate(testClickState);
            newproduct.setStockstate(stockState);
            newproduct.setRemarks(remarks);

            int addRes = newProductService.addNewProduct(newproduct);

            if(addRes == 1){
                return new RestResultBuilder().setCode(addRes)
                        .setMsg("添加成功")
                        .build();
            }else{
                return new RestResultBuilder().setCode(-1)
                        .setMsg("添加失败"+addRes)
                        .build();
            }


        } catch (IOException e) {
            return new RestResultBuilder().setCode(-1)
                    .setMsg(e.getMessage())
                    .build();
        }finally {
            // 关闭客户端(关闭后台线程)
            cosclient.shutdown();
        }
    }

    /**
     * 删除新品推广
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delNewProduct", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public int delNewProduct(int id){
        Newproduct newproduct = newProductService.findNewProductById(id);
        int index = newproduct.getImage().indexOf("com/");
        String key = newproduct.getImage().substring(index+4);
//        System.out.print("key is this:"+key);
        // 1 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region(bucket));
        // 3 生成cos客户端
        COSClient cosclient = new COSClient(cred, clientConfig);
        // bucket的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
        String bucketName = this.bucketName;
        try {
            cosclient.deleteObject(bucketName, key);
            return newProductService.deleteNewProduct(id);
        } catch (CosClientException cle) {
            System.out.print(cle.getMessage());
            return 0;
        }finally {
            // 关闭客户端(关闭后台线程)
            cosclient.shutdown();
        }

    }

    /**
     * 修改新品推广
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateNewProduct", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public int updateNewProduct(@RequestBody Newproduct newproduct){
        return newProductService.updateNewProduct(newproduct);
    }

    @ResponseBody
    @RequestMapping(value = "/updateNewProductImage", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public int updateNewProductImage(@RequestParam("imgFile") MultipartFile file, int id){

        if(file == null){
            return -1;
        }

        Newproduct newproduct = newProductService.findNewProductById(id);
        int index = newproduct.getImage().indexOf("com/");
        String delKey = newproduct.getImage().substring(index+4);
//        System.out.print("key is this:"+key);
        // 1 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region(bucket));
        // 3 生成cos客户端
        COSClient cosclient = new COSClient(cred, clientConfig);
        // bucket的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
        String bucketName = this.bucketName;

        String oldFileName = file.getOriginalFilename();
        String eName = oldFileName.substring(oldFileName.lastIndexOf("."));
        String newFileName = UUID.randomUUID()+eName;
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        int day=cal.get(Calendar.DATE);

        // 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20 M 以下的文件使用该接口
        // 大文件上传请参照 API 文档高级 API 上传
        File localFile = null;

        try {
            cosclient.deleteObject(bucketName, delKey);
            localFile = File.createTempFile("temp",null);
            file.transferTo(localFile);
            // 指定要上传到 COS 上的路径
            String addKey = "/newproduct/"+year+"/"+month+"/"+day+"/"+newFileName;
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, addKey, localFile);
            PutObjectResult putObjectResult = cosclient.putObject(putObjectRequest);
            Newproduct updateNewproduct = new Newproduct();
            updateNewproduct.setId(id);
            updateNewproduct.setImage("http://"+this.path+putObjectRequest.getKey());

            return newProductService.updateNewProduct(updateNewproduct);
        } catch (CosClientException cle) {
            System.out.print(cle.getMessage());
            return 0;
        } catch (IOException e) {
            return 0;
        }
        finally {
            // 关闭客户端(关闭后台线程)
            cosclient.shutdown();
        }
    }

    /**
     * 获取新品推广列表分页
     * @return
     */
    @RequestMapping(value = "/getNewProductList", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public @ResponseBody
    RestResult getNewProductList(@RequestParam("page") int pageNum, @RequestParam("limit") int pageSize){
        List<Newproduct> dataList = newProductService.findPageNewProduct(pageNum,pageSize);
        int count = newProductService.countNewProduct();
        return new RestResultBuilder().setCode(0)
                .setMsg("")
                .setCount(count)
                .setData(dataList)
                .build();
    }

    /**
     * 查找新品推广
     * @return
     */
    @RequestMapping(value = "/findNewProductByCode", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public @ResponseBody
    RestResult findNewProductByCode(@RequestParam("code") String code){
        List<Newproduct> dataList = newProductService.findNewProductByCode(code);
        return new RestResultBuilder().setCode(0)
                .setCount(dataList.size())
                .setMsg("")
                .setData(dataList)
                .build();
    }

    /**
     * 筛选新品推广
     * @return
     */
    @RequestMapping(value = "/findNewProduct", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public @ResponseBody
    RestResult findNewProduct(@RequestParam Map conditions){
        List<Newproduct> dataList = newProductService.findNewproductByIf(conditions);
        return new RestResultBuilder().setCode(0)
                .setCount(dataList.size())
                .setMsg("")
                .setData(dataList)
                .build();
    }

}
