package com.prac.onlinesql.mapper;

import com.prac.onlinesql.qo.BaseQO;
import com.prac.onlinesql.vo.TableVO;

import java.util.List;

/**
 * 接口描述:
 * 作者: 李宇
 * 创建时间:16:38 2018/11/11
 **/
public interface TableMapper {

    List<TableVO> getTable(BaseQO qo);

    int queryTableTotal(BaseQO qo);
}
