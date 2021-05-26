package com.github.kurtloong.entrybuffer.mapper;

import com.github.kurtloong.entrybuffer.domain.TurnoverFlow;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Entity com.github.kurtloong.entrybuffer.domain.TurnoverFlow
 */
@Mapper
public interface TurnoverFlowMapper {

    int deleteByPrimaryKey(Long id);

    int insert(TurnoverFlow record);

    int insertSelective(TurnoverFlow record);

    TurnoverFlow selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TurnoverFlow record);

    int updateByPrimaryKey(TurnoverFlow record);

}




