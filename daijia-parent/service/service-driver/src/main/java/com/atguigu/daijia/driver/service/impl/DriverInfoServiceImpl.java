package com.atguigu.daijia.driver.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.atguigu.daijia.common.constant.SystemConstant;
import com.atguigu.daijia.common.execption.GuiguException;
import com.atguigu.daijia.common.result.ResultCodeEnum;
import com.atguigu.daijia.driver.config.TencentCloudProperties;
import com.atguigu.daijia.driver.mapper.*;
import com.atguigu.daijia.driver.service.CosService;
import com.atguigu.daijia.driver.service.DriverInfoService;
import com.atguigu.daijia.model.entity.driver.*;
import com.atguigu.daijia.model.form.driver.DriverFaceModelForm;
import com.atguigu.daijia.model.form.driver.UpdateDriverAuthInfoForm;
import com.atguigu.daijia.model.vo.base.PageVo;
import com.atguigu.daijia.model.vo.driver.DriverAuthInfoVo;
import com.atguigu.daijia.model.vo.driver.DriverInfoVo;
import com.atguigu.daijia.model.vo.driver.DriverLoginVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tencentcloudapi.common.AbstractModel;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.iai.v20200303.IaiClient;
import com.tencentcloudapi.iai.v20200303.models.*;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

@Slf4j
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class DriverInfoServiceImpl extends ServiceImpl<DriverInfoMapper, DriverInfo> implements DriverInfoService {

    @Autowired
    private WxMaService wxMaService;

    @Autowired
    private DriverInfoMapper driverInfoMapper;

    @Autowired
    private DriverSetMapper driverSetMapper;

    @Autowired
    private DriverAccountMapper driverAccountMapper;

    @Autowired
    private DriverLoginLogMapper driverLoginLogMapper;

    @Autowired
    private DriverFaceRecognitionMapper driverFaceRecognitionMapper;

    @Autowired
    private CosService cosService;

    @Autowired
    private TencentCloudProperties tencentCloudProperties;

    //小程序授权登录
    @Override
    public Long login(String code) {
        try {
            //根据code + 小程序id + 秘钥请求微信接口，返回openid
            WxMaJscode2SessionResult sessionInfo =
                    wxMaService.getUserService().getSessionInfo(code);
            String openid = sessionInfo.getOpenid();

            //根据openid查询是否第一次登录
            LambdaQueryWrapper<DriverInfo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DriverInfo::getWxOpenId,openid);
            DriverInfo driverInfo = driverInfoMapper.selectOne(wrapper);

            if(driverInfo == null) {
                //添加司机基本信息
                driverInfo = new DriverInfo();
                driverInfo.setNickname(String.valueOf(System.currentTimeMillis()));
                driverInfo.setAvatarUrl("https://hire1.oss-cn-beijing.aliyuncs.com/recruitment/15046733-1cc6-4801-85d6-07dc70aa2458.jpg");
                driverInfo.setWxOpenId(openid);
                driverInfoMapper.insert(driverInfo);

                //初始化司机设置
                DriverSet driverSet = new DriverSet();
                driverSet.setDriverId(driverInfo.getId());
                driverSet.setOrderDistance(new BigDecimal(0));//0：无限制
                driverSet.setAcceptDistance(new BigDecimal(SystemConstant.ACCEPT_DISTANCE));//默认接单范围：5公里
                driverSet.setIsAutoAccept(0);//0：否 1：是
                driverSetMapper.insert(driverSet);

                //初始化司机账户信息
                DriverAccount driverAccount = new DriverAccount();
                driverAccount.setDriverId(driverInfo.getId());
                driverAccountMapper.insert(driverAccount);
            }

            //记录司机登录信息
            DriverLoginLog driverLoginLog = new DriverLoginLog();
            driverLoginLog.setDriverId(driverInfo.getId());
            driverLoginLog.setMsg("小程序登录");
            driverLoginLogMapper.insert(driverLoginLog);

            //返回司机id
            return driverInfo.getId();
        } catch (WxErrorException e) {
            throw new GuiguException(ResultCodeEnum.DATA_ERROR);
        }
    }

    //获取司机登录信息
    @Override
    public DriverLoginVo getDriverInfo(Long driverId) {
        //根据司机id获取司机信息
        DriverInfo driverInfo = driverInfoMapper.selectById(driverId);

        //driverInfo -- DriverLoginVo
        DriverLoginVo driverLoginVo = new DriverLoginVo();
        BeanUtils.copyProperties(driverInfo,driverLoginVo);

        //是否建档人脸识别
        String faceModelId = driverInfo.getFaceModelId();
        boolean isArchiveFace = StringUtils.hasText(faceModelId);
        driverLoginVo.setIsArchiveFace(isArchiveFace);
        return driverLoginVo;
    }

    //获取司机认证信息
    @Override
    public DriverAuthInfoVo getDriverAuthInfo(Long driverId) {
        DriverInfo driverInfo = driverInfoMapper.selectById(driverId);
        DriverAuthInfoVo driverAuthInfoVo = new DriverAuthInfoVo();
        BeanUtils.copyProperties(driverInfo,driverAuthInfoVo);

        driverAuthInfoVo.setIdcardBackShowUrl(cosService.getImageUrl(driverAuthInfoVo.getIdcardBackUrl()));
        driverAuthInfoVo.setIdcardFrontShowUrl(cosService.getImageUrl(driverAuthInfoVo.getIdcardFrontUrl()));
        driverAuthInfoVo.setIdcardHandShowUrl(cosService.getImageUrl(driverAuthInfoVo.getIdcardHandUrl()));
        driverAuthInfoVo.setDriverLicenseFrontShowUrl(cosService.getImageUrl(driverAuthInfoVo.getDriverLicenseFrontUrl()));
        driverAuthInfoVo.setDriverLicenseBackShowUrl(cosService.getImageUrl(driverAuthInfoVo.getDriverLicenseBackUrl()));
        driverAuthInfoVo.setDriverLicenseHandShowUrl(cosService.getImageUrl(driverAuthInfoVo.getDriverLicenseHandUrl()));

        return driverAuthInfoVo;
    }

    //更新司机认证信息
    @Override
    public Boolean updateDriverAuthInfo(UpdateDriverAuthInfoForm updateDriverAuthInfoForm) {
        //获取司机id
        Long driverId = updateDriverAuthInfoForm.getDriverId();

        //修改操作
        DriverInfo driverInfo = new DriverInfo();
        driverInfo.setId(driverId);
        BeanUtils.copyProperties(updateDriverAuthInfoForm,driverInfo);

//        int i = driverInfoMapper.updateById(driverInfo);
        boolean update = this.updateById(driverInfo);
        return update;
    }

    //创建司机人脸模型
//    @Override
//    public Boolean creatDriverFaceModel(DriverFaceModelForm driverFaceModelForm) {
//        //根据司机id获取司机信息
//        DriverInfo driverInfo =
//                driverInfoMapper.selectById(driverFaceModelForm.getDriverId());
//        try{
//
//            // 实例化一个认证对象，入参需要传入腾讯云账户 SecretId 和 SecretKey，此处还需注意密钥对的保密
//            // 代码泄露可能会导致 SecretId 和 SecretKey 泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考，建议采用更安全的方式来使用密钥，请参见：https://cloud.tencent.com/document/product/1278/85305
//            // 密钥可前往官网控制台 https://console.cloud.tencent.com/cam/capi 进行获取
//            Credential cred = new Credential(tencentCloudProperties.getSecretId(),
//                                            tencentCloudProperties.getSecretKey());
//            // 实例化一个http选项，可选的，没有特殊需求可以跳过
//            HttpProfile httpProfile = new HttpProfile();
//            httpProfile.setEndpoint("iai.tencentcloudapi.com");
//            // 实例化一个client选项，可选的，没有特殊需求可以跳过
//            ClientProfile clientProfile = new ClientProfile();
//            clientProfile.setHttpProfile(httpProfile);
//            // 实例化要请求产品的client对象,clientProfile是可选的
//            IaiClient client = new IaiClient(cred, tencentCloudProperties.getRegion(),
//                                                     clientProfile);
//            // 实例化一个请求对象,每个接口都会对应一个request对象
//            CreatePersonRequest req = new CreatePersonRequest();
//            //设置相关值
//            req.setGroupId(tencentCloudProperties.getPersionGroupId());
//            //基本信息
//            req.setPersonId(String.valueOf(driverInfo.getId()));
//            req.setGender(Long.parseLong(driverInfo.getGender()));
//            req.setQualityControl(4L);
//            req.setUniquePersonControl(4L);
//            req.setPersonName(driverInfo.getName());
//            req.setImage(driverFaceModelForm.getImageBase64());
//
//            // 返回的resp是一个CreatePersonResponse的实例，与请求对象对应
//            CreatePersonResponse resp = client.CreatePerson(req);
//            // 输出json格式的字符串回包
//            System.out.println(AbstractModel.toJsonString(resp));
//            String faceId = resp.getFaceId();
//            if(StringUtils.hasText(faceId)) {
//                driverInfo.setFaceModelId(faceId);
//                driverInfoMapper.updateById(driverInfo);
//            }
//        } catch (TencentCloudSDKException e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
    @Override
    public Boolean creatDriverFaceModel(DriverFaceModelForm driverFaceModelForm) {
        DriverInfo driverInfo = driverInfoMapper.selectById(driverFaceModelForm.getDriverId());
        // 模拟人脸模型创建成功
        driverInfo.setFaceModelId("mock_face_id_" + System.currentTimeMillis());
        driverInfoMapper.updateById(driverInfo);
        return true;
    }

    //获取司机设置信息
    @Override
    public DriverSet getDriverSet(Long driverId) {
        LambdaQueryWrapper<DriverSet> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DriverSet::getDriverId,driverId);
        DriverSet driverSet = driverSetMapper.selectOne(wrapper);
        return driverSet;
    }

    //判断司机当日是否进行过人脸识别
//    @Override
//    public Boolean isFaceRecognition(Long driverId) {
//        //根据司机id + 当日日期进行查询
//        LambdaQueryWrapper<DriverFaceRecognition> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(DriverFaceRecognition::getDriverId,driverId);
//        // 年-月-日 格式
//        wrapper.eq(DriverFaceRecognition::getFaceDate,new DateTime().toString("yyyy-MM-dd"));
//        //调用mapper方法
//        Long count = driverFaceRecognitionMapper.selectCount(wrapper);
//
//        return count != 0;
//    }
    @Override
    public Boolean isFaceRecognition(Long driverId) {
        return true;
    }
    //人脸识别
//    @Override
//    public Boolean verifyDriverFace(DriverFaceModelForm driverFaceModelForm) {
//        //1 照片比对
//        try{
//            // 实例化一个认证对象，入参需要传入腾讯云账户 SecretId 和 SecretKey，此处还需注意密钥对的保密
//            // 代码泄露可能会导致 SecretId 和 SecretKey 泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考，建议采用更安全的方式来使用密钥，请参见：https://cloud.tencent.com/document/product/1278/85305
//            // 密钥可前往官网控制台 https://console.cloud.tencent.com/cam/capi 进行获取
//            Credential cred = new Credential(tencentCloudProperties.getSecretId(),
//                    tencentCloudProperties.getSecretKey());
//            // 实例化一个http选项，可选的，没有特殊需求可以跳过
//            HttpProfile httpProfile = new HttpProfile();
//            httpProfile.setEndpoint("iai.tencentcloudapi.com");
//            // 实例化一个client选项，可选的，没有特殊需求可以跳过
//            ClientProfile clientProfile = new ClientProfile();
//            clientProfile.setHttpProfile(httpProfile);
//
//            // 实例化要请求产品的client对象,clientProfile是可选的
//            IaiClient client = new IaiClient(cred,
//                                         tencentCloudProperties.getRegion(),
//                                         clientProfile);
//            // 实例化一个请求对象,每个接口都会对应一个request对象
//            VerifyFaceRequest req = new VerifyFaceRequest();
//            //设置相关参数
//            req.setImage(driverFaceModelForm.getImageBase64());
//            req.setPersonId(String.valueOf(driverFaceModelForm.getDriverId()));
//
//            // 返回的resp是一个VerifyFaceResponse的实例，与请求对象对应
//            VerifyFaceResponse resp = client.VerifyFace(req);
//            // 输出json格式的字符串回包
//            System.out.println(AbstractModel.toJsonString(resp));
//            if(resp.getIsMatch()) { //照片比对成功
//                //2 如果照片比对成功，静态活体检测
//                Boolean isSuccess = this.
//                        detectLiveFace(driverFaceModelForm.getImageBase64());
//                if(isSuccess) {//3 如果静态活体检测通过，添加数据到认证表里面
//                    DriverFaceRecognition driverFaceRecognition = new DriverFaceRecognition();
//                    driverFaceRecognition.setDriverId(driverFaceModelForm.getDriverId());
//                    driverFaceRecognition.setFaceDate(new Date());
//                    driverFaceRecognitionMapper.insert(driverFaceRecognition);
//                    return true;
//                }
//            }
//        } catch (TencentCloudSDKException e) {
//            System.out.println(e.toString());
//        }
//
//        throw new GuiguException(ResultCodeEnum.DATA_ERROR);
//    }
    @Override
    public Boolean verifyDriverFace(DriverFaceModelForm driverFaceModelForm) {
        // 直接记录认证记录
        DriverFaceRecognition driverFaceRecognition = new DriverFaceRecognition();
        driverFaceRecognition.setDriverId(driverFaceModelForm.getDriverId());
        driverFaceRecognition.setFaceDate(new Date());
        driverFaceRecognitionMapper.insert(driverFaceRecognition);
        return true;
    }
    //更新接单状态
    // update driver_set set status=? where driver_id=?
    @Override
    public Boolean updateServiceStatus(Long driverId, Integer status) {
        LambdaQueryWrapper<DriverSet> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DriverSet::getDriverId,driverId);
        DriverSet driverSet = new DriverSet();
        driverSet.setServiceStatus(status);
        driverSetMapper.update(driverSet,wrapper);
        return true;
    }

    //获取司机基本信息
    @Override
    public DriverInfoVo getDriverInfoOrder(Long driverId) {
        //司机id获取基本信息
        DriverInfo driverInfo = driverInfoMapper.selectById(driverId);

        //封装DriverInfoVo
        DriverInfoVo driverInfoVo = new DriverInfoVo();
        BeanUtils.copyProperties(driverInfo,driverInfoVo);

        //计算驾龄
        //获取当前年
        int currentYear = new DateTime().getYear();
        //获取驾驶证初次领证日期
        //driver_license_issue_date
        int firstYear = new DateTime(driverInfo.getDriverLicenseIssueDate()).getYear();
        int driverLicenseAge = currentYear - firstYear;
        driverInfoVo.setDriverLicenseAge(driverLicenseAge);

        return driverInfoVo;
    }

    @Override
    public String getDriverOpenId(Long driverId) {
        DriverInfo driverInfo = this.getOne(new LambdaQueryWrapper<DriverInfo>().eq(DriverInfo::getId, driverId).select(DriverInfo::getWxOpenId));
        return driverInfo.getWxOpenId();
    }

    //人脸静态活体检测
//    private Boolean detectLiveFace(String imageBase64) {
//        try{
//            // 实例化一个认证对象，入参需要传入腾讯云账户 SecretId 和 SecretKey，此处还需注意密钥对的保密
//            // 代码泄露可能会导致 SecretId 和 SecretKey 泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考，建议采用更安全的方式来使用密钥，请参见：https://cloud.tencent.com/document/product/1278/85305
//            // 密钥可前往官网控制台 https://console.cloud.tencent.com/cam/capi 进行获取
//            Credential cred = new Credential(tencentCloudProperties.getSecretId(),
//                    tencentCloudProperties.getSecretKey());
//            // 实例化一个http选项，可选的，没有特殊需求可以跳过
//            HttpProfile httpProfile = new HttpProfile();
//            httpProfile.setEndpoint("iai.tencentcloudapi.com");
//            // 实例化一个client选项，可选的，没有特殊需求可以跳过
//            ClientProfile clientProfile = new ClientProfile();
//            clientProfile.setHttpProfile(httpProfile);
//            // 实例化要请求产品的client对象,clientProfile是可选的
//            IaiClient client = new IaiClient(cred, tencentCloudProperties.getRegion(),
//                    clientProfile);
//            // 实例化一个请求对象,每个接口都会对应一个request对象
//            DetectLiveFaceRequest req = new DetectLiveFaceRequest();
//            req.setImage(imageBase64);
//            // 返回的resp是一个DetectLiveFaceResponse的实例，与请求对象对应
//            DetectLiveFaceResponse resp = client.DetectLiveFace(req);
//            // 输出json格式的字符串回包
//            System.out.println(DetectLiveFaceResponse.toJsonString(resp));
//            if(resp.getIsLiveness()) {
//                return true;
//            }
//        } catch (TencentCloudSDKException e) {
//            System.out.println(e.toString());
//        }
//        return false;
//    }
    private Boolean detectLiveFace(String imageBase64) {
        return true;
    }

    // ==================== 管理端API实现 ====================

    @Override
    public PageVo<DriverInfo> findDriverInfoPage(Long page, Long limit) {
        Page<DriverInfo> pageParam = new Page<>(page, limit);
        Page<DriverInfo> pageInfo = page(pageParam);

        PageVo<DriverInfo> pageVo = new PageVo<>();
        pageVo.setPage(page);
        pageVo.setLimit(limit);
        pageVo.setTotal(pageInfo.getTotal());
        pageVo.setPages(pageInfo.getPages());
        pageVo.setRecords(pageInfo.getRecords());

        return pageVo;
    }

    @Override
    public PageVo<DriverInfo> findDriverInfoPageByName(Long page, Long limit, String name) {
        Page<DriverInfo> pageParam = new Page<>(page, limit);

        LambdaQueryWrapper<DriverInfo> queryWrapper = new LambdaQueryWrapper<>();
        if (name != null && !name.trim().isEmpty()) {
            queryWrapper.like(DriverInfo::getName, name);
        }

        Page<DriverInfo> pageInfo = page(pageParam, queryWrapper);

        PageVo<DriverInfo> pageVo = new PageVo<>();
        pageVo.setPage(page);
        pageVo.setLimit(limit);
        pageVo.setTotal(pageInfo.getTotal());
        pageVo.setPages(pageInfo.getPages());
        pageVo.setRecords(pageInfo.getRecords());

        return pageVo;
    }

    @Override
    public PageVo<DriverInfo> findDriverInfoPageByAuthStatus(Long page, Long limit, Integer authStatus) {
        Page<DriverInfo> pageParam = new Page<>(page, limit);

        LambdaQueryWrapper<DriverInfo> queryWrapper = new LambdaQueryWrapper<>();
        if (authStatus != null) {
            queryWrapper.eq(DriverInfo::getAuthStatus, authStatus);
        }

        Page<DriverInfo> pageInfo = page(pageParam, queryWrapper);

        PageVo<DriverInfo> pageVo = new PageVo<>();
        pageVo.setPage(page);
        pageVo.setLimit(limit);
        pageVo.setTotal(pageInfo.getTotal());
        pageVo.setPages(pageInfo.getPages());
        pageVo.setRecords(pageInfo.getRecords());

        return pageVo;
    }

    @Override
    public Boolean updateDriverStatus(Long id, Integer status) {
        DriverInfo driverInfo = new DriverInfo();
        driverInfo.setId(id);
        driverInfo.setStatus(status);
        return updateById(driverInfo);
    }
}
//    }
//}