/**
 * qccr.com Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.toowell.official.common;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qccr.managerprod.facade.dto.CodeRo;

/**
 * 广告计划，业务-子业务关系
 * 1,燃油添加剂,1,燃油系统养护品
 * relaMap:{1:[{code:1-1,codeName:燃油系统养护品},{},...]}
 * bizList:[{code:1,codeName:燃油添加剂},...]
 * bizMap:{1:{code:1,codeName:燃油添加剂}}
 * subbizMap:{1-1:燃油系统养护品}
 * @author chensheng
 * @date 2017/2/20:50
 */
public class SubbizRela {

    private final static Map<String, List<CodeRo>> relaMap              = Maps.newHashMap();

    private final static Map<String, CodeRo>       subbizMap            = Maps.newHashMap();
    private final static Map<String, String>       subbizNameAndCodeMap = Maps.newHashMap();

    private final static List<CodeRo>              bizList              = Lists.newArrayList();

    private final static Map<String, CodeRo>       bizMap               = Maps.newHashMap();
    private final static Map<String, String>       bizNameAndCodeMap    = Maps.newHashMap();

    private final static Joiner                    joiner               = Joiner.on(",")
                                                                            .skipNulls();

    static {
        try {
            InputStream inputStream = SubbizRela.class.getClassLoader().getResourceAsStream(
                "subbiz.properties");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bReader = new BufferedReader(inputStreamReader);
            String temp = "";
            while ((temp = bReader.readLine()) != null) {
                String[] bizAndSubbiz = temp.split(",");
                if (!bizMap.containsKey(bizAndSubbiz[0])) {
                    CodeRo codeRo = new CodeRo();
                    codeRo.setCode(bizAndSubbiz[0]);
                    codeRo.setCodename(bizAndSubbiz[1]);
                    bizList.add(codeRo);
                    bizMap.put(bizAndSubbiz[0], codeRo);
                    bizNameAndCodeMap.put(bizAndSubbiz[1], bizAndSubbiz[0]);
                }
                CodeRo codeRo = new CodeRo();
                codeRo.setCode(bizAndSubbiz[2]);
                codeRo.setCodename(bizAndSubbiz[3]);
                if (!relaMap.containsKey(bizAndSubbiz[0])) {
                    List<CodeRo> codeRoList = Lists.newArrayList();
                    relaMap.put(bizAndSubbiz[0], codeRoList);
                }
                relaMap.get(bizAndSubbiz[0]).add(codeRo);
                subbizMap.put(bizAndSubbiz[2], codeRo);
                subbizNameAndCodeMap.put(bizAndSubbiz[3], bizAndSubbiz[2]);
            }

            bReader.close();
            inputStreamReader.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public final static List<CodeRo> getSubbizList() {
        String[] bizCodes = new String[bizMap.keySet().size()];
        if (bizCodes.length == 0) {
            return Lists.newArrayList();
        }
        return getSubbizList(bizMap.keySet().toArray(bizCodes));
    }

    public final static List<CodeRo> getSubbizList(String[] bizCodes) {
        List<CodeRo> codeRoList = Lists.newArrayList();
        if (bizCodes == null || bizCodes.length == 0) {
            return getSubbizList();
        }
        for (String bizCode : bizCodes) {
            codeRoList.add(bizMap.get(bizCode));
            codeRoList.addAll(relaMap.get(bizCode));
        }
        return codeRoList;
    }

    public final static List<CodeRo> getBizList() {
        return bizList;
    }

    public final static CodeRo getBiz(String code) {
        return bizMap.get(code);
    }

    public final static String getBizNames(String codes) {
        if (StringUtils.isBlank(codes)) {
            return "";
        }
        String[] codeArr = codes.split(",");
        String[] nameArr = new String[codeArr.length];
        for (int i = 0, len = codeArr.length; i < len; i++) {
            nameArr[i] = bizMap.get(codeArr[i]).getCodename();
        }
        return joiner.join(nameArr);
    }

    public final static CodeRo getSubbiz(String code) {
        return subbizMap.get(code);
    }

    public final static String getSubbizNames(String codes) {
        if (StringUtils.isBlank(codes)) {
            return "";
        }
        String[] codeArr = codes.split(",");
        String[] nameArr = new String[codeArr.length];
        for (int i = 0, len = codeArr.length; i < len; i++) {
            nameArr[i] = subbizMap.get(codeArr[i]).getCodename();
        }
        return joiner.join(nameArr);
    }

    public final static String getSubbizCodesByName(String names) {
        if (StringUtils.isBlank(names)) {
            return "";
        }
        String[] nameArr = names.split(",");
        String[] codeArr = new String[nameArr.length];
        for (int i = 0, len = nameArr.length; i < len; i++) {
            codeArr[i] = subbizNameAndCodeMap.get(nameArr[i]);
        }
        return joiner.join(codeArr);
    }

    public final static String getBizCodesByName(String names) {
        if (StringUtils.isBlank(names)) {
            return "";
        }
        String[] nameArr = names.split(",");
        String[] codeArr = new String[nameArr.length];
        for (int i = 0, len = nameArr.length; i < len; i++) {
            codeArr[i] = bizNameAndCodeMap.get(nameArr[i]);
        }
        return joiner.join(codeArr);
    }
}
