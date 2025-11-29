package com.example.bulkupload.mapper;

import com.example.bulkupload.dto.SmaLeadSecondDto;
import com.example.bulkupload.entity.SmaLeadSecond;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SmaLeadMapper {

    SmaLeadMapper SMA_LEAD_MAPPER = Mappers.getMapper(SmaLeadMapper.class);

    SmaLeadSecondDto mappedToSmaLeadSecondDto(SmaLeadSecond smaLeadSecond);

    SmaLeadSecond mappedToSmaLeadSecond(SmaLeadSecondDto smaLeadSecondDto);
}
