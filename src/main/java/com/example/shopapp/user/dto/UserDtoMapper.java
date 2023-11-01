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
                user.getBirthDate(),
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

    public static User mapPostUserDtoToUser(PostUserDto postUserDto) {
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

    public static User mapPutUserDtoToUser(PutUserDto putUserDto) {
        return User.builder()
                .firstName(putUserDto.firstName())
                .lastName(putUserDto.lastName())
                .birthDate(putUserDto.birthDate())
                .gender(putUserDto.gender())
                .phoneNumber(putUserDto.phoneNumber())
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
