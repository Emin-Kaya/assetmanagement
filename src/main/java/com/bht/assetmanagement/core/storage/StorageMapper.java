package com.bht.assetmanagement.core.storage;

import com.bht.assetmanagement.persistence.dto.StorageResponse;
import com.bht.assetmanagement.persistence.dto.StorageDto;
import com.bht.assetmanagement.persistence.entity.Storage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StorageMapper {
    StorageMapper INSTANCE = Mappers.getMapper(StorageMapper.class);

    @Mapping(source = "assets", target = "assetDtos")
    StorageResponse mapEntityToStorageResponse(Storage storage);

    StorageDto mapEntityToStorageDto(Storage storage);

    Storage mapStoragRequestToEntity(String name);
}
