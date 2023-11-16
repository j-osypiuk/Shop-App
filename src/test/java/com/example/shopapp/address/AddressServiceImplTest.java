package com.example.shopapp.address;

import com.example.shopapp.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    @Mock
    AddressRepository addressRepository;
    AddressServiceImpl addressService;

    @BeforeEach
    void setUp() {
        addressService = new AddressServiceImpl(addressRepository);
    }

    @Test
    void updateAddressByIdUpdatesAddress() throws ObjectNotFoundException {
        // given
        Long addressId = 1L;
        Address address = Address.builder()
                .country("England")
                .region("Sth London")
                .city("London")
                .street("Johnson Avenue")
                .number("24")
                .postalCode("32423")
                .build();

        Address foundAddress = Address.builder()
                .country("Poland")
                .region("Mazovia")
                .city("Warsaw")
                .street("Zlota")
                .number("34")
                .postalCode("2334")
                .build();
        given(addressRepository.findById(addressId)).willReturn(Optional.of(foundAddress));
        given(addressRepository.save(foundAddress)).willReturn(foundAddress);
        // when
        Address updatedAddress = addressService.updateAddressById(addressId, address);
        // then
        ArgumentCaptor<Address> addressCaptor = ArgumentCaptor.forClass(Address.class);
        ArgumentCaptor<Long> addressIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(addressRepository).findById(addressIdCaptor.capture());
        verify(addressRepository).save(addressCaptor.capture());
        assertThat(addressIdCaptor.getValue()).isEqualTo(addressId);
        assertThat(addressCaptor.getValue()).isEqualTo(address);
        assertEquals(updatedAddress, address);
    }

    @Test
    void updateAddressByIdThrowsExceptionIfAddressWithGivenIdDoesNotExists() throws ObjectNotFoundException {
        // given
        Long addressId = 1L;
        Address address = Address.builder()
                .country("England")
                .region("Sth London")
                .city("London")
                .street("Johnson Avenue")
                .number("24")
                .postalCode("32423")
                .build();
        given(addressRepository.findById(addressId)).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> addressService.updateAddressById(addressId, address))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("Address with id = " + addressId + " not found");
        ArgumentCaptor<Long> addressIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(addressRepository).findById(addressIdCaptor.capture());
        verify(addressRepository, never()).save(address);
        assertThat(addressIdCaptor.getValue()).isEqualTo(addressId);
    }
}