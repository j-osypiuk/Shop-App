package com.example.shopapp.discount;

import com.example.shopapp.exception.DuplicateUniqueValueException;
import com.example.shopapp.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DiscountServiceImplTest {

    @Mock
    private DiscountRepository discountRepository;
    private DiscountServiceImpl discountService;

    @BeforeEach
    void setUp() {
        discountService = new DiscountServiceImpl(discountRepository);
    }

    @Test
    void saveDiscountSavesDiscount() throws DuplicateUniqueValueException {
        // given
        Discount discount = Discount.builder().name("Winter discount").build();
        given(discountRepository.existsDiscountByName(discount.getName())).willReturn(false);
        given(discountRepository.save(discount)).willReturn(discount);
        // when
        Discount savedDiscount = discountService.saveDiscount(discount);
        // then
        ArgumentCaptor<Discount> discountCaptor = ArgumentCaptor.forClass(Discount.class);
        ArgumentCaptor<String> discountNameCaptor = ArgumentCaptor.forClass(String.class);
        verify(discountRepository).existsDiscountByName(discountNameCaptor.capture());
        verify(discountRepository).save(discountCaptor.capture());
        assertThat(discountNameCaptor.getValue()).isEqualTo(discount.getName());
        assertThat(discountCaptor.getValue()).isEqualTo(savedDiscount);
        assertEquals(savedDiscount, discount);
    }

    @Test
    void saveDiscountThrowsExceptionIfOtherExistingDiscountHasTheSameName() {
        // given
        Discount discount = Discount.builder().name("Winter discount").build();
        given(discountRepository.existsDiscountByName(discount.getName())).willReturn(true);
        // when
        // then
        ArgumentCaptor<String> discountNameCaptor = ArgumentCaptor.forClass(String.class);
        assertThatThrownBy(() -> discountService.saveDiscount(discount))
                .isInstanceOf(DuplicateUniqueValueException.class)
                .hasMessageContaining("Discount with name = " + discount.getName() + " already exists");
        verify(discountRepository).existsDiscountByName(discountNameCaptor.capture());
        verify(discountRepository, never()).save(discount);
        assertThat(discountNameCaptor.getValue()).isEqualTo(discount.getName());
    }

    @Test
    void getDiscountByIdReturnsDiscount() throws ObjectNotFoundException {
        // given
        Long discountId = 1L;
        Discount discount = Discount.builder().discountId(discountId).build();
        given(discountRepository.findById(discountId)).willReturn(Optional.of(discount));
        // when
        Discount foundDiscount = discountService.getDiscountById(discountId);
        // then
        ArgumentCaptor<Long> discountIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(discountRepository).findById(discountIdCaptor.capture());
        assertThat(discountIdCaptor.getValue()).isEqualTo(discountId);
        assertEquals(foundDiscount, discount);
    }

    @Test
    void getDiscountByIdThrowsExceptionIfDiscountDoesNotExists() {
        // given
        Long discountId = 1L;
        given(discountRepository.findById(discountId)).willReturn(Optional.empty());
        // when
        // then
        ArgumentCaptor<Long> discountIdCaptor = ArgumentCaptor.forClass(Long.class);
        assertThatThrownBy(() -> discountService.getDiscountById(discountId))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("Discount with id = " + discountId + " not found");
        verify(discountRepository).findById(discountIdCaptor.capture());
        assertThat(discountIdCaptor.getValue()).isEqualTo(discountId);
    }

    @Test
    void getAllDiscountsReturnsListOfDiscounts() throws ObjectNotFoundException {
        // given
        Discount discount1 = Discount.builder().name("Winter discount").description("Winter discount description").build();
        Discount discount2 = Discount.builder().name("Summer discount").description("Summer discount description").build();
        given(discountRepository.findAll()).willReturn(Arrays.asList(discount1, discount2));
        // when
        List<Discount> categories = discountService.getAllDiscounts();
        // then
        verify(discountRepository).findAll();
        assertEquals(categories.size(), 2);
        assertEquals(categories.get(0), discount1);
        assertEquals(categories.get(1), discount2);
    }

    @Test
    void getAllDiscountsThrowsExceptionIfNoDiscountsExists() {
        // given
        given(discountRepository.findAll()).willReturn(Collections.emptyList());
        // when
        // then
        assertThatThrownBy(() -> discountService.getAllDiscounts())
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("No discounts found");
        verify(discountRepository).findAll();
    }
    @Test
    void updateDiscountByIdUpdatesDiscount() throws ObjectNotFoundException, DuplicateUniqueValueException {
        // given
        Long discountId = 1L;
        Discount foundDiscount = Discount.builder()
                .name("Winter discount")
                .description("Winter discount description")
                .discountPercent(35)
                .build();
        Discount discount = Discount.builder()
                .name("Summer discount")
                .description("Summer discount description")
                .discountPercent(15)
                .build();
        given(discountRepository.findById(discountId)).willReturn(Optional.of(foundDiscount));
        given(discountRepository.existsDiscountByName(discount.getName())).willReturn(false);
        given(discountRepository.save(foundDiscount)).willReturn(foundDiscount);
        // when
        Discount updatedDiscount = discountService.updateDiscountById(discountId, discount);
        // then
        ArgumentCaptor<Discount> discountCaptor = ArgumentCaptor.forClass(Discount.class);
        ArgumentCaptor<Long> discountIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> discountNameCaptor = ArgumentCaptor.forClass(String.class);
        verify(discountRepository).findById(discountIdCaptor.capture());
        verify(discountRepository).existsDiscountByName(discountNameCaptor.capture());
        verify(discountRepository).save(discountCaptor.capture());
        assertThat(discountIdCaptor.getValue()).isEqualTo(discountId);
        assertThat(discountNameCaptor.getValue()).isEqualTo(discount.getName());
        assertThat(discountCaptor.getValue()).isEqualTo(discount);
        assertEquals(updatedDiscount, discount);
    }

    @Test
    void updateDiscountByIdThrowsExceptionIfDiscountWithGivenIdDoesNotExists() {
        // given
        Long discountId = 1L;
        Discount discount = Discount.builder().name("Summer discount").description("Summer discount description").build();
        given(discountRepository.findById(discountId)).willReturn(Optional.empty());
        // when
        // then
        ArgumentCaptor<Long> discountIdCaptor = ArgumentCaptor.forClass(Long.class);
        assertThatThrownBy(() -> discountService.updateDiscountById(discountId, discount))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("Discount with id = " + discountId + " not found");
        verify(discountRepository).findById(discountIdCaptor.capture());
        verify(discountRepository, never()).existsDiscountByName(discount.getName());
        verify(discountRepository, never()).save(discount);
        assertThat(discountIdCaptor.getValue()).isEqualTo(discountId);
    }

    @Test
    void updateDiscountByIdThrowsExceptionIfOtherExistingDiscountHasTheSameName() {
        // given
        Long discountId = 1L;
        Discount discount = Discount.builder().name("Summer discount").description("Summer discount description").build();
        given(discountRepository.findById(discountId)).willReturn(Optional.of(discount));
        given(discountRepository.existsDiscountByName(discount.getName())).willReturn(true);
        // when
        // then
        ArgumentCaptor<Long> discountIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> discountNameCaptor = ArgumentCaptor.forClass(String.class);
        assertThatThrownBy(() -> discountService.updateDiscountById(discountId, discount))
                .isInstanceOf(DuplicateUniqueValueException.class)
                .hasMessageContaining("Discount with name = " + discount.getName() + " already exists");
        verify(discountRepository).findById(discountIdCaptor.capture());
        verify(discountRepository).existsDiscountByName(discountNameCaptor.capture());
        verify(discountRepository, never()).save(discount);
        assertThat(discountIdCaptor.getValue()).isEqualTo(discountId);
        assertThat(discountNameCaptor.getValue()).isEqualTo(discount.getName());
    }

    @Test
    void deleteDiscountByIdDeletesDiscount() throws ObjectNotFoundException {
        // given
        Long discountId = 1L;
        given(discountRepository.deleteDiscountById(discountId)).willReturn(1);
        // when
        discountService.deleteDiscountById(discountId);
        // then
        ArgumentCaptor<Long> discountIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(discountRepository).deleteDiscountById(discountIdCaptor.capture());
        assertThat(discountIdCaptor.getValue()).isEqualTo(discountId);
    }

    @Test
    void deleteDiscountByIdThrowsExceptionIfDiscountWithGivenIdDoesNotExists() {
        // given
        Long discountId = 1L;
        given(discountRepository.deleteDiscountById(discountId)).willReturn(0);
        // when
        // then
        ArgumentCaptor<Long> discountIdCaptor = ArgumentCaptor.forClass(Long.class);
        assertThatThrownBy(() -> discountService.deleteDiscountById(discountId))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("Discount with id = " + discountId + " not found");
        verify(discountRepository).deleteDiscountById(discountIdCaptor.capture());
        assertThat(discountIdCaptor.getValue()).isEqualTo(discountId);
    }
}