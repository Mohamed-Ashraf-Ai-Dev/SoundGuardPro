cmake_minimum_required(VERSION 3.18.1)

project("soundguardpro")

add_library(
        soundguardpro
        SHARED
        native-lib.cpp)

find_library(
        log-lib
        log)

target_link_libraries(
        soundguardpro
        ${log-lib})
