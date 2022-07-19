package com.bht.assetmanagement.core.storage;

import com.bht.assetmanagement.persistence.dto.StorageDto;
import com.bht.assetmanagement.persistence.dto.StorageEmployeeDto;
import com.bht.assetmanagement.persistence.entity.Storage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StorageMapper {
    StorageMapper INSTANCE = Mappers.getMapper(StorageMapper.class);

    @Mapping(source = "assets", target = "assetDtos")
    StorageDto mapEntityToStorageDto(Storage storage);

    StorageEmployeeDto mapEntityToStorageEmployeeDto(Storage storage);

    Storage mapStoragRequestToEntity(String name);
}
