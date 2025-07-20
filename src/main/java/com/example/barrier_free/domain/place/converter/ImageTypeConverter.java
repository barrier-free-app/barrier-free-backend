package com.example.barrier_free.domain.place.converter;

import com.example.barrier_free.domain.place.enums.ImageType;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ImageTypeConverter implements AttributeConverter<ImageType, Integer> {

	@Override
	public Integer convertToDatabaseColumn(ImageType attribute) {
		if (attribute == null)
			return null;
		return attribute.getCode();
	}

	@Override
	public ImageType convertToEntityAttribute(Integer dbData) {
		if (dbData == null)
			return null;
		return ImageType.fromCode(dbData);
	}
}

