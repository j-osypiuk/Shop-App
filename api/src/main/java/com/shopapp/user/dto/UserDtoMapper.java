package com.shopapp.user.dto;

import com.shopapp.address.dto.AddressDtoMapper;
import com.shopapp.user.User;

import java.util.List;

public class UserDtoMapper {

    public static ResponseUserDto mapUserToResponseUserDto(User user) {
        return ResponseUserDto.builder()
                .id(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .birthDate(user.getBirthDate())
                .gender(user.getGender())
                .phoneNumber(user.getPhoneNumber())
                .address(AddressDtoMapper.mapAddressToResponseAddressDto(user.getAddress()))
                .build();
    }

    public static User mapRequestUserDtoToUser(RequestUserDto postUserDto) {
        return User.builder()
                .firstName(postUserDto.firstName())
                .lastName(postUserDto.lastName())
                .email(postUserDto.email())
                .password(postUserDto.password())
                .birthDate(postUserDto.birthDate())
                .gender(postUserDto.gender())
                .phoneNumber(postUserDto.phoneNumber())
                .address(AddressDtoMapper.mapRequestAddressDtoToAddress(postUserDto.address()))
                .build();
    }

    public static OrderUserDto mapUserToOrderUserDto(User user) {
        return OrderUserDto.builder()
                .id(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    public static List<ResponseUserDto> mapUserListToUserDtoList(List<User> users) {
        return users.stream()
                .map(UserDtoMapper::mapUserToResponseUserDto).toList();
    }
}
