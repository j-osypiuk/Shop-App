package com.example.shopapp.discount;

import com.example.shopapp.discount.dto.RequestDiscountDto;
import com.example.shopapp.discount.dto.ResponseDiscountDto;
import com.example.shopapp.discount.dto.DiscountDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiscountServiceImpl implements DiscountService{

    @Autowired
    DiscountRepository discountRepository;

    @Override
    public ResponseDiscountDto saveDiscount(RequestDiscountDto requestDiscountDto) {
        Discount discountDB = discountRepository
                .save(DiscountDtoMapper.mapRequestDiscountDtoToDiscount(requestDiscountDto));
        return DiscountDtoMapper.mapDiscountToResponseDiscountDto(discountDB);
    }

    @Override
    public ResponseDiscountDto getDiscountById(Long id) {
        Discount discountDB = discountRepository.findById(id).get();
        return DiscountDtoMapper.mapDiscountToResponseDiscountDto(discountDB);
    }

    @Override
    public List<ResponseDiscountDto> getAllDiscounts() {
        List<Discount> discountsDB = discountRepository.findAll();
        return DiscountDtoMapper.mapDiscountListToResponseDiscountDtoList(discountsDB);
    }

    @Override
    public ResponseDiscountDto updateDiscountById(Long id, RequestDiscountDto requestDiscountDto) {
        Optional<Discount> discountDB = discountRepository.findById(id);

        if (requestDiscountDto.name().equals(discountDB.get().getName()))
            discountDB.get().setName(requestDiscountDto.name());
        if (requestDiscountDto.description().equals(discountDB.get().getDescription()))
            discountDB.get().setDescription(requestDiscountDto.description());
        if (requestDiscountDto.discountPercent() != discountDB.get().getDiscountPercent())
            discountDB.get().setDiscountPercent(requestDiscountDto.discountPercent());

        Discount updatedDiscount = discountRepository.save(discountDB.get());
        return DiscountDtoMapper.mapDiscountToResponseDiscountDto(updatedDiscount);
    }

    @Override
    public void deleteDiscountById(Long id) {
        discountRepository.deleteById(id);
    }
}
