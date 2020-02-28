package com.tmqt.web;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import com.tmqt.dto.RestResult;
import com.tmqt.dto.RestResultBuilder;
import com.tmqt.model.Productplan;
import com.tmqt.service.ProductPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by chenw on 2018/12/19.
 */
@Controller
@RequestMapping
public class ProductPlanController {
    @Autowired
    private ProductPlanService productPlanService;

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
     * 添加产品安排
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addProductPlan", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public RestResult addProductPlan(@RequestParam("imgFile") MultipartFile file, String code,
                                     String material, String insideFabric, String bottomMaterial, String type,
                                     String color, String factory, String cost, Float price, Integer titleState,
                                     String brand, Integer takeLength, Date dcTime, Date alTime, Date kdTime, Date tmTime,
                                     Date prePhotoTime,Integer dcPre, Integer alPre, Integer kdPre, Integer tmPre, String remarks){
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
            String key = "/productplan/"+year+"/"+month+"/"+day+"/"+newFileName;
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
            PutObjectResult putObjectResult = cosclient.putObject(putObjectRequest);

            Productplan productplan = new Productplan();
            productplan.setCode(code);
            productplan.setImage("http://"+this.path+putObjectRequest.getKey());
            productplan.setMaterial(material);
            productplan.setBottommaterial(bottomMaterial);
            productplan.setInsidefabric(insideFabric);
            productplan.setType(type);
            productplan.setColor(color);
            productplan.setFactory(factory);
            productplan.setCost(cost);
            productplan.setPrice(price);
            productplan.setTitlestate(titleState);
            productplan.setBrand(brand);
            productplan.setTakelength(takeLength);
            productplan.setDctime(dcTime);
            productplan.setAltime(alTime);
            productplan.setKdtime(kdTime);
            productplan.setTmtime(tmTime);
            productplan.setPrephototime(prePhotoTime);
            productplan.setDcpre(dcPre);
            productplan.setAlpre(alPre);
            productplan.setKdpre(kdPre);
            productplan.setTmpre(tmPre);
            productplan.setRemarks(remarks);

            int addRes = productPlanService.addProductPlan(productplan);

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

//        return productPlanService.addProductPlan(productplan);
    }

    /**
     * 删除产品安排
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delProductPlan", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public int delProductPlan(int id){
        Productplan productplan = productPlanService.findProductPlanById(id);
        int index = productplan.getImage().indexOf("com/");
        String key = productplan.getImage().substring(index+4);
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
            return productPlanService.deleteProductPlan(id);
        } catch (CosClientException cle) {
            System.out.print(cle.getMessage());
            return 0;
        }finally {
            // 关闭客户端(关闭后台线程)
            cosclient.shutdown();
        }
    }

    /**
     * 修改产品安排
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateProductPlan", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public int updateProductPlan(@RequestBody Productplan productplan){
        return productPlanService.updateProductPlan(productplan);
    }

    @ResponseBody
    @RequestMapping(value = "/updateProductPlanImage", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public int updateProductPlanImage(@RequestParam("imgFile") MultipartFile file, int id){

        if(file == null){
            return -1;
        }

        Productplan productplan = productPlanService.findProductPlanById(id);
        int index = productplan.getImage().indexOf("com/");
        String delKey = productplan.getImage().substring(index+4);
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
            String addKey = "/productplan/"+year+"/"+month+"/"+day+"/"+newFileName;
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, addKey, localFile);
            PutObjectResult putObjectResult = cosclient.putObject(putObjectRequest);
            Productplan updateProductPlan = new Productplan();
            updateProductPlan.setId(id);
            updateProductPlan.setImage("http://"+this.path+putObjectRequest.getKey());

            return productPlanService.updateProductPlan(updateProductPlan);
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
     * 获取产品安排列表分页
     * @return
     */
    @RequestMapping(value = "/getProductPlanList", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public @ResponseBody
    RestResult getProductPlanList(@RequestParam("page") int pageNum, @RequestParam("limit") int pageSize){
        List<Productplan> dataList = productPlanService.findPageProductPlan(pageNum,pageSize);
        int count = productPlanService.countProductPlan();
        return new RestResultBuilder().setCode(0)
                .setMsg("")
                .setCount(count)
                .setData(dataList)
                .build();
    }

    /**
     * 查找产品安排
     * @return
     */
    @RequestMapping(value = "/findProductPlanByCode", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public @ResponseBody
    RestResult findProductPlanByCode(@RequestParam("code") String code){
        List<Productplan> dataList = productPlanService.findProductPlanByCode(code);
        return new RestResultBuilder().setCode(0)
                .setCount(dataList.size())
                .setMsg("")
                .setData(dataList)
                .build();
    }

    /**
     * 筛选产品安排
     * @return
     */
    @RequestMapping(value = "/findProductPlan", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public @ResponseBody
    RestResult findProductPlan(@RequestParam Map conditions){
        List<Productplan> dataList = productPlanService.findProductplanByIf(conditions);
        return new RestResultBuilder().setCode(0)
                .setCount(dataList.size())
                .setMsg("")
                .setData(dataList)
                .build();
    }

}
