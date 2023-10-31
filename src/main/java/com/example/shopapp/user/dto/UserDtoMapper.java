package com.example.shopapp.user.dto;

import com.example.shopapp.address.dto.AddressDtoMapper;
import com.example.shopapp.address.dto.ResponseAddressDto;
import com.example.shopapp.user.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserDtoMapper {

    public static ResponseUserDto mapUserToResponseUserDto(User user) {
        return new ResponseUserDto(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getAge(),
                user.getGender(),
                user.getPhoneNumber(),
                new ResponseAddressDto(
                        user.getAddress().getAddressId(),
                        user.getAddress().getCountry(),
                        user.getAddress().getRegion(),
                        user.getAddress().getCity(),
                        user.getAddress().getStreet(),
                        user.getAddress().getNumber(),
                        user.getAddress().getPostalCode()
                )
        );
    }

    public static User mapRequestUserDtoToUser(RequestUserDto requestUserDto) {
        return User.builder()
                .firstName(requestUserDto.firstName())
                .lastName(requestUserDto.lastName())
                .email(requestUserDto.email())
                .password(requestUserDto.password())
                .age(requestUserDto.age())
                .gender(requestUserDto.gender())
                .phoneNumber(requestUserDto.phoneNumber())
                .address(AddressDtoMapper.mapRequestAddressDtoToAddress(requestUserDto.address()))
                .build();
    }

    public static OrderUserDto mapUserToOrderUserDto(User user) {
        return new OrderUserDto(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber()
        );
    }

    public static List<ResponseUserDto> mapUserListToUserDtoList(List<User> users) {
        return users.stream()
                .map(UserDtoMapper::mapUserToResponseUserDto).collect(Collectors.toList());
    }
}
