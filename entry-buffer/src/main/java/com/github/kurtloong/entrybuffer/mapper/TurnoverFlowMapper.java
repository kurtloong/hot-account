package com.github.kurtloong.entrybuffer.mapper;

import com.github.kurtloong.entrybuffer.domain.TurnoverFlow;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * The interface Turnover flow mapper.
 *
 * @Entity com.github.kurtloong.entrybuffer.domain.TurnoverFlow
 */
@Mapper
public interface TurnoverFlowMapper {

    /**
     * Delete by primary key int.
     *
     * @param id the id
     * @return the int
     */
    int deleteByPrimaryKey(Long id);

    /**
     * Insert int.
     *
     * @param record the record
     * @return the int
     */
    int insert(TurnoverFlow record);

    /**
     * Insert selective int.
     *
     * @param record the record
     * @return the int
     */
    int insertSelective(TurnoverFlow record);

    /**
     * Select by primary key turnover flow.
     *
     * @param id the id
     * @return the turnover flow
     */
    TurnoverFlow selectByPrimaryKey(Long id);

    /**
     * Update by primary key selective int.
     *
     * @param record the record
     * @return the int
     */
    int updateByPrimaryKeySelective(TurnoverFlow record);

    /**
     * Update by primary key int.
     *
     * @param record the record
     * @return the int
     */
    int updateByPrimaryKey(TurnoverFlow record);

    /**
     * 获取流水列表
     *
     * @param accountId the account id
     * @param status    the status
     * @return the list
     */
    List<TurnoverFlow> searchAllByAccountIdAndTurnoverFlowStatus(String accountId, boolean status);



}




