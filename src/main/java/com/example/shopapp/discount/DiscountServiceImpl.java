package com.example.shopapp.discount;

import com.example.shopapp.discount.dto.DiscountDtoMapper;
import com.example.shopapp.discount.dto.RequestDiscountDto;
import com.example.shopapp.discount.dto.ResponseDiscountDto;
import com.example.shopapp.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountServiceImpl implements DiscountService{

    private final DiscountRepository discountRepository;

    public DiscountServiceImpl(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    @Override
    public ResponseDiscountDto saveDiscount(RequestDiscountDto requestDiscountDto) {
        Discount discountDB = discountRepository
                .save(DiscountDtoMapper.mapRequestDiscountDtoToDiscount(requestDiscountDto));
        return DiscountDtoMapper.mapDiscountToResponseDiscountDto(discountDB);
    }

    @Override
    public ResponseDiscountDto getDiscountById(Long id) throws ObjectNotFoundException {
        Discount discountDB = discountRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Discount with id = " + id + " not found"));

        return DiscountDtoMapper.mapDiscountToResponseDiscountDto(discountDB);
    }

    @Override
    public List<ResponseDiscountDto> getAllDiscounts() throws ObjectNotFoundException {
        List<Discount> discountsDB = discountRepository.findAll();

        if (discountsDB.isEmpty()) throw new ObjectNotFoundException("No discounts found");

        return DiscountDtoMapper.mapDiscountListToResponseDiscountDtoList(discountsDB);
    }

    @Override
    public ResponseDiscountDto updateDiscountById(Long id, RequestDiscountDto requestDiscountDto) throws ObjectNotFoundException {
        Discount discountDB = discountRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Discount with id = " + id + " not found"));

        if (!requestDiscountDto.name().equals(discountDB.getName()))
            discountDB.setName(requestDiscountDto.name());
        if (!requestDiscountDto.description().equals(discountDB.getDescription()))
            discountDB.setDescription(requestDiscountDto.description());
        if (requestDiscountDto.discountPercent() != discountDB.getDiscountPercent())
            discountDB.setDiscountPercent(requestDiscountDto.discountPercent());

        discountRepository.save(discountDB);
        return DiscountDtoMapper.mapDiscountToResponseDiscountDto(discountDB);
    }

    @Override
    public void deleteDiscountById(Long id) throws ObjectNotFoundException {
        Integer isDeleted = discountRepository.deleteDiscountByDiscountId(id);

        if (isDeleted == 0) throw new ObjectNotFoundException("Discount with id = " + id + " not found");
    }
}
